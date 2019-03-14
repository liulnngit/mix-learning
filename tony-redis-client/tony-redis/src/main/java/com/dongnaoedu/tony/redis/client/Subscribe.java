package com.dongnaoedu.tony.redis.client;

import java.io.InputStream;
import java.io.OutputStream;

// 订阅
public class Subscribe {

	private OutputStream writer;
	private InputStream reader;

	public Subscribe(OutputStream writer, InputStream reader) {
		this.writer = writer;
		this.reader = reader;
	}

	// 粉丝 订阅 对象
	public void fans(String channel) throws Exception {
		// subscribe ruolan
		// 
		// 怎么组装报文？
		StringBuffer command = new StringBuffer();
		command.append("*2").append("\r\n");
		command.append("$9").append("\r\n");
		command.append("subscribe").append("\r\n");
		command.append("$").append(channel.getBytes().length).append("\r\n");
		command.append(channel).append("\r\n");

		writer.write(command.toString().getBytes());
		// 不断的获取新动态
		while (true) {
			byte[] b = new byte[1024];
			reader.read(b);
			System.out.println(channel + "有新动态啦：");
			System.out.println(new String(b));
		}
	}

}
