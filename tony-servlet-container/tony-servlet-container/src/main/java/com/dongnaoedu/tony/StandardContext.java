package com.dongnaoedu.tony;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 本示例中，仅做了servlet，没有去做filter等其他功能
 * 
 * @author 动脑学院.Tony
 *
 */
public class StandardContext extends DefaultHandler {

	/** Servlet 集合 */
	Map<String, Object> servlets = new HashMap<>();

	/** Servlet 实例对象集合 */
	Map<String, Object> servletInstances = new HashMap<>();

	/** Servlet 参数 */
	Map<String, Map<String, String>> servletParam = new HashMap<>();

	/** Servlet 映射 */
	Map<String, String> servletMapping = new HashMap<>();

	private String projectPath;

	/**
	 * 传入项目路径，即可找到web.xml进行解析
	 * 
	 * @param projectPath
	 *            项目绝对路径
	 */
	public StandardContext(String projectPath) {
		this.setProjectPath(projectPath);
	}

	public void start() throws Exception {

		// 1. 加载解析web.xml文件
		loadXml(projectPath + "/WEB-INF/web.xml");
		// 2. 加载和初始化servlet
		loadServlet();
	}

	/**
	 * 读取web.xml文件中的信息， 比如(context_param,listener,filter,servlet)
	 * <p>
	 * 这个示例中仅读取servlet信息
	 * </p>
	 * 
	 * @param xmlPath
	 *            xml文件路径 @throws Exception @throws
	 */
	public void loadXml(String xmlPath) throws Exception {
		// 创建一个解析XML的工厂对象
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		// 创建一个解析XML的对象
		SAXParser parser = parserFactory.newSAXParser();
		// 创建一个解析助手类
		parser.parse(xmlPath, this);

		System.out.println("******************" + xmlPath + " 加载完毕********************");
	}

	public void loadServlet() throws Exception {
		// TODO 这里需要用到类加载器进行加载
		
		// 指定class文件和第三方jar包的存储路径
		ArrayList<URL> urls = new ArrayList<>();
		// lib
		File libs = new File(projectPath + "\\WEB-INF\\lib");
		if (libs.exists()) {
			// 遍历lib包目录，添加到URL中
			for (String lib : libs.list()) {
				urls.add(new URL("file:" + projectPath + "\\WEB-INF\\lib\\" + lib));
			}
		}
		// classes
		urls.add(new URL("file:" + projectPath + "\\WEB-INF\\classes\\"));

		// 为项目创建一个类加载器，加载项目的class和jar包
		URLClassLoader servletClassLoader = new URLClassLoader(urls.toArray(new URL[] {}), this.getClass().getClassLoader());
		// 设置后续类加载所有用的类加载器为刚刚创建的那个，后面初始化servlet需要用到的
		Thread.currentThread().setContextClassLoader(servletClassLoader);

		// 获取项目下配置的所有servlet
		Collection<Entry<String, Object>> servlets = this.servlets.entrySet();
		// 遍历
		for (Entry<?, ?> entry : servlets) {
			// 类加载
			System.out.println(entry.getValue().toString());
			Class<?> clazz = servletClassLoader.loadClass(entry.getValue().toString());
			// 实例化servlet
			Servlet servlet = (Servlet) clazz.newInstance();
			// 将实例化的对象，保存下来
			this.servletInstances.put(entry.getKey().toString(), servlet);
			// 初始化
			servlet.init(new ServletConfig() {
				
				@Override
				public String getServletName() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public ServletContext getServletContext() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Enumeration<String> getInitParameterNames() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public String getInitParameter(String name) {
					// TODO Auto-generated method stub
					return null;
				}
			});

		}

		System.out.println("******************" + projectPath + "中的servlet加载并初始化完毕********************");

	}

	String currentServlet = null;
	String currentServletMapping = null;
	String currentParam = null;
	String qName = null;

	// 开始解析文档，即开始解析XML根元素时调用该方法
	@Override
	public void startDocument() throws SAXException {
	}

	// 开始解析每个元素时都会调用该方法
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// 判断正在解析的元素是不是开始解析的元素
		this.qName = qName;
	}

	// 解析到每个元素的内容时会调用此方法
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String currentValue = new String(ch, start, length);
		// 如果内容不为空和空格，也不是换行符则将该元素名和值和存入map中
		if (currentValue == null || currentValue.trim().equals("") || currentValue.trim().equals("\n")) {
			return;
		}
		if ("servlet-name".equals(qName)) {
			currentServlet = currentValue;
			currentServletMapping = currentValue;
		} else if (qName.equals("servlet-class")) {
			// servlet信息
			String servletClass = currentValue;
			servlets.put(currentServlet, servletClass);
		} else if (qName.equals("param-name")) {
			currentParam = currentValue;
		} else if (qName.equals("param-value")) {
			String paramValue = currentValue;
			// servlet param 参数
			HashMap<String, String> params = new HashMap<>();
			params.put(currentParam, paramValue);
			servletParam.put(currentServlet, params);
		} else if ("servlet-name".equals(qName)) {
			currentServletMapping = currentValue;
		} else if (qName.equals("url-pattern")) {
			String urlPattern = currentValue;
			// url映射
			servletMapping.put(urlPattern, currentServletMapping);
		}
	}

	// 每个元素结束的时候都会调用该方法
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

	// 结束解析文档，即解析根元素结束标签时调用该方法
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	public Map<String, Object> getServlets() {
		return servlets;
	}

	public void setServlets(Map<String, Object> servlets) {
		this.servlets = servlets;
	}

	public Map<String, Map<String, String>> getServletParam() {
		return servletParam;
	}

	public void setServletParam(Map<String, Map<String, String>> servletParam) {
		this.servletParam = servletParam;
	}

	public Map<String, String> getServletMapping() {
		return servletMapping;
	}

	public void setServletMapping(Map<String, String> servletMapping) {
		this.servletMapping = servletMapping;
	}

	public Map<String, Object> getServletInstances() {
		return servletInstances;
	}

	public void setServletInstances(Map<String, Object> servletInstances) {
		this.servletInstances = servletInstances;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
}
