package com.dongnaoedu.tony.test;

import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dongnaoedu.tony.service.MiaoshaService;

import redis.clients.jedis.Jedis;

/**
 * 压力测试
 * 
 * @author Tony
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class BenchmarkTests {

	@Autowired
	MiaoshaService miaoshaService;

	// 秒杀商品编码
	private static final String GOODS_CODE = "iphoneX";

	// 模拟的请求数量
	private static final int threadNum = 2000;

	// 倒计数器，用于模拟高并发（信号枪机制）
	private CountDownLatch cdl = new CountDownLatch(threadNum);

	long timed = 0L;

	@Before
	public void start() {
		System.out.println("开始测试");
		// 初始化Token
		Jedis jedis = new Jedis();
		jedis.del("token_list");
		for (int i = 0; i < 100; i++) {
			jedis.lpush("token_list", String.valueOf(i));
		}
		jedis.close();
		System.out.println("300个令牌初始化完成");
	}

	@After
	public void end() {
		System.out.println("结束测试,执行时长：" + (System.currentTimeMillis() - timed));
	}

	@Test
	public void benchmark() throws InterruptedException {
		// 创建
		Thread[] threads = new Thread[threadNum];
		for (int i = 0; i < threadNum; i++) {
			String userId = "Tony_" + i;
			// String userId = "Tony_";

			Thread thread = new Thread(new UserRequest(userId));
			threads[i] = thread;

			thread.start();
			// 倒计时器倒计数一次
			cdl.countDown();
		}

		// 等待上面所有线程执行完毕之后，结束测试
		for (Thread thread : threads) {
			thread.join();
		}
	}

	/** 多线程模拟用户查询请求 */
	private class UserRequest implements Runnable {

		String userId;

		public UserRequest(String userId) {
			this.userId = userId;
		}

		@Override
		public void run() {
			try {
				cdl.await();// 等待其他线程就绪后，再运行后续的代码
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// http请求实际上就是多线程调用这个方法
			miaoshaService.miaosha(GOODS_CODE, userId);
		}

	}

}
