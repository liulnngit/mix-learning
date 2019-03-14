package com.dongnaoedu.coupon.api;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dongnaoedu.coupon.service.DispatchService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 消费者，取调度队列
 * @author Tony
 *
 */
@Component
public class OrderDispatchConsumer {
	private final Logger logger = Logger.getLogger(OrderDispatchConsumer.class);

	@Autowired
	DispatchService dispatchService;
	Connection connection = null;
	Channel channel = null;

	@PostConstruct // 对象初始化的时候会调用这个方法
	public void messageConsumer() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		// 1 与rabbitmq连接
		factory.setHost("mq.tony.com"); // 服务器地址
		factory.setUsername("admin"); // mq账户名
		factory.setPassword("12345678"); // mq密码
		factory.setPort(5672); // mq端口
		connection = factory.newConnection(); // 建立链接
		channel = connection.createChannel(); // 打开通道

		// 2 消费quque里面的内容
		// 2.1 创建消费者实现
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				// 执行业务操作
				try {
					// mq里面的数据转为json对象
					JSONObject message = JSONObject.parseObject(new String(body));
					logger.warn("收到MQ里面的消息：" + message.toJSONString());
					String orderId = message.getString("orderId");
					dispatchService.dispatch(orderId);
					// ack - 告诉MQ，我已经收到啦
					channel.basicAck(envelope.getDeliveryTag(), false);
				} catch (SQLException exception) {
					logger.error("MQ消费者报错啦，这个错误我们自己处理，不需要再发了", exception);
					// Nack - 告诉MQ，我处理有点问题，但是这个问题我能处理，不用继续给我发了 丢弃或者丢到死信队列
					channel.basicNack(envelope.getDeliveryTag(), false, false);
				} catch (Exception e) {
					logger.error("出现了意料之外的异常，再重发一次", e);
					// Nack - 告诉MQ，我收到了，但是有意料不到的异常，再给我发一次。
					// requeue: true是继续， false是丢弃或者丢到死信队列
					channel.basicNack(envelope.getDeliveryTag(), false, true);
					// 根据不同的异常，和业务需要，采取不通的措施
				}
				// 如果不给回复，就等这个consumer断开链接后再继续
			}
		};
		// 2.2 开始消费(开启ack模式)
		channel.basicConsume("orderDispatchQueue", false, consumer);
		logger.warn("开始消费啦");
	}

	@PreDestroy // 对象销毁的时候会调用这个方法
	public void destory() throws Exception {
		// 断开链接
		if (channel != null) {
			channel.close();
		}
		if (connection != null) {
			connection.close();
		}
	}}
