package com.dongnaoedu.tony.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongnaoedu.tony.utils.RedisCacheUtils;

import redis.clients.jedis.Jedis;

@Component
public class TicketService {

	private final Logger logger = Logger.getLogger(TicketService.class);

	@Resource(name = "mainRedisCache")
	RedisCacheUtils mainRedisCache;

	@Autowired
	DatabaseService databaseService;

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

		// 2000并发
		// 2. 缓存中没有则取数据库
		value = databaseService.queryFromDatabase(ticketSeq);
		System.out.println(Thread.currentThread().getName() + "从数据库中取得数据==============>" + value);

		// 3. 塞到缓存 120秒过期时间，一致性
		jedis.setex(ticketSeq, 120, value);

		jedis.close();
		return value;
	}
}
