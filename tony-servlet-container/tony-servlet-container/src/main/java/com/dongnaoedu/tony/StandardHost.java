package com.dongnaoedu.tony;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 虚拟主机和域名相对应
 * 
 * @author Tony
 *
 */
public class StandardHost {

	/** 不同项目对应的配置信息 */
	private final Map<Object, StandardContext> standardContexts = new HashMap<>();

	private String appBase;

	private String name;

	/**
	 * 其实这里我们没做域名判断
	 * 
	 * @param name
	 *            域名
	 * @param appBase
	 *            项目部署目录
	 */
	public StandardHost(String name, String appBase) {
		this.name = name;
		this.appBase = appBase;
	}

	public void start() throws Exception {
		List<String> projects = new ArrayList<>();
		// 1. 查找这个主机下面有多少项目
		// 把部署目录下的每个文件夹都当成一个项目
		File file = new File(appBase);
		File[] listFiles = file.listFiles();
		for (File project : listFiles) {
			if (project.isDirectory()) {
				System.out.println("发现项目：" + project.getName());
				projects.add(project.getName());
			}
		}

		// 2. 为每一个项目创建一个Context对象，存储项目里面的内容(如配置信息，web.xml，servlet，filter....)
		for (String project : projects) {
			StandardContext standardContext = new StandardContext(appBase + "/" + project);
			standardContext.start();
			getStandardContexts().put(project, standardContext);
		}

	}

	public String getAppBase() {
		return appBase;
	}

	public void setAppBase(String appBase) {
		this.appBase = appBase;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Object, StandardContext> getStandardContexts() {
		return standardContexts;
	}

}
