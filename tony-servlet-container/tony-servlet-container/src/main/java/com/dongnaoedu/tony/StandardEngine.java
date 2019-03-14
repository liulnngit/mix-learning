package com.dongnaoedu.tony;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet引擎（示例代码，仅创建一个host）
 * 
 * @author 动脑学院.Tony老师
 *
 */
public class StandardEngine {
	public static StandardHost standardHost;

	public static void start() throws Exception {
		// 示例：创建一个host，和localhost域名对应，项目部署在/dongnao-tony-server/webapps目录下面
		standardHost = new StandardHost("localhost", "/dongnao-tony-server/webapps");
		standardHost.start();
	}
	

	/**
	 * 调度，处理请求
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void dispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 忽略了域名的匹配
		
		
		// 分析http请求的项目路径，得到请求对应的项目
		String project = request.getContextPath().split("/")[1];

		// TODO 根据请求的路径，匹配servlet
		// 此处仅仅做了简单的url匹配
		// 获取路径相匹配的servlet名称
		// 1. 判断顶级的路径有没有被配置映射，斜杠最大
		String servletName = standardHost.getStandardContexts().get(project).servletMapping.get("/");
		if (servletName == null) {
			// 2. 如果没有配置"/",则根据请求路径去百分百匹配
			servletName = standardHost.getStandardContexts().get(project).servletMapping
					.get(request.getServletPath());
		}

		// 3. 如果还没找到，404
		if (servletName == null) {
			System.out.println("随便抛个错误吧");
			return;
		}

		// 根据servlet名称，获取对应的servlet实例
		Servlet servlet = (Servlet) standardHost.getStandardContexts().get(project).servletInstances
				.get(servletName);

		// 调用servlet实例的service方法继续处理这个请求
		// 这里往后就是业务系统的处理范畴了！！
		servlet.service(request, response);
	}
}
