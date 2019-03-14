package com.dongnaoedu.tony.utils;

import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

public class TomcatUtils {
	/**
	 * 获取TOMCAT http线程池中繁忙线程数
	 * 
	 * @throws Exception
	 */
	public static int getThreadNums() throws Exception {
		// 此处源码参考：org.apache.catalina.manager.StatusManagerServlet
		
		// 此处返回tomcat线程池中的线程数
		MBeanServer mBeanServer = null;
		if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
			mBeanServer = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
		}
		// Query Thread Pools
		String onStr = "*:type=ThreadPool,*";
		ObjectName objectName = new ObjectName(onStr);
		Set<ObjectInstance> set = mBeanServer.queryMBeans(objectName, null);
		Iterator<ObjectInstance> iterator = set.iterator();
		String httpThreadPoolName = null;
		while (iterator.hasNext()) {
			ObjectInstance oi = iterator.next();
			// 找到处理http的线程池
			if (oi.getObjectName().toString().contains("http")) {
				httpThreadPoolName = oi.getObjectName().toString();
				break;
			}
		}
		// 获取繁忙数
		Object value = mBeanServer.getAttribute(new ObjectName(httpThreadPoolName), "currentThreadsBusy");
		return Integer.valueOf(value.toString());
	}
}
