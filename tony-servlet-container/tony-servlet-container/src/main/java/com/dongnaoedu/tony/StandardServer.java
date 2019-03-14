package com.dongnaoedu.tony;

/**
 * server
 * 
 * @author 动脑学院.Tony老师
 *
 */
public class StandardServer {
	public void start() throws Exception {
		// 示例，启动单个service
		new StandardService().start();
	}
}
