package com.dongnaoedu.tony.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * java热部署
 * 
 * @author Tony
 *
 */
public class HotDeployTests {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		// 1. Bootstrap loader 核心类库加载器
		// 2. extension class loader 拓展类库加载器
		// 3. application class loader 用户应用程序加载器
		// 不能解决我们的问题
		
		// 不会重复加载 同样的类名，同一个类加载器，这就表示为 同一个类
		// 首先不会自己去尝试加载这个类，而是把这个请求委派给父加载器去完成，
		// 每一个层次的加载器都是如此，因此所有的类加载请求都会传给顶层的启动类加载器。
		// 只有当父加载器反馈自己无法完成该加载请求（该加载器的搜索范围中没有找到对应的类）时，子加载器才会尝试自己去加载。

		// 自己构建一个类加载器
		URL dabaojianUrl = new URL("file:C:\\Users\\zhang\\Desktop\\tonyjar.jar");
		// jdk提供 一个 可以自己来拓展的，可以自己去实例化的类加载工具
		URLClassLoader parentClassLoader = new URLClassLoader(new URL[] { dabaojianUrl });

		while (true) {
			// 优化： 根据tonyjar.jar 文件修改时间，去判断 是否有变更

			// 自己构建一个类加载器
			URL dabaojianJarUrl = new URL("file:C:\\Users\\zhang\\Desktop\\tonyjar.jar");

			// jdk提供 一个 可以自己来拓展的，可以自己去实例化的类加载工具
			URLClassLoader customClassLoader = new URLClassLoader(new URL[] { dabaojianJarUrl }, parentClassLoader);

			Class<?> dabaojianClazz = customClassLoader.loadClass("com.dongnaoedu.tony.classloader.DabaojianService");

			Object dabaojianService = dabaojianClazz.newInstance();
			dabaojianClazz.getMethod("service").invoke(dabaojianService);

			// 什么情况下，会被GC

			// 第一个条件：当 这个类 的实例 都被GC了
			// 第二个条件：当 类加载器被 GC
			// 类 才有可能被卸载

			System.gc();

			Thread.sleep(3000L);
		}
	}
}
