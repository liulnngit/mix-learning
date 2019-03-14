package com.dongnaoedu.tony.service;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 读取orderCreateQueue消息队列的信息
 * <p>判断订单在数据库有没有，实现补单</p>
 * @author Tony
 *
 */
@Component
public class OrderCreateConsumer {
	private final Logger logger = Logger.getLogger(OrderCreateConsumer.class);

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
				// mq里面的数据转为json对象
				JSONObject message = JSONObject.parseObject(new String(body));
				
				logger.warn("收到MQ里面的消息：" + message.toJSONString());
				logger.warn("根据MQ里面的订单数据，判断订单数据库是否存在该数据。");
				logger.warn("如果不存在，则补插入订单数据，防止出现漏单");
				
				// ack - 告诉MQ，我已经收到啦
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		// 2.2 开始消费(开启ack模式)
		channel.basicConsume("orderCreateQueue", false, consumer);
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
	}
}
