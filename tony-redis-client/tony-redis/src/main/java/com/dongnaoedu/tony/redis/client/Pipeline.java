package com.dongnaoedu.tony.redis.client;

import java.io.InputStream;
import java.io.OutputStream;

// 管道
public class Pipeline {

	private OutputStream writer;
	private InputStream reader;

	public Pipeline(OutputStream writer, InputStream reader) {
		this.writer = writer;
		this.reader = reader;
	}

	// 发送命令
	// set
	public void set(String key, String value) throws Exception {
		StringBuffer command = new StringBuffer();
		command.append("*3").append("\r\n");
		command.append("$3").append("\r\n");
		command.append("SET").append("\r\n");
		command.append("$").append(key.getBytes().length).append("\r\n");
		command.append(key).append("\r\n");
		command.append("$").append(value.getBytes().length).append("\r\n");
		command.append(value).append("\r\n");

		// 发送命令
		writer.write(command.toString().getBytes());
		// 不获取响应
	}

	// 获取所有命令响应内容
	public String sync() throws Exception {
		// 怎么读取呢？
		byte[] result = new byte[1024 * 10];
		reader.read(result);
		return new String(result);
	}

}
