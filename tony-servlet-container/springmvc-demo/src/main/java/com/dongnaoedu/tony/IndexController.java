package com.dongnaoedu.tony;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	@GetMapping(value = "/index")
	public String index(HttpServletRequest httpServletRequest) {
		System.out.println("springMvcDemo 进行Request请求");
		System.out.println("RequestURI: " + httpServletRequest.getRequestURI());
		System.out.println("ContextPath: " + httpServletRequest.getContextPath());
		System.out.println("ServletPath: " + httpServletRequest.getServletPath());
		System.out.println("Method: " + httpServletRequest.getMethod());
		return "index....";
	}

	@GetMapping(value = "/hello")
	public String hello(HttpServletRequest httpServletRequest) {
		return "hello, Tony....!";
	}
}