package com.dongnaoedu.tony;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 接收网络请求
 * 
 * @author 动脑学院.Tony老师
 *
 */
public class HttpConnector {
	// 处理请求的线程池
	public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(25, 50, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public void start() throws IOException {
		ServerSocket serverSocket = null;
		try {
			// bind 监听端口
			serverSocket = new ServerSocket(9999);
			System.out.println(Thread.currentThread().getName() + " 启动 " + 9999);
			// 循环获取新的连接
			while (true) {
				// 获取连接，此处阻塞，没有新的连接就会一直停在这里
				Socket socket = serverSocket.accept();
				// 用线程池来处理连接
				threadPoolExecutor.execute(new Runnable() {

					@Override
					public void run() {
						try {
							// 交给适配器,让engine去处理业务操作
							new CoyoteAdapter().service(socket);
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
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null)
				serverSocket.close();
		}
	}
}
