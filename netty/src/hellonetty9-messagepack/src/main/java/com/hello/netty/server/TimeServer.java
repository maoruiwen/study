package com.hello.netty.server;


import com.hello.netty.messagepack.decoder.MsgPackDecoder;
import com.hello.netty.messagepack.encoder.MsgPackEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TimeServer {
	public static void main(String[] args) {
		new TimeServer().bind(8080);
		
	}
	
	public void bind(int port){
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap serverB = new ServerBootstrap();
			serverB.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChildChannelHandler());//backlog 请求链接队列最大长度。
			ChannelFuture f = serverB.bind(port).sync();
			System.out.println("启动成功"+port);
			f.channel().closeFuture().sync();//等待服务监听端口关闭
		} catch (InterruptedException e) {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
			e.printStackTrace();
		}
		
	}
	
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast("msgpack decoder", new MsgPackDecoder()).addLast("msgpack encoder", new MsgPackEncoder()).addLast(new TimerServerHandle());
		}
		
	}
}
