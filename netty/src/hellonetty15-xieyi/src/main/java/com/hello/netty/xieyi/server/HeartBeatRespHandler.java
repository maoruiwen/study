package com.hello.netty.xieyi.server;

import com.hello.netty.xieyi.entry.Header;
import com.hello.netty.xieyi.entry.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 心跳应答请求
 * @author Administrator
 *
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		if(message!=null&&message.getHeader().getType()==5){//如果为心跳请求
			System.out.println("server receive client heart message:"+message);
			NettyMessage heartMsg = buildHeartBeat();
			ctx.writeAndFlush(heartMsg);
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	/*构建心跳应答请求*/
	private NettyMessage buildHeartBeat(){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType((byte)6);
		message.setHeader(header);
		return message;
	}
	
}
