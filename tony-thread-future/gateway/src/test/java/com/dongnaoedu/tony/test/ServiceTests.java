package com.dongnaoedu.tony.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dongnaoedu.tony.service.UserService;
import com.dongnaoedu.tony.service.UserServiceByThread;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ServiceTests {

	private final Logger logger = Logger.getLogger(UserService.class);

	@Before
	public void start() {
		System.out.println("开始测试");
	}

	@After
	public void end() {
		System.out.println("结束测试");
	}

	@Autowired
	UserService userService;

	@Test
	public void testUserSerivce() {
		// 调用
		long currentTimeMillis = System.currentTimeMillis();

		Object userInfo = userService.getUserInfo("tony");

		logger.error("getUserInfo总执行时间为" + (System.currentTimeMillis() - currentTimeMillis));
		System.out.println(userInfo.toString());
	}
	
	@Autowired
	UserServiceByThread userServiceByThread;

	@Test
	public void testUserSerivceThread() throws Exception {
		// 调用
		long currentTimeMillis = System.currentTimeMillis();

		Object userInfo = userServiceByThread.getUserInfo("tony");

		logger.error("getUserInfo总执行时间为" + (System.currentTimeMillis() - currentTimeMillis));
		System.out.println(userInfo.toString());
	}
}
