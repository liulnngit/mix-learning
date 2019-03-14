package com.dongnaoedu.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// 短信系统
@SpringBootApplication
@EnableEurekaClient // 我需要用到eureka
public class SmsInterfaceApp {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(SmsInterfaceApp.class).web(true).run(args);
	}
}
