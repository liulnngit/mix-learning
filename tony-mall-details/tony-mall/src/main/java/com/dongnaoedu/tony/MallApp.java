package com.dongnaoedu.tony;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
public class MallApp {
	public static void main(String[] args) {
		new SpringApplicationBuilder(MallApp.class).web(WebApplicationType.SERVLET).run(args);
	}
	
	/** 增加一个filter，实现浏览器缓存(http status 304的原理) */
	@Bean
	public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
}
