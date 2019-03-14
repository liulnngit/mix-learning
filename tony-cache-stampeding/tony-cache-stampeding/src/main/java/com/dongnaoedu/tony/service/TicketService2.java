package com.dongnaoedu.tony.service;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongnaoedu.tony.utils.RedisCacheUtils;

import redis.clients.jedis.Jedis;

// 降级策略
@Service
public class TicketService2 {

	private final Logger logger = Logger.getLogger(TicketService.class);

	@Resource(name = "mainRedisCache")
	RedisCacheUtils mainRedisCache;

	@Resource(name = "bakRedisCache")
	RedisCacheUtils bakRedisCache;

	@Autowired
	DatabaseService databaseService;

	// 不局限于形式：redis，set
	ConcurrentHashMap<String, String> maplock = new ConcurrentHashMap<>();

	/**
	 * 查询车次余票
	 * 
	 * @param ticketSeq
	 *            车次
	 * @return 余票数量
	 */
	public Object queryTicketStock(String ticketSeq) {
		Jedis jedis = mainRedisCache.getJedis();
		// 1. 先从redis中获取余票信息
		String value = jedis.get(ticketSeq);
		if (value != null) {
			logger.warn(Thread.currentThread().getName() + "缓存中取得数据==============>" + value);
			return value;
		}

		// 单车次 2000并发
		// 解决方案2：缓存降级
		boolean lock = false;
		try {
			// 细粒度，每一个车次
			lock = maplock.putIfAbsent(ticketSeq, ticketSeq + "") == null;

			if (lock) {
				// 拿到锁
				// 2. 缓存中没有则取数据库
				value = databaseService.queryFromDatabase(ticketSeq);
				System.out.println(Thread.currentThread().getName() + "从数据库中取得数据==============>" + value);

				// 3. 塞到主缓存 120秒过期时间，一致性
				jedis.setex(ticketSeq, 120, value);

				// 4. 塞到备份缓存， 永不过期的,后台线程异步更新
				Jedis bakJedis = bakRedisCache.getJedis();
				bakJedis.set(ticketSeq, value);
				bakJedis.close();

			} else {
				// 降级
				// >读备份缓存
				Jedis bakJedis = bakRedisCache.getJedis();
				value = bakJedis.get(ticketSeq);
				bakJedis.close();
				if (value != null) {
					logger.warn(Thread.currentThread().getName() + "缓存降级：备份缓存取得数据==============>" + value);
				} else {
					// > 没拿到锁 降级 ，根据业务进行降级
					value = "0";// 次要的
					logger.warn(Thread.currentThread().getName() + "缓存降级：固定值取得数据==============>" + value);
				}
			}

		} finally {
			if (lock) {
				maplock.remove(ticketSeq);
			}
		}

		jedis.close();
		return value;
	}
}
