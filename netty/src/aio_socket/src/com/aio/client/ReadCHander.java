package com.aio.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class ReadCHander implements CompletionHandler<Integer, ByteBuffer>{
	
	private AsynchronousSocketChannel channel;
	private CountDownLatch latch;
	
	public ReadCHander(AsynchronousSocketChannel channel,CountDownLatch latch){
		this.channel = channel;
		this.latch = latch;
	}

	@Override
	public void completed(Integer result, ByteBuffer buffer) {
		buffer.flip();
		byte [] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);
		try {
			String body = new String(bytes,"utf-8");
			System.out.println(body+":服务器端反馈数据");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		System.out.println("数据读取失败");
		try {
			channel.close();
			latch.countDown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
