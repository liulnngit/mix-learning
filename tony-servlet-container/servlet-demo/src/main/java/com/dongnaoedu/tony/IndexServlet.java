package com.dongnaoedu.tony;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 5631739327007081912L;

	@Override
	public void init() throws ServletException {
		System.out.println("servlet-demo IndexServlet 初始化");
	}

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse resp)
			throws ServletException, IOException {
		// 写出去
		resp.getOutputStream().write("hello,this is 'servlet-demo', index...".getBytes());
		resp.getOutputStream().flush();
		
		// resp.getWriter().print("");
	}

}