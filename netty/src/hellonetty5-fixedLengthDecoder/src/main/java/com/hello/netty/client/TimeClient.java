package com.hello.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {
	public static void main(String[] args) {
		new TimeClient().connect(8080, "127.0.0.1");
	}
	
	public void connect(int port,String host){
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
		.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ByteBuf bf_delimiter = Unpooled.copiedBuffer("&_".getBytes());
				ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,bf_delimiter)).addLast(new StringDecoder()).addLast(new TimerClientHandle());
			}
			
		});
		
		try {
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			group.shutdownGracefully();
			e.printStackTrace();
		}
	}
}
