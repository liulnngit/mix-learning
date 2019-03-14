package com.dongnaoedu.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 启动入口
 * @author Tony
 *
 */
@SpringBootApplication
@EnableEurekaServer // 启用Eureka服务端
public class EurekaApp {
	public static void main(String[] args) {
		new SpringApplicationBuilder(EurekaApp.class).web(true).run(args);
	}
}


