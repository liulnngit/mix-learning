package com.tony.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SmsInterfaceApp {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(SmsInterfaceApp.class).web(true).run(args);
	}
}
