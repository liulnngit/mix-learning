package com.dongnaoedu.tony.web.manage;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.dongnaoedu.tony.service.ManageService;

/**
 * 新增商品
 * 
 * @author Tony
 *
 */
@Controller
@RequestMapping("manage")
public class AddController {
	@Autowired
	ManageService demoService;

	/** 跳转到新增页面 */
	@GetMapping("/add")
	public String add() {
		return "manage/add";
	}

	/** 新增商品 */
	@PostMapping("add")
	public String doAdd(@RequestParam HashMap<String, String> params, WebRequest webRequest) {
		params.put("goods_id", UUID.randomUUID().toString());
		demoService.add(params);
		return "redirect:/manage";
	}
}
