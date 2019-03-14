package com.dongnaoedu.tony.redis.proxy;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// redis代理服务
public class TonyRedisProxy {
	// redis服务器列表
	private static List<String> servers = new ArrayList<String>();

	static {
		// redis 实例
		// update 哨兵
		servers.add("127.0.0.1:6380");
		servers.add("127.0.0.1:6381");
		servers.add("127.0.0.1:6382");
	}

	// 最简单的代理实现负载均衡
	public static void main(String[] args) throws Exception {
		System.out.println("启动redis代理服务，端口:" + 19000);
		// 监听端口 
		ServerSocket serverSocket = new ServerSocket(19000);
		Socket socket;
		while ((socket = serverSocket.accept()) != null) {
			try {
				while (true) {
					// 负载，分片存储，日志审查
					System.out.println("一个链接....");
					InputStream inputStream = socket.getInputStream();
					byte[] request = new byte[1024];
					inputStream.read(request);
					
					// 根据RESP协议 - 解析请求
					String req = new String(request);
					System.out.println("收到请求：");
					System.out.println(req);
					
					String[] params = req.split("\r\n");
					// 获取key的长度 set abc tony_abc
					int keyLenth = Integer.parseInt(params[3].split("\\$")[1]);
					
					// 根据key长度取模
					int mod = keyLenth % servers.size();
					// 根据取模结果获取地址
					System.out.println("根据算法选择服务器:" + servers.get(mod));
					String[] serverInfo = servers.get(mod).split(":");

					// 代理请求 （类似nginx反向代理） 此处代理服务和redisServer进行连接
					// 先去哨兵服务器获取master
					Socket client = new Socket(serverInfo[0], Integer.parseInt(serverInfo[1]));
					client.getOutputStream().write(request);
					
					// 返回结果
					byte[] response = new byte[1024];
					// 获取redisserver 响应
					client.getInputStream().read(response);
					client.close();
					// 讲响应内容再 返回给 jedis
					socket.getOutputStream().write(response);

					System.out.println("##############打印结束");
					System.out.println();

				}
			} catch (Exception e) {
			}

		}
	}
}
