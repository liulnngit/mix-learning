package com.dongnaoedu.tony.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;

import com.dongnaoedu.tony.service.SearchService;

/** 商品展示 */
@Controller
public class ExplorerController {

	@Autowired
	SearchService demoService;

	/** search接口 */
	@GetMapping(value = { "/search", "/" })
	public String search(String q, ModelMap model) throws Exception {
		// 根据搜索条件进行查询, 调用接口查询商品信息
		List<Map<String, Object>> goodsList = demoService.search(q);
		model.put("datas", goodsList);
		return "search";
	}

	/** 商品详情页面渲染接口 */
	@GetMapping("/item-{goodsId}.html")
	public String item(@PathVariable("goodsId") String goodsId, ModelMap model, WebRequest webRequest)
			throws Exception {
		// 调用商品查询服务，查出商品信息，渲染到页面
		Map<String, Object> goodsInfo = demoService.queryById(goodsId);
		model.put("goodsInfo", goodsInfo);
		return "item";
	}

}
