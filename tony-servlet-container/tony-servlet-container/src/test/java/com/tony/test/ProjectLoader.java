package com.tony.test;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

/**
 * 类加载工具
 * <p>
 * 每个项目都单独的去加载它们相关的类和第三方jar包
 * </p>
 * 
 * @author Tony
 *
 */
public class ProjectLoader {

	private String project;

	// 创建类加载器
	URLClassLoader loader = null;

	public ProjectLoader(String project) throws Exception {
		this.project = project;

		// class文件和第三方jar包的存储路径
		ArrayList<URL> urls = new ArrayList<>();
		// lib
		File libs = new File(BootStraper.work_space + "\\" + getProject() + "\\WEB-INF\\lib");
		if (libs.exists()) {
			// 遍历lib包目录，添加到URL中
			for (String lib : libs.list()) {
				urls.add(new URL("file:" + BootStraper.work_space + "\\" + getProject() + "\\WEB-INF\\lib\\" + lib));
			}
		}
		// classes
		urls.add(new URL("file:" + BootStraper.work_space + "\\" + getProject() + "\\WEB-INF\\classes\\"));

		// 为项目创建一个类加载器，加载项目的class和jar包
		this.loader = new URLClassLoader(urls.toArray(new URL[] {}), ProjectLoader.class.getClassLoader());
	}

