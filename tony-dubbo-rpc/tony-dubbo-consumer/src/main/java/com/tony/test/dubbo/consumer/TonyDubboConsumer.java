package com.tony.test.dubbo.consumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import com.alibaba.fastjson.JSON;

/**
 * 自己写一个dubbo消费者，去调用服务提供者
 * 
 * @author Tony
 *
 */
public class TonyDubboConsumer {
	public static void main(String[] args) throws Exception, IOException {
		SocketChannel socket = SocketChannel.open();
		// 此处虽然用了NIO的工具，但是却没有开启 NIO非阻塞模式
		// 1. 使用TCP
		// 2. 寻址（寻找服务提供者的地址:zookeeper）
		ZkClient zkClient = new ZkClient("127.0.0.1:2181");
		List<String> providers = zkClient.getChildren("/dubbo/com.dongnaoedu.tony.service.DemoService/providers");
		String providerUrl = null;
		for (String provider : providers) {
			providerUrl = URLDecoder.decode(provider, "utf-8");
		}
		if (providerUrl == null) {
			return;
		}
		System.out.println("从zk中寻找到一个服务提供者的地址:");
		System.out.println(providerUrl);
		String[] ipPort = providerUrl.split("/")[2].split(":");

		String host = ipPort[0];
		int port = Integer.valueOf(ipPort[1]);

		// 3. 序列化请求数据（包装成dubbo能认识的数据）

		// socket.configureBlocking(false);
		socket.connect(new InetSocketAddress(host, port));
		// -------------------------------组装请求报文 = header + body
		StringBuffer bodyString = new StringBuffer();
		// body
		// dubbo rpc版本
		bodyString.append(JSON.toJSONString("2.0.1")).append("\r\n");
		// 服务接口路径
		bodyString.append(JSON.toJSONString("com.dongnaoedu.tony.service.DemoService")).append("\r\n");
		// 服务版本号
		bodyString.append(JSON.toJSONString("0.0.0")).append("\r\n");
		// 服务方法名
		bodyString.append(JSON.toJSONString("getUserNameById")).append("\r\n");
		// 参数描述符,JVM参数描述符，按顺序填写
		bodyString.append(JSON.toJSONString("Ljava/lang/String;")).append("\r\n");
		// 参数值,需要进行转换(序列化)
		bodyString.append(JSON.toJSONString("Tony")).append("\r\n");
		// 隐式参数(为dubbo框架拓展提供的,是一个对象)
		bodyString.append("{}").append("\r\n");
		byte[] body = bodyString.toString().getBytes();
		System.out.println("body内容如下：");
		System.out.println(bodyString);
		System.out.println("1. 请求的body内容组装完成");

		// 头部
		// "header(16B)头部信息（调用控制协议）"
		byte[] header = new byte[16];
		// "magic(2B)魔数(2）"
		short magic = (short) 0xdabb;
		byte[] magicArray = ByteUtil.short2bytes(magic);
		System.arraycopy(magicArray, 0, header, 0, 2);

		// "flag(1B)标识（1）"
		header[2] = (byte) 0xC6;
		// "status(1B)响应状态(1)"，请求报文中，不需要填写,默认是0
		header[3] = 0x00;

		// "messageId(8B)消息ID（8）"
		byte[] messageId = ByteUtil.long2bytes(1);
		System.arraycopy(messageId, 0, header, 4, 8);
		// "bodyLength(4B)内容长度(4)"
		byte[] bodyLength = ByteUtil.int2bytes(body.length);
		System.arraycopy(bodyLength, 0, header, 12, 4);
		System.out.println("2. header部分组装完成");

		// 拼装请求报文
		byte[] request = new byte[body.length + header.length];
		System.arraycopy(header, 0, request, 0, header.length);
		System.arraycopy(body, 0, request, 16, body.length);
		// -------------------------------组装完成

		// 发起调用请求
		socket.write(ByteBuffer.wrap(request));
		System.out.println("3. header+body通过socket_outputStream已经传输到dubbo服务提供者。");

		// 输出RPC调用结果
		System.out.println("4. 接下来，使用socket_inputStream取出RPC调用结果:");
		// 1. 取出响应报文中的header
		ByteBuffer responseHeader = ByteBuffer.allocate(16);
		socket.read(responseHeader);
		// responseHeader 是无法输出的，因为不属于任何字符编码
		
		// 2. 取出响应报文中的body
		ByteBuffer responseBody = ByteBuffer.allocate(1024);
		socket.read(responseBody);
		System.out.println(new String(responseBody.array()));

		// 如果我的程序一直没停止， 就不关闭socket连接，这就是长连接
		socket.close();

	}
}
