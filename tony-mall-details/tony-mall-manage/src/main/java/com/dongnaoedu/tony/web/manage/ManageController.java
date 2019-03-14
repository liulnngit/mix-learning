package com.dongnaoedu.tony.web.manage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.dongnaoedu.tony.service.ManageService;

@Controller
public class ManageController {
	@Autowired
	ManageService demoService;

	/** 跳转到商品管理后台 */
	@GetMapping("/manage")
	public String manage(ModelMap model) {
		// 展示所有商品
		List<Map<String, Object>> goodsList = demoService.search(null);
		model.put("datas", goodsList);
		return "manage/list";
	}
}
