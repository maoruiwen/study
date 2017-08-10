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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
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
			
			/**
			 * 
			 * @param maxFrameLength  允许直接长度
			 * @param lengthFieldOffset 偏移量，便宜多少位为length的长度
			 * @param lengthFieldLength 标识长度数据占多少字符
			 * @param lengthAdjustment  实际字节数 = length(总字节)+（lenthAdjustment）
			 * @param initialBytesToStrip  标识decode的时候去掉的直接。   
			 */
			ch.pipeline().addLast(" framedecoder ", new LengthFieldBasedFrameDecoder(65535,0,2,0,2))
			.addLast("msgpack decoder", new MsgPackDecoder())
			
			/**
			 * 编码添加直接长度，占领个直接。   默认为false 记录真实长度。   为ture(真实长度+length)  为true 则解码 lengthAdjustment 设置为-2
			 */
			.addLast("frame encoder", new LengthFieldPrepender(2))
			.addLast("msgpack encoder", new MsgPackEncoder()).addLast(new TimerServerHandle());
		}
		
	}
}
