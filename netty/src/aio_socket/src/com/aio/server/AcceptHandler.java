package com.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

	@Override
	public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
		System.out.println("客户端连入服务器：");
		attachment.asyncServerChannel.accept(attachment, this);
		
		//创建读取数据监听。
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		result.read(buffer,buffer,new ReadHander(result));//设置读取数据监听。
		
	}

	@Override
	public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
		System.out.println("链接失败");
		exc.printStackTrace();
		attachment.countDownlatch.countDown();
	}
}
