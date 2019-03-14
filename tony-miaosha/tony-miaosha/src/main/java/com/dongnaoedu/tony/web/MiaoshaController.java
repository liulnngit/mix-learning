package com.dongnaoedu.tony.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.tony.service.MiaoshaService;

@RestController
public class MiaoshaController {

	@Autowired
	MiaoshaService miaoshaService;

	// 秒杀接口
	@RequestMapping("/miaosha")
	public Object getUserInfo(String goodsCode, String userId) throws Exception {
		// 调用service方法
		return miaoshaService.miaosha(goodsCode, userId);
	}
}
