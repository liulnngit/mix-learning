package com.dongnaoedu.tony.test;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;

public class RedisTests {
	public static void main(String[] args) {
		Jedis jedis = new Jedis();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", "tony");
		jsonObject.put("address", "changsha");

		jedis.set("tony", jsonObject.toJSONString());
	}
}
