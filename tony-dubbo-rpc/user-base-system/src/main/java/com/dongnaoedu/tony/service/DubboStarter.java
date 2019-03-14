package com.dongnaoedu.tony.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动服务
 * @author Tony
 *
 */
public class DubboStarter {
	public static void main(String[] args) throws Exception {
		String[] s = new String[1];
		System.out.println(s);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "provider.xml" });
		context.start();

		System.in.read();
		context.close();
	}
}
