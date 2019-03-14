package com.dongnaoedu.tony.service;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @author Tony
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {

	private final Logger logger = Logger.getLogger(OrderService.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 创建订单
	 * 
	 * @throws Exception
	 */
	public void createOrder(String userId, String orderContent) throws Exception {
		// 1. 添加订单记录
		String orderId = UUID.randomUUID().toString();
		int count = jdbcTemplate.update(
				"insert into table_order (order_id, user_id, order_content, create_time) values (?, ?, ?,now())",
				new Object[] { orderId, userId, orderContent });

		if (count != 1) {
			throw new Exception("订单创建失败，原因[数据库操作失败]");
		}

//		// 2. 调用优惠券接口
//		RestTemplate restTemplate = createRestTemplate();
//		String httpUrl = "http://dispatch.tony.com:8080/tony-web-dispatch/dispatch-api/dispatch?orderId=" + orderId;
//		String result = restTemplate.getForObject(httpUrl, String.class);
//		if (!"ok".equals(result)) {
//			throw new Exception("订单创建失败，原因[调度接口失败]");
//		}
		
		// 2. 不直接调用优惠券接口，通过Rabbitmq发布消息通知其他系统进行下一步处理（这一步MQ确认机制）
		// 代码不做封装，原滋原味，底层实现
		ConnectionFactory factory = new ConnectionFactory();
		// 2.1 与rabbitmq连接
		factory.setHost("mq.tony.com"); // 服务器地址
		factory.setUsername("admin"); // mq账户名
		factory.setPassword("12345678"); // mq密码
		factory.setPort(5672); // mq端口
		Connection connection = factory.newConnection(); // 建立链接
		Channel channel = connection.createChannel(); // 打开通道
		// 建议在rabbitmq中进行创建
		channel.exchangeDeclare("createOrderExchange", "fanout"); // 定义一个exchange(重复定义不影响)
		channel.queueDeclare("orderDispatchQueue", true, false, false, null); // 定义一个queue，(重复定义不影响)
		channel.queueBind("orderDispatchQueue", "createOrderExchange", "*"); // 设置绑定关系

		// 2.2 发送消息到Rabitmq
		channel.confirmSelect(); // 开启确认机制，rabbitmq收到以后，会给一个应答
		JSONObject messageJson = new JSONObject();
		messageJson.put("orderId", orderId);
		String message = messageJson.toJSONString();
		channel.basicPublish("createOrderExchange", "", null, message.getBytes());
		channel.waitForConfirmsOrDie(); // 如果返回

		// 2.3 断开链接
		channel.close();
		connection.close();

		logger.warn("订单创建成功");

	}

	// 创建一个HTTP请求工具类
	public RestTemplate createRestTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		// 链接超时时间 > 3秒
		requestFactory.setConnectTimeout(3000);
		// 处理超时时间 > 2 秒
		requestFactory.setReadTimeout(2000);
		return new RestTemplate(requestFactory);
	}
}
