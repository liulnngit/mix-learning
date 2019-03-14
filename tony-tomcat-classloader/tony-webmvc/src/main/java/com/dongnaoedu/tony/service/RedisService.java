package com.dongnaoedu.tony.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.codahale.metrics.annotation.Timed;

import redis.clients.jedis.Jedis;

@Service
public class RedisService {

	private final Logger logger = Logger.getLogger(RedisService.class);

	@Timed // 收集这个方法的调用次数，平均响应时间，最大最小响应时间....
	public String queryFromRedis(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = new Jedis();
			// 从redis中获取
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return value;

	}
}
