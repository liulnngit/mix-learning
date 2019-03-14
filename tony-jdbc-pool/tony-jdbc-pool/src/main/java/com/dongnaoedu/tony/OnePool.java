package com.dongnaoedu.tony;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class OnePool implements TonyPoolInterface {

	// 繁忙
	LinkedBlockingQueue<TonyJdbcConnect> busy;

	// 空闲
	LinkedBlockingQueue<TonyJdbcConnect> idle;

	int max;
	long maxWait;
	int idleCount;

	// 连接池大小
	AtomicInteger activeSize = new AtomicInteger(0);

	@Override
	public void init(int max, long maxWait, int idleCount) {
		this.max = max;
		this.maxWait = maxWait;
		this.idleCount = idleCount;

		busy = new LinkedBlockingQueue<>();
		idle = new LinkedBlockingQueue<>();
	}

	@Override
	public TonyJdbcConnect getResource() throws Exception {
		// 1. 记录一个进入方法的时间，因为会有等待超时的情况
		long now = System.currentTimeMillis();

		TonyJdbcConnect tonyJdbcConnect = null;

		// 2. 从空闲集合中获取一个redis连接对象 ;
		// TODO 知识点 - queue接口方法介绍
		tonyJdbcConnect = idle.poll();

		while (true) {

			if (tonyJdbcConnect != null) {
				busy.offer(tonyJdbcConnect);
				return tonyJdbcConnect;
			}

			// 3. 判断连接池数量是否满了，; 如果没满的就构建一个新的数据库连接，并且放到繁忙集合中 return
			// TODO 知识点 - 线程安全
			if (activeSize.get() < max) { // i=9
				if (activeSize.incrementAndGet() <= max) { // AtomicInteger
					tonyJdbcConnect = new TonyJdbcConnect();
					busy.offer(tonyJdbcConnect);
					return tonyJdbcConnect;
				} else {
					activeSize.decrementAndGet();
				}
			}

			// 4. 等待等待其他线程是否连接 ; 如果等到了一个被释放的连接，放到繁忙集合中，就return
			try {
				// 实现方案：sleep？
				tonyJdbcConnect = idle.poll(maxWait - (System.currentTimeMillis() - now), TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				// TODO 知识点 - 线程中断异常：InterruptedException
				throw new Exception("线程中断...");
			}

			// 5. 等了很久，还没有等到。超过过了maxWait，抛出等待超时的异常
			if (tonyJdbcConnect == null && (System.currentTimeMillis() - now) > maxWait) {
				throw new Exception("timeout，超时了...");
			}

		} // 结束while
	}

	@Override
	public void returnConnect(TonyJdbcConnect connect) {
		if (connect == null) {
			return;
		}

		// 移出繁忙
		boolean removeResult = busy.remove(connect);
		if (removeResult) {
			// 1. 如果空闲的太多，则直接关闭连接
			if (idleCount < idle.size()) {
				releaseConnect(connect);
				return;
			}

			// 2. 如果加入空闲队列的操作失败，则关闭连接
			boolean success = idle.offer(connect);
			if (!success) {
				releaseConnect(connect);
			}

		} else {
			// 3. 放回pool的时候，没有成功的从队列移除,则直接关闭连接
			releaseConnect(connect);
		}
	}

	private void releaseConnect(TonyJdbcConnect connect) {
		connect.close();
		// 连接池的总数 减一
		activeSize.decrementAndGet();
	}

}
