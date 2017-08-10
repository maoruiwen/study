package com.aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class WriterHander implements CompletionHandler<Integer, ByteBuffer>{

	//用于读取半包消息和发送应答  
    private AsynchronousSocketChannel channel;  
    public WriterHander(AsynchronousSocketChannel channel) {  
            this.channel = channel;  
    }  
    
    //发送消息
    public void write(String result){
    	byte [] bytes = result.getBytes();
    	ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
    	writeBuffer.put(bytes);
    	writeBuffer.flip();
    	channel.write(writeBuffer, writeBuffer, this);
    }
	
	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		if(attachment.hasRemaining()){
			channel.write(attachment,attachment,this);
		}else{
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			channel.read(byteBuffer,byteBuffer,new ReadHander(channel));
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		System.out.println("写出数据失败"+exc.getMessage());
		try {
			this.channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
