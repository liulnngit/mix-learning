package com.tony.test;

import java.io.IOException;
import java.net.Socket;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * socket处理类
 * 
 * @author Tony
 *
 */
class SocketProcessor implements Runnable {

	Socket socket;

	public SocketProcessor(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// 接收请求数据
			byte[] requestBody = new byte[1024];
			socket.getInputStream().read(requestBody);
			String requestString = new String(requestBody);
			System.out.println("收到http请求，内容如下：");
			System.out.println(requestString);

			// 自己从socket中提取信息，封装成request，response对象
			HttpServletRequest request = RequestFactory.createRequest(requestBody);
			HttpServletResponse response = ResponseFactory.createResponse(socket);

			// 分析http请求的项目路径，得到请求对应的项目
			String project = request.getContextPath().split("/")[1];

			// TODO 根据请求的路径，匹配servlet
			// 此处仅仅做了简单的url匹配
			// 获取路径相匹配的servlet名称
			// 1. 判断顶级的路径有没有被配置映射，斜杠最大
			String servletName = BootStraper.projectConfigBeans.get(project).servletMapping.get("/");
			if (servletName == null) {
				// 2. 如果没有配置"/",则根据请求路径去百分百匹配
				servletName = BootStraper.projectConfigBeans.get(project).servletMapping.get(request.getServletPath());
			}

			// 3. 如果还没找到，404
			if (servletName == null) {
				System.out.println("404....");
				response.getWriter().println("404");
				return;
			}

			// 根据servlet名称，获取对应的servlet实例
			Servlet servlet = (Servlet) BootStraper.projectConfigBeans.get(project).servletInstances.get(servletName);

			// 调用servlet实例的service方法继续处理这个请求
			// 这里往后就是业务系统的处理范畴了！！
			servlet.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 最后关闭socket
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}