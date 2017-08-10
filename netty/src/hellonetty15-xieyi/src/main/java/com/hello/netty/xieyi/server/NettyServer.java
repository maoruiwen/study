package com.hello.netty.xieyi.server;

import com.hello.netty.xieyi.coder.NettyMessageDecoder;
import com.hello.netty.xieyi.coder.NettyMessageEncoder;

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
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {
	
	public void bind(int port){
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4,-8,0))
				.addLast(new NettyMessageEncoder())
				.addLast("readTimeOutHandler",new ReadTimeoutHandler(50))
				.addLast("login server", new LoginAuthRespHandler())
				.addLast("heartBeathandler",new HeartBeatRespHandler());
			}
			
		});
		try {
			ChannelFuture f = b.bind(port);
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		int port = 8080;
		new NettyServer().bind(port);
	}
	
}
