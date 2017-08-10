package com.hello.netty.xieyi.client;

import com.hello.netty.xieyi.entry.Header;
import com.hello.netty.xieyi.entry.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端登录hander。
 * @author morw
 *
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

	/*
	 * tcp 3次握手之后，发送握手请求认证请求。 因为是ip白名单机制，无需携带消息
	 * @see io.netty.channel.ChannelHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildMessage());
	}

	/*
	 * 校验握手应答消息是否成功。
	 * @see io.netty.channel.ChannelHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		NettyMessage message = (NettyMessage) msg;
		if(message.getHeader()!=null&&message.getHeader().getType()==4){
			byte loginResult = (Byte) message.getBody();
			if(loginResult!=(byte)0){
				System.out.println("握手失败");
				ctx.close();//握手失败
			}else{
				System.out.println("login 握手成功："+message);
				ctx.fireChannelRead(msg);//转入到下一个channelHader
			}
		}else{
			ctx.fireChannelRead(msg);//转发到下一个channelHandler.
		}
	}
	
	public NettyMessage buildMessage(){
		NettyMessage nettyMessage = new NettyMessage();
		Header header = new Header();
		header.setType((byte)3);//3握手请求
		nettyMessage.setHeader(header);
		return nettyMessage;
	}

	/*
	 * 捕获异常
	 * @see io.netty.channel.ChannelHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}

	
	
	
}
