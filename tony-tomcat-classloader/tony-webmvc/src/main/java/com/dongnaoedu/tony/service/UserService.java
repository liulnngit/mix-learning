package com.dongnaoedu.tony.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class UserService {

	private final Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	HttpService httpService;

	@Autowired
	RedisService redisService;

	public Object getUserInfo(String userId) {
		// TODO 省略一万行业务代码
		long currentTimeMillis = System.currentTimeMillis();
		// 先从redis中获取用户信息
		String value = redisService.queryFromRedis(userId);
		JSONObject userInfo = JSONObject.parseObject(value);
		
		// 再从接口获取用户积分信息
		String intergral = httpService.queryForRest(userId);
		JSONObject intergralInfo = JSONObject.parseObject(intergral);
		
		// 合并为一个json对象
		userInfo.putAll(intergralInfo);
		
		
		logger.error("getUserInfo执行时间" + (System.currentTimeMillis() - currentTimeMillis));
		return userInfo;
	}
}
