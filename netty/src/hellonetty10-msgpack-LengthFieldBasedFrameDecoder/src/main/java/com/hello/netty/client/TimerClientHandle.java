package com.hello.netty.client;


import com.hello.netty.entry.UserInfo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimerClientHandle extends ChannelHandlerAdapter {
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
			for(int i =0 ;i<1000;i++){
				UserInfo userinfo = new UserInfo();
				userinfo.setAge(100);
				userinfo.setName("1211111111");
				userinfo.setSex(1000);
				ctx.writeAndFlush(userinfo);
				System.out.println("发送数据："+i);
			}
			ctx.flush();
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		/*String body = (String)msg;
		System.out.println("Now is: "+body+"; the counter is:");*/
		/*ByteBuf byteBuf = (ByteBuf) msg;
		byte [] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		
		String body = new String(bytes,"utf-8");
		System.out.println("Now is: "+body+"; the counter is:"+ ++counter);*/
	}
}
