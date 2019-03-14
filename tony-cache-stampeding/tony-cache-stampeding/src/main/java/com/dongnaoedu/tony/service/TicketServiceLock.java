package com.dongnaoedu.tony.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongnaoedu.tony.utils.RedisCacheUtils;

import redis.clients.jedis.Jedis;

@Service
public class TicketServiceLock {

	private final Logger logger = Logger.getLogger(TicketService.class);

	@Resource(name = "mainRedisCache")
	RedisCacheUtils mainRedisCache;

	@Autowired
	DatabaseService databaseService;

	Lock lock = new ReentrantLock();

	/**
	 * 查询车次余票
	 * 
	 * @param ticketSeq
	 *            车次
	 * @return 余票数量
	 */
	public Object queryTicketStock(String ticketSeq) {
		// 1. 2000并发请求
		Jedis jedis = mainRedisCache.getJedis();
		// 1. 先从redis中获取余票信息
		String value = jedis.get(ticketSeq);
		if (value != null) {
			logger.warn(Thread.currentThread().getName() + "缓存中取得数据==============>" + value);
			return value;
		}

		// 2000线程并发
		// 互斥锁 拿到锁的才有资格 访问数据并重建缓存
		lock.lock(); // 没拿到锁的 线程 等待锁   1999 等待
		// 1个线程拿到锁
		try {
			// 再次读取缓存
			value = jedis.get(ticketSeq);
			if (value != null) {
				logger.warn(Thread.currentThread().getName() + "缓存中取得数据==============>" + value);
				return value;
			}

			// 2. 缓存中没有则取数据库
			value = databaseService.queryFromDatabase(ticketSeq);
			System.out.println(Thread.currentThread().getName() + "从数据库中取得数据==============>" + value);

			// 3. 塞到缓存 120秒过期时间，一致性
			jedis.setex(ticketSeq, 120, value);
		} finally {
			lock.unlock();
		}

		jedis.close();
		return value;
	}
}
