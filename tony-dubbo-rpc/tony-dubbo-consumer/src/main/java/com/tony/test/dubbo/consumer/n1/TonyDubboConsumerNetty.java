package com.tony.test.dubbo.consumer.n1;
//package com.tony.test.dubbo.consumer;
//
//import java.io.IOException;
//
//import com.alibaba.fastjson.JSON;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//
///**
// * 自己写一个dubbo消费者，去调用服务提供者
// * 
// * @author Tony
// *
// */
//public class TonyDubboConsumerNetty {
//	public static void main(String[] args) throws Exception, IOException {
//		EventLoopGroup group = new NioEventLoopGroup();
//		try {
//			Bootstrap b = new Bootstrap();
//			b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
//				@Override
//				protected void initChannel(SocketChannel ch) throws Exception {
//					ChannelPipeline p = ch.pipeline();
//					 p.addLast(new DubboRpcEncoder());
//					p.addLast(new DiscardClientHandler());
//				}
//			});
//
//			// Make the connection attempt.
//			ChannelFuture f = b.connect("127.0.0.1", 28080).sync();
//
//			// Wait until the connection is closed.
//			f.channel().closeFuture().sync();
//		} finally {
//			group.shutdownGracefully();
//		}
//	}
//}
//
//class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {
//
//	private ByteBuf content;
//	private ChannelHandlerContext ctx;
//
//	@Override
//	public void channelActive(ChannelHandlerContext ctx) {
//		this.ctx = ctx;
//
//		// Initialize the message.
////		content = ctx.alloc().directBuffer(1024);//.writeZero(1024);
////		content = Unpooled.buffer(1024);
//		// -------------------------------组装请求报文 = header + body
//		StringBuffer bodyString = new StringBuffer();
//		// body
//		// dubbo rpc版本
//		bodyString.append(JSON.toJSONString("2.0.1")).append("\r\n");
//		// 服务接口路径
//		bodyString.append(JSON.toJSONString("com.dongnaoedu.tony.service.UserQueryService")).append("\r\n");
//		// 服务版本号
//		bodyString.append(JSON.toJSONString("0.0.0")).append("\r\n");
//		// 服务方法名
//		bodyString.append(JSON.toJSONString("getUserNameById")).append("\r\n");
//		// 参数描述符,JVM参数描述符，按顺序填写
//		bodyString.append(JSON.toJSONString("Ljava/lang/String;")).append("\r\n");
//		// 参数值,需要进行转换(序列化)
//		bodyString.append(JSON.toJSONString("Tony")).append("\r\n");
//		// 隐式参数(为dubbo框架拓展提供的,是一个对象)
//		bodyString.append("{}").append("\r\n");
//		byte[] body = bodyString.toString().getBytes();
//		System.out.println("body内容如下：");
//		System.out.println(bodyString);
//		System.out.println("1. 请求的body内容组装完成");
//
//		// 头部
//		// "header(16B)头部信息（调用控制协议）"
//		byte[] header = new byte[16];
//		// "magic(2B)魔数(2）"
//		short magic = (short) 0xdabb;
//		byte[] magicArray = ByteUtil.short2bytes(magic);
//		System.arraycopy(magicArray, 0, header, 0, 2);
//
//		// "flag(1B)标识（1）"
//		header[2] = (byte) 0xC6;
//		// "status(1B)响应状态(1)"，请求报文中，不需要填写,默认是0
//		header[3] = 0x00;
//
//		// "messageId(8B)消息ID（8）"
//		byte[] messageId = ByteUtil.long2bytes(1);
//		System.arraycopy(messageId, 0, header, 4, 8);
//		// "bodyLength(4B)内容长度(4)"
//		byte[] bodyLength = ByteUtil.int2bytes(body.length);
//		System.arraycopy(bodyLength, 0, header, 12, 4);
//		System.out.println("2. header部分组装完成");
//
//		// 拼装请求报文
//		byte[] request = new byte[body.length + header.length];
//		System.arraycopy(header, 0, request, 0, header.length);
//		System.arraycopy(body, 0, request, 16, body.length);
//		// -------------------------------组装完成
////		content.writeBytes(request);
//
//		ctx.writeAndFlush(request);
//	}
//
//	@Override
//	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//		// Server is supposed to send nothing, but if it sends something, discard it.
//		System.out.println(msg.toString());
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//		// Close the connection when an exception is raised.
//		cause.printStackTrace();
//		ctx.close();
//	}
//
//}
