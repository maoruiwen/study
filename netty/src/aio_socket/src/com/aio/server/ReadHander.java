package com.aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadHander implements CompletionHandler<Integer, ByteBuffer> {

	//用于读取半包消息和发送应答  
    private AsynchronousSocketChannel channel;  
    public ReadHander(AsynchronousSocketChannel channel) {  
            this.channel = channel;  
    }  
    
	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] message = new byte[attachment.remaining()];
		attachment.get(message);
		try {
			String expression = new String(message,"UTF-8");
			System.out.println("接受到的消息："+expression);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new WriterHander(channel).write("接收到请求数据");
		
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		System.out.println("读取失败："+exc.getMessage());
		try {
			this.channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
