package com.tony.test.dubbo.consumer.n1;
//package com.tony.test.dubbo.consumer;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.MessageToByteEncoder;
//
//public class DubboRpcEncoder extends MessageToByteEncoder<Object> {
//    // header length.
//    protected static final int HEADER_LENGTH = 16;
//
//    @Override
//    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf buffer) throws Exception {
//    	byte[] bs = (byte[]) msg;
//        buffer.writeBytes(bs);
//    }
//
//}
