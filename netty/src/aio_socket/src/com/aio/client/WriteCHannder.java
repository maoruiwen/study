package com.aio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class WriteCHannder implements CompletionHandler<Integer, ByteBuffer> {
	
	private AsynchronousSocketChannel channel;
	private CountDownLatch latch;
	public WriteCHannder(AsynchronousSocketChannel channel,CountDownLatch latch) {
		this.channel = channel;
		this.latch = latch;
		
	}
	
	public void sendMsg(String result){
		byte[] bytes = result.getBytes();
		ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
		byteBuffer.put(bytes);
		byteBuffer.flip();
		channel.write(byteBuffer, byteBuffer, this);
	}

	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		if(attachment.hasRemaining()){
			channel.write(attachment, attachment, this);
		}else{
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			channel.read(byteBuffer,byteBuffer,new ReadCHander(channel, latch));
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			channel.close();
			latch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
