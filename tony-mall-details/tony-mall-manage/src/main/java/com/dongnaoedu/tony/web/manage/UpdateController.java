package com.dongnaoedu.tony.web.manage;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import com.dongnaoedu.tony.service.ManageService;

@Controller
@RequestMapping("manage")
public class UpdateController {
	@Autowired
	ManageService demoService;

	/** 跳转修改商品页面 */
	@GetMapping("/update-{goodsId}")
	public String update(@PathVariable("goodsId") String goodsId, ModelMap model, WebRequest webRequest) {
		// 调用商品查询服务，查出商品信息，渲染到页面
		Map<String, Object> goodsInfo = demoService.queryById(goodsId);
		model.put("goodsInfo", goodsInfo);
		return "/manage/update";
	}

	/** 修改商品 */
	@PostMapping("/update")
	public String doUpdate(@RequestParam HashMap<String, String> params, WebRequest webRequest) {
		demoService.update(params);
		
		// 通过某种方式触发缓存更新
		String response = new RestTemplate().getForObject("http://mall.rebot.xyz/purge/item-1.html", String.class);
		System.out.println("触发缓存更新，取得结果"); 
		System.out.println(response);
		return "redirect:/manage";
	}
}
