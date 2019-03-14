package com.tony.springcloud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

	@RequestMapping(path = "/userinfo-api/get", produces = "application/json; charset=UTF-8")
	public String getInfo(String userId) throws InterruptedException {
		Thread.sleep(2000L);
		return "{\"userId\":\"" + userId + "\",\"address\":\"Changsha\",\"age\":19,\"userName\":\"张峰\"}";
	}

	@RequestMapping(path = "/integral-api/get", produces = "application/json; charset=UTF-8")
	public String getIntegral(String userId) throws InterruptedException {
		Thread.sleep(3000L);
		return "{\"userId\":\"" + userId + "\",\"intergral\":99}";
	}
}