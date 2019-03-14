package com.dongnaoedu.tony.web.manage;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.dongnaoedu.tony.service.ManageService;

/**
 * 删除商品
 * 
 * @author Tony
 *
 */
@Controller
@RequestMapping("manage")
public class DeleteController {
	@Autowired
	ManageService demoService;

	/** 删除商品 */
	@PostMapping("/delete")
	public String doAdd(@RequestParam HashMap<String, String> params, WebRequest webRequest) {
		demoService.delete(params);
		return "redirect:/manage";
	}
}
