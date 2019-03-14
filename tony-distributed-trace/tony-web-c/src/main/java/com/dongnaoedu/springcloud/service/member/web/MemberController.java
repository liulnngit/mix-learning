package com.dongnaoedu.springcloud.service.member.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// 会员管理中心
@RestController
public class MemberController {

	@Autowired
	RestTemplate restTemplate;

	// 充值接口
	@RequestMapping(value = "/index", produces = "text/html;charset=UTF-8")
	public String recharge(HttpServletRequest request) {
		StringBuffer result = new StringBuffer();
		result.append(">>>>>>>> tony-web-c");
		return result.toString();
	}
}
