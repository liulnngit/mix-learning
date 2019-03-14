package com.dongnaoedu.tony;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dongnaoedu.tony.service.DemoService;

public class Consumer {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "consumer.xml" });
		context.start();
		// obtain proxy object for remote invocation
		DemoService demoService = (DemoService) context.getBean("demoService");
		// execute remote invocation
		String hello = demoService.getUserNameById("123");
		// show the result
		System.out.println("远程调用####>>>>>" + hello);

	}
}