package com.dongnaoedu.tony.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * 串行调用http接口
 * 
 * @author 动脑学院.Tony老师
 *
 */
@Component
public class UserService {

	private final Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private RestTemplate restTemplate;

	public Object getUserInfo(String userId) {
		// 1. 先从调用获取用户基础信息的http接口
		long userinfoTime = System.currentTimeMillis();
		String value = restTemplate.getForObject("http://www.tony.com/userinfo-api/get?userId=" + userId, String.class);
		JSONObject userInfo = JSONObject.parseObject(value);
		logger.error("userinfo-api用户基本信息接口调用时间为" + (System.currentTimeMillis() - userinfoTime));

		// 2. 再调用获取用户积分信息的接口
		long integralApiTime = System.currentTimeMillis();
		String intergral = restTemplate.getForObject("http://www.tony.com/integral-api/get?userId=" + userId,
				String.class);
		JSONObject intergralInfo = JSONObject.parseObject(intergral);
		logger.error("integral-api积分接口调用时间为" + (System.currentTimeMillis() - integralApiTime));
		
		// 3. 合并为一个json对象
		JSONObject result = new JSONObject();
		result.putAll(userInfo);
		result.putAll(intergralInfo);

		return result;
	}

}
