package com.dongnaoedu.tony;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LoadBalanceApp {

	// 程序入口
	public static void main(String[] args) {
		new SpringApplicationBuilder(LoadBalanceApp.class).web(WebApplicationType.SERVLET).run(args);
	}

	/** 时间查询接口 */
	@RequestMapping("/time")
	public String index(HttpServletRequest request) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.now();
		String time = ldt.format(dtf);
		return "当前时间：" + time + "<br/>" + "由该服务器处理此次请求：" + request.getLocalAddr() + ":" + request.getLocalPort();
	}
}
