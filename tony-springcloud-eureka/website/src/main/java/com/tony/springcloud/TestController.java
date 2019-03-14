package com.tony.springcloud;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 就是普通的controller，这就是为什么说只要你会SpringMVC，就可以快速上手SpringCloud
 * 
 * @author zhang
 *
 */
@RestController
public class TestController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	DiscoveryClient discoveryClient;// spring cloud 提供的客户端工具

	@RequestMapping("test")
	public String test() throws IOException {
		// eureka client后台线程已经缓存了所有 系统名称以及所部署的服务器信息
		// 通过eureka client 获取接口系统的实例信息
		// 也就是说去看 部署了哪些机器，以及IP 端口等信息
		List<ServiceInstance> instances = discoveryClient.getInstances("lession-1-sms-interface");

		for (ServiceInstance serviceInstance : instances) {
			System.out.println("获取到服务实例：" + serviceInstance.getPort());
		}
		// 随机挑选一个服务器去访问
		int index = new Random().nextInt(instances.size());
		ServiceInstance serviceInstance = instances.get(index);

		// 获取的IP 端口 信息
		String host = serviceInstance.getHost();
		int port = serviceInstance.getPort();

		// 调用接口
		String result = new RestTemplate().getForObject("http://" + host + ":" + port + "/sms/1", String.class);

		return result;
	}

	/** springcloud快速上手的示例 */
	@RequestMapping("fast-test")
	public String restTemplateTest() {
		// 调用接口
		// 通过服务名即可调用，这就是SpringCloud对微服务的高度集成，快速上手，开箱即用
		// httpclient: http://localhost:9003/sms/2  
		String result = restTemplate.getForObject("http://lession-1-sms-interface/sms/1", String.class);
		return result;
	}
}
