package com.dongnaoedu.tony.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.tony.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	// 获取用户信息
	@RequestMapping("/getUserInfo")
	public Object getUserInfo(String userId) throws Exception {
		// 调用service方法获取
		return userService.getUserInfo(userId);
	}
}
