package com.tony.test;

import org.junit.Test;

import com.dongnaoedu.tony.redis.client.Pipeline;
import com.dongnaoedu.tony.redis.client.TonyRedisClient;

public class TontClientTests {

	@Test
	public void getsetTest() throws Exception {
		TonyRedisClient tonyRedisClient = new TonyRedisClient("127.0.0.1", 6379);
		tonyRedisClient.set("tony_test", "你好");

		String value = tonyRedisClient.get("tony_test");
		System.out.println("通过自己写的客户端读取到redis种的数据:");
		System.out.println(value);
	}

	// 高级特性1 - 极致性能利器：pipeline
	@Test
	public void pipelineTest() throws Exception {
		// 普通的方式，操作10000次
		TonyRedisClient client = new TonyRedisClient("127.0.0.1", 6379);

		long now = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			client.set("counter_tony", i + "");
		}
		String value = client.get("counter_tony");
		System.out.println("##################开始打印普通的方式执行结果");
		System.out.println(value);
		System.out.println("执行时间:" + (System.currentTimeMillis() - now));

		// pupeline操作一万次
		client = new TonyRedisClient("127.0.0.1", 6379);

		now = System.currentTimeMillis();
		Pipeline pipelined = client.pipeline();

		for (int i = 0; i < 10000; i++) {
			pipelined.set("counter_pipeline_tony", i + "");
		}
		pipelined.sync();
		value = client.get("counter_pipeline_tony");
		System.out.println("##################开始打印pipeline的方式执行结果");
		System.out.println(value);
		System.out.println("pipeline执行时间:" + (System.currentTimeMillis() - now));
		System.out.println("##################打印结束");

	}
	
	// 订阅
	@Test
	public void subTest() throws Exception {
		TonyRedisClient tonyRedisClient = new TonyRedisClient("127.0.0.1", 6379);
		tonyRedisClient.subscribe().fans("ruolan");
	}
	
}
