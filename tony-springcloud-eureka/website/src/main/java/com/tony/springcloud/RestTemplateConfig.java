package com.tony.springcloud;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // 注解配置
public class RestTemplateConfig {

	/** 创建一个 RestTemplate工具实例 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		// Spring提供的http请求工具类
		return new RestTemplate();
	}
}
