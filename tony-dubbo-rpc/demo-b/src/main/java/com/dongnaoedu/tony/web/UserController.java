package com.dongnaoedu.tony.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.tony.service.UserQueryService;

@RestController
public class UserController {

	@Autowired
	UserQueryService userQueryService;

	@GetMapping(value = "/getUserName")
	public String findUserName(String userId) {
		String userName = userQueryService.getUserNameById(userId);
		System.out.println("B服务调用远程服务成功！");
		
		return "hello " + userName;
	}
}