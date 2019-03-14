package com.tony.test;

import java.io.IOException;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;

/**
 * 
 * Jedis客户端体验
 * 
 * - Tony
 * 
 */
public class JedisTest {

	// set/get
	@Test
	public void jedis() {
		// 常用的客户端 jedis
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		// set hello tony
		// 发送一个命令
		jedis.set("hello", "tony");
		
		//String val = jedis.get("hello");

		//System.out.println(val);
	}

	// 高级特性1 - 极致性能利器：pipeline
	@Test
	public void pipelineTest() {
		// 普通的方式，操作10000次
		Jedis client = new Jedis("127.0.0.1", 6379);

		long now = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			client.set("counter", i + "");
		}
		String value = client.get("counter");
		System.out.println("##################开始打印普通的方式执行结果");
		System.out.println(value);
		System.out.println("执行时间:" + (System.currentTimeMillis() - now));

		// pupeline操作一万次
		client = new Jedis("127.0.0.1", 6379);

		now = System.currentTimeMillis();
		Pipeline pipelined = client.pipelined();

		for (int i = 0; i < 10000; i++) {
			pipelined.set("counter_pipeline", i + "");
		}
		pipelined.sync();
		value = client.get("counter_pipeline");
		System.out.println("##################开始打印pipeline的方式执行结果");
		System.out.println(value);
		System.out.println("pipeline执行时间:" + (System.currentTimeMillis() - now));
		System.out.println("##################打印结束");

	}

	// 高级特性2 - 发布订阅机制(广泛应用于：消息推送，信息同步，高可用哨兵机制，分布式配置中心，dubbo注册中心等等场景)
	@Test
	public void pubSubTest() throws IOException {
		// 创建链接
		Jedis jedis = new Jedis("127.0.0.1", 6379);

		JedisPubSub jedisPubSub = new JedisPubSub() {
			// 取得订阅的消息后的处理
			@Override
			public void onMessage(String channel, String message) {
				System.out.println("收到新消息：" + channel + ">>" + message);
			}
		};
		// 开始订阅
		jedis.subscribe(jedisPubSub, "tony");

		System.out.println("开始订阅Tony的信息");
		System.in.read();
	}

	// 自己搞一个分片存储
	@Test
	public void proxyTest() {
		// 不同长度的key，存在不同的服务器
		// 测试数据 a,ab,abc,abcd，会分散在三台机器上

		// 代理服务器IP
		String proxyHost = "127.0.0.1";
		// 代理服务器端口
		int proxyPort = 19000;

		Jedis jedis = null;
		String response = null;

		jedis = new Jedis(proxyHost, proxyPort);
		response = jedis.set("a", "tony_a");
		jedis.close();
		System.out.println("返回结果:" + response);

		jedis = new Jedis(proxyHost, proxyPort);
		response = jedis.set("ab", "tony_ab");
		jedis.close();
		System.out.println("返回结果:" + response);

		jedis = new Jedis(proxyHost, proxyPort);
		response = jedis.set("abc", "tony_abc");
		jedis.close();
		System.out.println("返回结果:" + response);

		jedis = new Jedis(proxyHost, proxyPort);
		response = jedis.set("abcd", "tony_abcd");
		jedis.close();
		System.out.println("返回结果:" + response);

	}
}