	/**
	 * 加载并初始化servlet
	 * 
	 * @return
	 * @throws Exception
	 */
	public ProjectLoader load() throws Exception {
		// 设置后续类加载所有用的类加载器为刚刚创建的那个，后面初始化servlet需要用到的
		Thread.currentThread().setContextClassLoader(this.loader);

		// 获取项目下配置的所有servlet
		ProjectConfigBean configBean = (ProjectConfigBean) BootStraper.projectConfigBeans.get(project);
		Collection<Entry<String, Object>> servlets = configBean.servlets.entrySet();
		// 遍历
		for (Entry<?, ?> entry : servlets) {
			// 类加载
			System.out.println(entry.getValue().toString());
			Class<?> clazz = loader.loadClass(entry.getValue().toString());
			// 实例化servlet
			Servlet servlet = (Servlet) clazz.newInstance();
			// 将实例化的对象，保存下来
			configBean.servletInstances.put(entry.getKey().toString(), servlet);
			// 初始化
			servlet.init(new ServletConfig() {

				@Override
				public String getServletName() {
					return entry.getKey().toString();
				}

				@Override
				public ServletContext getServletContext() {
					return new ServletContext() {

						@Override
						public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
								throws IllegalStateException, IllegalArgumentException {

						}

						@Override
						public boolean setInitParameter(String name, String value) {
							return false;
						}

						@Override
						public void setAttribute(String name, Object object) {

						}

						@Override
						public void removeAttribute(String name) {

						}

						@Override
						public void log(String message, Throwable throwable) {

						}

						@Override
						public void log(Exception exception, String msg) {

						}

						@Override
						public void log(String msg) {
							// TODO 自己实现，日志输出
							System.out.println("tony server log: >>" + msg);
						}

						@Override
						public SessionCookieConfig getSessionCookieConfig() {
							return null;
						}

						@Override
						public Enumeration<Servlet> getServlets() {
							return null;
						}

						@Override
						public Map<String, ? extends ServletRegistration> getServletRegistrations() {
							return null;
						}

						@Override
						public ServletRegistration getServletRegistration(String servletName) {
							return null;
						}

						@Override
						public Enumeration<String> getServletNames() {
							return null;
						}

						@Override
						public String getServletContextName() {
							return null;
						}

						@Override
						public Servlet getServlet(String name) throws ServletException {
							return null;
						}

						@Override
						public String getServerInfo() {
							return null;
						}

						@Override
						public Set<String> getResourcePaths(String path) {
							return null;
						}

						@Override
						public InputStream getResourceAsStream(String path) {
							return null;
						}

						@Override
						public URL getResource(String path) throws MalformedURLException {
							return null;
						}

						@Override
						public RequestDispatcher getRequestDispatcher(String path) {
							return null;
						}

						@Override
						public String getRealPath(String path) {
							return null;
						}

						@Override
						public RequestDispatcher getNamedDispatcher(String name) {
							return null;
						}

						@Override
						public int getMinorVersion() {
							return 0;
						}

						@Override
						public String getMimeType(String file) {
							return null;
						}

						@Override
						public int getMajorVersion() {
							return 0;
						}

						@Override
						public JspConfigDescriptor getJspConfigDescriptor() {
							return null;
						}

						@Override
						public Enumeration<String> getInitParameterNames() {
							// TODO 自己实现,返回servlet的参数名称
							Vector<String> parameterNames = new Vector<>();
							parameterNames.addAll(configBean.servletParam.get(entry.getKey().toString()).keySet());

							return parameterNames.elements();
						}

						@Override
						public String getInitParameter(String name) {
							// TODO 自己实现,根据参数名，返回参数值
							return configBean.servletParam.get(entry.getKey().toString()).get(name);
						}

						@Override
						public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
							return null;
						}

						@Override
						public FilterRegistration getFilterRegistration(String filterName) {
							return null;
						}

						@Override
						public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
							return null;
						}

						@Override
						public int getEffectiveMinorVersion() {
							return 0;
						}

						@Override
						public int getEffectiveMajorVersion() {
							return 0;
						}

						@Override
						public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
							return null;
						}

						@Override
						public String getContextPath() {
							return null;
						}

						@Override
						public ServletContext getContext(String uripath) {
							return null;
						}

						@Override
						public ClassLoader getClassLoader() {
							return null;
						}

						@Override
						public Enumeration<String> getAttributeNames() {
							// TODO 自己实现,返回servlet配置的的属性名称
							Vector<String> attributeNames = new Vector<>();
							return attributeNames.elements();
						}

						@Override
						public Object getAttribute(String name) {
							return null;
						}

						@Override
						public void declareRoles(String... roleNames) {

						}

						@Override
						public <T extends Servlet> T createServlet(Class<T> c) throws ServletException {
							return null;
						}

						@Override
						public <T extends EventListener> T createListener(Class<T> c) throws ServletException {
							return null;
						}

						@Override
						public <T extends Filter> T createFilter(Class<T> c) throws ServletException {
							return null;
						}

						@Override
						public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName,
								Class<? extends Servlet> servletClass) {
							return null;
						}

						@Override
						public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName,
								Servlet servlet) {
							return null;
						}

						@Override
						public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName,
								String className) {
							return null;
						}

						@Override
						public <T extends EventListener> void addListener(T t) {

						}

						@Override
						public void addListener(String className) {

						}

						@Override
						public void addListener(Class<? extends EventListener> listenerClass) {

						}

						@Override
						public Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
							return null;
						}

						@Override
						public Dynamic addFilter(String filterName, Filter filter) {
							return null;
						}

						@Override
						public Dynamic addFilter(String filterName, String className) {
							return null;
						}

						@Override
						public String getVirtualServerName() {
							return null;
						}
					};
				}

				@Override
				public Enumeration<String> getInitParameterNames() {
					// TODO 获取servlet所有参数的名称
					Vector<String> parameterNames = new Vector<>();
					parameterNames.addAll(configBean.servletParam.get(entry.getKey().toString()).keySet());

					return parameterNames.elements();
				}

				@Override
				public String getInitParameter(String name) {
					// TODO 根据servlet参数名称，获取对应的值
					return configBean.servletParam.get(entry.getKey().toString()).get(name);
				}
			});

		}

		System.out.println("******************项目" + this.getProject() + "中的servlet加载并初始化完毕********************");
		return this;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public URLClassLoader getLoader() {
		return loader;
	}

	public void setLoader(URLClassLoader loader) {
		this.loader = loader;
	}

}
