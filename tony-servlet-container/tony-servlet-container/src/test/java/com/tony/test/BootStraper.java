package com.tony.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * servlet容器启动类
 * 
 * @author Tony
 *
 */
public class BootStraper {
	/** 工作空间 - 也就是war包的发布目录 */
	public static final String work_space = "C:\\u01\\tony-server-9999";

	/** 不同项目对应的配置信息 */
	public static final Map<Object, ProjectConfigBean> projectConfigBeans = new HashMap<>();

	public static void main(String[] args) throws Exception {
		// 1. 检查目录下是否有项目部署;
		Set<String> projects = ProjectChecker.check(work_space);

		// 2. 解析项目的web.xml
		for (String project : projects) {
			ProjectConfigBean projectConfigBean = new ProjectConfigBean(project).loadXml();
			projectConfigBeans.put(project, projectConfigBean);
		}

		// 3. 加载和初始化项目中的servlet
		for (String project : projects) {
			new ProjectLoader(project).load();
		}

		// 4. 启动web服务
		WebServerStarter.start();
	}

}
