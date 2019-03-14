package com.dongnaoedu.tony.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ManageService {

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

	/**
	 * 增加商品
	 * 
	 * @param map
	 *            商品信息
	 * @return 执行结果
	 */
	public boolean add(HashMap<String, String> map) {
		String sql = "INSERT INTO `tb_goods_info` ";
		sql += "(`goods_id`, `goods_title`, `goods_desc`, `goods_comment`, `goods_price`) VALUES  (?,?,?,?,?)";

		int update = jdbcTemplate.update(sql, map.get("goods_id"), map.get("goods_title"), map.get("goods_desc"),
				map.get("goods_comment"), map.get("goods_price"));
		return update == 1;
	}

	/**
	 * 增加商品
	 * 
	 * @param map
	 *            商品信息
	 * @return 执行结果
	 */
	public boolean delete(HashMap<String, String> map) {
		String sql = "delete from tb_goods_info where goods_id=?";

		int update = jdbcTemplate.update(sql, map.get("goods_id"));
		return update == 1;
	}

	/**
	 * 修改商品信息
	 * 
	 * @param map
	 *            商品信息
	 * @return 执行结果
	 */
	public boolean update(HashMap<String, String> map) {
		String sql = "update tb_goods_info set ";
		sql += "goods_title=?,goods_desc=?,goods_comment=?,goods_price=? where goods_id=?";

		int update = jdbcTemplate.update(sql, map.get("goods_title"), map.get("goods_desc"), map.get("goods_comment"),
				map.get("goods_price"), map.get("goods_id"));
		return update == 1;
	}
}
