package com.dongnaoedu.tony.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dongnaoedu.tony.service.OrderService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 压力测试
 * 
 * @author Tony
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class WebaTests {

	@Autowired
	OrderService ticketService;

	static String QUEUE_NAME = "tony";

	@Test
	public void sendTest1() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.207.62.159");
		factory.setUsername("admin");
		factory.setPassword("12345678");
		// factory.setPort(5672);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.confirmSelect();
		channel.exchangeDeclare("createOrderExchange", BuiltinExchangeType.FANOUT);
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		channel.queueBind(QUEUE_NAME, "createOrderExchange", "*");
		
		String message = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		channel.addConfirmListener(new ConfirmListener() {
			
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println(deliveryTag);
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println(deliveryTag);
			}
		});
		Thread.sleep(1000L);
		channel.close();
		connection.close();
	}

	@Test
	public void reciTest1() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.207.62.159");
		factory.setVirtualHost("/");
		factory.setUsername("admin");
		factory.setPassword("12345678");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

//		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}

}
