package com.dongnaoedu.tony;

/**
 * 数据库连接池 <br/>
 * 1. 初始化 <br/>
 * 2. 从池中取出一个连接 <br/>
 * 3. 放回连接池 <br/>
 * 4. 释放空闲连接
 * 
 * @author Tony
 *
 */
public interface TonyPoolInterface {

	/**
	 * 初始化
	 * 
	 * @param max
	 *            最大连接数
	 * @param maxWait
	 *            获取连接最长等待时间
	 * @param idleCount
	 *            空闲数量
	 */
	public void init(int max, long maxWait, int idleCount);

	/**
	 * 获取连接
	 * 
	 * @return 连接
	 * @throws Exception
	 */
	public TonyJdbcConnect getResource() throws Exception;

	/**
	 * 返回连接
	 * 
	 * @param 数据库
	 *            连接对象
	 */
	public void returnConnect(TonyJdbcConnect connect);

}
