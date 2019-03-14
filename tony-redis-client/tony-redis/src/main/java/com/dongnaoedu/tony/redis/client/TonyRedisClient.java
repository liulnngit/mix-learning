package com.dongnaoedu.tony.redis.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 自己写的客户端
 * 
 * @author tony
 *
 */
public class TonyRedisClient {
	private Socket socket;
	private OutputStream writer;
	private InputStream reader;

	public TonyRedisClient(String host, int port) throws Exception {
		socket = new Socket(host, port);
		// 往外写数据
		writer = socket.getOutputStream();
		// 读取响应
		reader = socket.getInputStream();
	}

	// set命令的实现
	// set key value
	public String set(String key, String value) throws Exception {
		StringBuffer command = new StringBuffer();
		command.append("*3").append("\r\n");
		command.append("$3").append("\r\n");
		command.append("SET").append("\r\n");
		command.append("$").append(key.getBytes().length).append("\r\n");
		command.append(key).append("\r\n");
		command.append("$").append(value.getBytes().length).append("\r\n");
		command.append(value).append("\r\n");

		return exceConmmand(command);

	}

	// get
	// get key
	public String get(String key) throws Exception {
		// 怎么组装报文？
		StringBuffer command = new StringBuffer();
		command.append("*2").append("\r\n");
		command.append("$3").append("\r\n");
		command.append("GET").append("\r\n");
		command.append("$").append(key.getBytes().length).append("\r\n");
		command.append(key).append("\r\n");
		// 重构快捷键 shift alt m
		return exceConmmand(command);
	}

	// execute
	private String exceConmmand(StringBuffer command) throws IOException {
		// 发送命令，获取响应
		// 怎么发送呢？
		writer.write(command.toString().getBytes());
		// 怎么读取呢？
		byte[] result = new byte[1024];
		reader.read(result);
		return new String(result);
	}

	// 管道
	public Pipeline pipeline() {
		return new Pipeline(writer, reader);
	}

	// 订阅
	public Subscribe subscribe() {
		return new Subscribe(writer, reader);
	}

}
