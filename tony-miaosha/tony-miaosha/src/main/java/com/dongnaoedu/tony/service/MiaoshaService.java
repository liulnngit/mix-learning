package com.dongnaoedu.tony.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongnaoedu.tony.utils.RedisCacheUtils;

import redis.clients.jedis.Jedis;

@Component
public class MiaoshaService {

	private final Logger logger = Logger.getLogger(MiaoshaService.class);

	@Autowired
	RedisCacheUtils redisPool;
	
	@Autowired
	DatabaseService databaseService;

	/**
	 * 秒杀具体实现
	 * 
	 * @param goodsCode
	 *            商品编码
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public boolean miaosha(String goodsCode, String userId) {
		// 方案3: 频率限制
		// 用户操作频率限制,5秒内允许访问一次
		// set setnx setex
		Jedis jedis = redisPool.getJedis();
		String value = jedis.set(userId, "", "NX", "EX", 10);
		if(!"OK".equals(value)) {
			logger.warn("被限制操作频率啦,用户：" + userId);
			return false;
		}
		
		// 方案4： 令牌机制
		// 取令牌，拿到令牌的允许尝试购买
		String token = jedis.lpop("token_list");
		if(token == null || "".equals(token)) {
			logger.warn("没抢到Token，不参与秒杀,用户：" + userId);
			return false;
		}
		
		boolean result = databaseService.buy(goodsCode, userId);
		logger.warn("秒杀结果:" + result);
		return result;
	}
}
