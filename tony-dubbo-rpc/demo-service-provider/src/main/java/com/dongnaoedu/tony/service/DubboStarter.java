package com.dongnaoedu.tony.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 实现服务提供
 * 
 * @author Tony
 *
 */
public class DubboStarter {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "provider.xml" });
		context.start();
		System.in.read(); //等待输入阻塞
		context.close();
	}
}
