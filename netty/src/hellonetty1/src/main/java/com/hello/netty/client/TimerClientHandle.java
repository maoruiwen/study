package com.hello.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimerClientHandle extends ChannelHandlerAdapter {
	
	private final ByteBuf msg;
	
	public TimerClientHandle(){
		byte [] req = "发送请求数据".getBytes();
		msg = Unpooled.buffer(req.length);
		msg.writeBytes(req);
		msg.discardReadBytes()
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf byteBuf = (ByteBuf) msg;
		byte [] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		
		String body = new String(bytes,"utf-8");
		System.out.println("收到服务端数据:"+body);
	}
}
