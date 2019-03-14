package com.tony.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// 服务消费者
@SpringBootApplication
@EnableEurekaClient
public class WebsiteApp {
	public static void main(String[] args) {
		// 运行起来一个 web应用
		new SpringApplicationBuilder(WebsiteApp.class).web(true).run(args);
	}
}
