package com.dongnaoedu.tony;

/**
 * Tomcat给servlet容器的命名
 * @author 动脑学院.Tony老师
 *
 */
public class Catalina {
	public void start() throws Exception {
		new StandardServer().start();
	}
}
