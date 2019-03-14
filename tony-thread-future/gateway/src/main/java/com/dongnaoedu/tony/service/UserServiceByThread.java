package com.dongnaoedu.tony.service;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * 多线程.优化
 * 
 * @author 动脑学院.Tony老师
 *
 */
@Component
public class UserServiceByThread {

	private final Logger logger = Logger.getLogger(UserServiceByThread.class);

	@Autowired
	private RestTemplate restTemplate;

	public Object getUserInfo(final String userId) throws Exception {
		
		// 1. 通过callable封装我们的业务代码
		Callable<JSONObject> userInfoCallable = new Callable<JSONObject>() {
			@Override
			public JSONObject call() throws Exception {
				// 1. 先从调用获取用户基础信息的http接口
				long userinfoTime = System.currentTimeMillis();
				String value = restTemplate.getForObject("http://www.tony.com/userinfo-api/get?userId=" + userId, String.class);
				JSONObject userInfo = JSONObject.parseObject(value);
				logger.error("userinfo-api用户基本信息接口调用时间为" + (System.currentTimeMillis() - userinfoTime));
				return userInfo;
			}
		};
		
		Callable<JSONObject> intergralInfoCallable = new Callable<JSONObject>() {
			@Override
			public JSONObject call() throws Exception {
				// 2. 再调用获取用户积分信息的接口
				long integralApiTime = System.currentTimeMillis();
				String intergral = restTemplate.getForObject("http://www.tony.com/integral-api/get?userId=" + userId,
						String.class);
				JSONObject intergralInfo = JSONObject.parseObject(intergral);
				logger.error("integral-api积分接口调用时间为" + (System.currentTimeMillis() - integralApiTime));
				return intergralInfo;
			}
		};
		// 2. FutureTask包装callable
		FutureTask<JSONObject> userInfoFutureTask = new FutureTask<>(userInfoCallable);
		FutureTask<JSONObject> intergralInfoFutureTask = new FutureTask<>(intergralInfoCallable);
		
		// 3. 运行FutureTask(PS.生成环境，还是用线程池)
		new Thread(userInfoFutureTask).start();
		new Thread(intergralInfoFutureTask).start();
		
		// TODO 在future执行过程中，还可以作业务 
		
		
		// 4. 合并为一个json对象
		JSONObject result = new JSONObject();
		// 用户主线程 FutureTask 等待执行完毕。在get方法有返回值之前，这个用户线程 会停在这里，不会继续往下执行
		// 消费者
		result.putAll(userInfoFutureTask.get());
		result.putAll(intergralInfoFutureTask.get());

		return result;
	}

}
