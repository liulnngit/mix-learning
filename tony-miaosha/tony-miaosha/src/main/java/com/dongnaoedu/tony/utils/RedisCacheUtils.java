package com.dongnaoedu.tony.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis缓存工具类
 * 
 * @author Tony
 *
 */
public class RedisCacheUtils {

	private JedisPool pool;

	/**
	 * 建立连接池 真实环境，一般把配置参数缺抽取出来。
	 * 
	 */
	public RedisCacheUtils(String host, int port) {
		// 建立连接池配置参数
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxTotal(10000);
		// 设置最大阻塞时间，记住是毫秒数milliseconds
		config.setMaxWaitMillis(10000);
		// 设置空间连接
		config.setMaxIdle(100);
		// 创建连接池
		pool = new JedisPool(config, host, port);
	}

	/**
	 * 获取一个jedis 对象
	 * 
	 * @return
	 */
	public Jedis getJedis() {
		return pool.getResource();
	}
}