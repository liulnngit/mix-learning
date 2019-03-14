package com.dongnaoedu.tony.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.codahale.metrics.annotation.Timed;

@Service
public class HttpService {

	private final Logger logger = Logger.getLogger(HttpService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Timed // 性能损耗低 ，计算全在内存,没有大数据计算， 结果输出是异步
	public String queryForRest(String userId) {
		// 调用http接口
		return restTemplate.getForObject("http://www.tony.com/userinfo/get?userId=" + userId, String.class);
	}
}
