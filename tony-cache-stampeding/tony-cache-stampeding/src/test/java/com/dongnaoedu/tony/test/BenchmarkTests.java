package com.dongnaoedu.tony.test;

import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dongnaoedu.tony.service.TicketService;
import com.dongnaoedu.tony.service.TicketService2;
import com.dongnaoedu.tony.service.TicketServiceLock;

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
	TicketService ticketService;
	
	// 方案1 升级版
	@Autowired
	TicketServiceLock ticketServiceLock;
	
	@Autowired
	TicketService2 ticketService2;
	
	// 车次
	private static final String TICKET_SEQ = "G296";

	// 模拟的请求数量
	private static final int threadNum = 2000;

	// 倒计数器，用于模拟高并发（信号枪机制）
	private CountDownLatch cdl = new CountDownLatch(threadNum);

	long timed = 0L;

	@Before
	public void start() {
		System.out.println("开始测试");
		timed = System.currentTimeMillis();
	}

	@After
	public void end() {
		System.out.println("结束测试,执行时长：" + (System.currentTimeMillis() - timed));
	}

	@Test
	public void benchmark() throws InterruptedException {
		// 创建 并不是马上发起请求
		Thread[] threads = new Thread[threadNum];
		for (int i = 0; i < threadNum; i++) {
			Thread thread = new Thread(new QueryRequest());
			threads[i] = thread;

			thread.start();
			// 田径。倒计时器倒计数 减一
			cdl.countDown();
		}

		// 等待上面所有线程执行完毕之后，结束测试
		for (Thread thread : threads) {
			thread.join();
		}
	}

	/** 多线程模拟用户查询请求 */
	private class QueryRequest implements Runnable {
		@Override
		public void run() {
			try {
				cdl.await();// 等待其他线程就绪后，再运行后续的代码
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// http请求实际上就是多线程调用这个方法
			ticketService2.queryTicketStock(TICKET_SEQ);
		}

	}

}
