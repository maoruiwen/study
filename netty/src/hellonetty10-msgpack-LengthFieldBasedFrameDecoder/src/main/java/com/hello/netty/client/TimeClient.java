package com.hello.netty.client;

import com.hello.netty.messagepack.decoder.MsgPackDecoder;
import com.hello.netty.messagepack.encoder.MsgPackEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

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
				/**
				 * 
				 * @param maxFrameLength  允许直接长度
				 * @param lengthFieldOffset 偏移量，便宜多少位为length的长度
				 * @param lengthFieldLength 标识长度数据占多少字符
				 * @param lengthAdjustment  实际字节数 = length(总字节)+（lenthAdjustment）
				 * @param initialBytesToStrip  标识decode的时候去掉的直接。   
				 */
				ch.pipeline().addLast(" frame decoder ", new LengthFieldBasedFrameDecoder(65535,0,2,0,2))
				.addLast("msgpack decoder", new MsgPackDecoder())
				
				/**
				 * 编码添加直接长度，占领个直接。   默认为false 记录真实长度。   为ture(真实长度+length)  为true 则解码 lengthAdjustment 设置为-2
				 */
				.addLast("frame encoder", new LengthFieldPrepender(2))
				.addLast("msgpack encoder", new MsgPackEncoder()).addLast(new TimerClientHandle());
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
