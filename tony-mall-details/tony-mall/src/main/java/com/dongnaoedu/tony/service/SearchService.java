package com.dongnaoedu.tony.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 调用搜索服务
	 * 
	 * @param q
	 *            关键字
	 * @return
	 */
	public List<Map<String, Object>> search(String q) {
		String sql = "select * from tb_goods_info ";
		if (q != null) {
			sql += "where goods_title like '%" + q + "%'";
		}
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return result;
	}

	/**
	 * 调用商品查询服务，获取商品更多信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @return
	 */
	public Map<String, Object> queryById(String goodsId) {
		Map<String, Object> result = jdbcTemplate
				.queryForMap("select * from tb_goods_info where goods_id = '" + goodsId + "'");
		return result;
	}
}
