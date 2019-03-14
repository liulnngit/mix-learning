package com.dongnaoedu.tony.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 * socket到request的封装
 * 
 * @author 动脑学院.Tony老师
 *
 */
public class RequestFactory {
	/**
	 * 构建出request对象
	 * 
	 * @param socket
	 * @return
	 */
	public static HttpServletRequest createRequest(byte[] requestBody) {
		// TODO 实现HttpServletRequest接口并构建一个我们必用的request对象
		// 实际就是根据http协议对请求的报文进行解析，取出来 请求路径，编码等等信息
		return new HttpServletRequest() {

			@Override
			public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public AsyncContext startAsync() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
				// Auto-generated method stub

			}

			@Override
			public void setAttribute(String name, Object o) {
				// Auto-generated method stub

			}

			@Override
			public void removeAttribute(String name) {
				// Auto-generated method stub

			}

			@Override
			public boolean isSecure() {
				// Auto-generated method stub
				return false;
			}

			@Override
			public boolean isAsyncSupported() {
				// Auto-generated method stub
				return false;
			}

			@Override
			public boolean isAsyncStarted() {
				// Auto-generated method stub
				return false;
			}

			@Override
			public ServletContext getServletContext() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public int getServerPort() {
				// Auto-generated method stub
				return 0;
			}

			@Override
			public String getServerName() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getScheme() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String path) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public int getRemotePort() {
				// Auto-generated method stub
				return 0;
			}

			@Override
			public String getRemoteHost() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getRemoteAddr() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getRealPath(String path) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public BufferedReader getReader() throws IOException {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getProtocol() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String[] getParameterValues(String name) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Enumeration<String> getParameterNames() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, String[]> getParameterMap() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getParameter(String name) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Enumeration<Locale> getLocales() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Locale getLocale() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public int getLocalPort() {
				// Auto-generated method stub
				return 0;
			}

			@Override
			public String getLocalName() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getLocalAddr() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public ServletInputStream getInputStream() throws IOException {
				// Auto-generated method stub
				return null;
			}

			@Override
			public DispatcherType getDispatcherType() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getContentType() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public int getContentLength() {
				// Auto-generated method stub
				return 0;
			}

			@Override
			public String getCharacterEncoding() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Enumeration<String> getAttributeNames() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Object getAttribute(String name) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public AsyncContext getAsyncContext() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public void logout() throws ServletException {
				// Auto-generated method stub

			}

			@Override
			public void login(String username, String password) throws ServletException {
				// Auto-generated method stub

			}

			@Override
			public boolean isUserInRole(String role) {
				// Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRequestedSessionIdValid() {
				// Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromUrl() {
				// Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromURL() {
				// Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromCookie() {
				// Auto-generated method stub
				return false;
			}

			@Override
			public Principal getUserPrincipal() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public HttpSession getSession(boolean create) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public HttpSession getSession() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getServletPath() {
				// TODO 获取请求路径
				// 简单点就是文本解析，每一行都是一部分数据
				// http请求路径和请求方法method就是在第一行，所以我们做个简单的处理就能拿到路径
				String requestString = new String(requestBody);
				String uri = requestString.split("\r\n")[0].split(" ")[1];

				String project = uri.split("/")[1];

				String servletPath = uri.replace("/" + project, "");
				return servletPath;
			}

			@Override
			public String getRequestedSessionId() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public StringBuffer getRequestURL() {
				// Auto-generated method stub

				return null;
			}

			/** 获取请求路径 */
			@Override
			public String getRequestURI() {
				// TODO 获取请求路径
				// 简单点就是文本解析，每一行都是一部分数据
				// 请求路径和请求方法method就是在第一行，所以我们做个简单的处理就能拿到路径

				String requestString = new String(requestBody);
				String uri = requestString.split("\r\n")[0].split(" ")[1];
				return uri;
			}

			@Override
			public String getRemoteUser() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getQueryString() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getPathTranslated() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getPathInfo() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Collection<Part> getParts() throws IOException, IllegalStateException, ServletException {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Part getPart(String name) throws IOException, IllegalStateException, ServletException {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getMethod() {
				// TODO 获取请求方法
				// 简单点就是文本解析，每一行都是一部分数据
				// 请求路径和请求方法method就是在第一行，所以我们做个简单的处理就能拿到路径
				String requestString = new String(requestBody);
				String method = requestString.split("\r\n")[0].split(" ")[0];
				return method;
			}

			@Override
			public int getIntHeader(String name) {
				// Auto-generated method stub
				return 0;
			}

			@Override
			public Enumeration<String> getHeaders(String name) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Enumeration<String> getHeaderNames() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getHeader(String name) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public long getDateHeader(String name) {
				// Auto-generated method stub
				return 0;
			}

			@Override
			public Cookie[] getCookies() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getContextPath() {
				// TODO 获取请求项目上下文信息
				// 根据路径来分析，第一个斜杠处就是对应我们的项目

				String requestString = new String(requestBody);
				String uri = requestString.split("\r\n")[0].split(" ")[1];

				String project = uri.split("/")[1];
				return "/" + project;
			}

			@Override
			public String getAuthType() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
				// Auto-generated method stub
				return false;
			}

			@Override
			public long getContentLengthLong() {
				return 0;
			}

			@Override
			public String changeSessionId() {
				return null;
			}

			@Override
			public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass)
					throws IOException, ServletException {
				return null;
			}
		};
	}
}
