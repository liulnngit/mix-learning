package com.tony.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 虚拟的redis服务
 * @author tony
 *
 */
public class RedisVM {
	public static void main(String[] args) throws IOException {
		// server
		ServerSocket serverSocket = new ServerSocket(6379);
		System.out.println("启动虚拟redis服务：6379");
		// socket
		Socket accept = serverSocket.accept();
		
		// 读取传输的内容
		InputStream inputStream = accept.getInputStream();
		byte[] b = new byte[1024];
		inputStream.read(b);
		
		System.out.println("收到的内容如下：");
		System.out.println(new String(b));
	}
}
