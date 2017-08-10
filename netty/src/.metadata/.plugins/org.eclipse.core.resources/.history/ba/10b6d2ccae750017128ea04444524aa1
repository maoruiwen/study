package com.hello.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimerClientHandle extends ChannelHandlerAdapter {
	
	private int counter;
	private  byte [] req;
	
	public TimerClientHandle(){
		req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
		
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf msg = null;
		for(int i =0 ; i<100;i++){
			msg = Unpooled.buffer(req.length);
			msg.writeBytes(req);
			ctx.writeAndFlush(msg);
		}
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String)msg;
		System.out.println("Now is: "+body+"; the counter is:"+ ++counter);
		/*ByteBuf byteBuf = (ByteBuf) msg;
		byte [] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		
		String body = new String(bytes,"utf-8");
		System.out.println("Now is: "+body+"; the counter is:"+ ++counter);*/
	}
}
