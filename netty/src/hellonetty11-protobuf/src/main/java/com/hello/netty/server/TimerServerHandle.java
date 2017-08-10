package com.hello.netty.server;


import java.util.List;

import com.hello.netty.entry.UserInfo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimerServerHandle extends ChannelHandlerAdapter {
	
	private int counter=0;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		/*ByteBuf buf = (ByteBuf) msg;
		byte[]req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req,"utf-8").substring(0,req.length-System.getProperty("line.separator").length());*/
		@SuppressWarnings({  "unchecked" })
		List<UserInfo>users  = (List<UserInfo>) msg;
		System.out.println("请求数据："+users+"; the counter is"+ ++counter);
		
	/*	String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString():"BAD ORDER";
		currentTime = currentTime+"&_";
		
		ByteBuf bf = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(bf);*/
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
}
