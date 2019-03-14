package com.dongnaoedu.tony.classloader;

import sun.net.spi.nameservice.dns.DNSNameService;

public class ViewClassLoaderTest {
	public static void main(String[] args) throws Exception {
		// 加载核心类库的 BootStrap ClassLoader
		System.out.println("String.class 核心类库加载器：" + String.class.getClassLoader());

		// 加载拓展库的 Extension ClassLoader
		System.out.println("DNSNameService.class 拓展类库加载器：" + DNSNameService.class.getClassLoader());

		// 加载用户应用程序的
		ClassLoader appClassLoader = ViewClassLoaderTest.class.getClassLoader();
		System.out.println("ViewClassLoaderTest.class 用户应用程序库加载器：实例：" + appClassLoader);
		System.out.println("ViewClassLoaderTest.class 父加载器：实例：" + appClassLoader.getParent());
		System.out.println("ViewClassLoaderTest.class 父加载器-父加载器：实例：" + appClassLoader.getParent().getParent());
		// 1. 防止重复加载
		// 2. 安全上的考虑,防止
		System.out.println(appClassLoader.loadClass("Object").getClassLoader());
	}
}
