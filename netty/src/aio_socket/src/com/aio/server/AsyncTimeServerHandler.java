package com.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable {
	AsynchronousServerSocketChannel asyncServerChannel;
	CountDownLatch countDownlatch;
	
	public AsyncTimeServerHandler(int port) {
		try {
			asyncServerChannel = AsynchronousServerSocketChannel.open();
			asyncServerChannel.bind(new InetSocketAddress(port));
			System.out.println("server start port:"+port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		countDownlatch = new CountDownLatch(1);
		doAccept();
		try {
			countDownlatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//添加服务端监听
	private void doAccept(){
		asyncServerChannel.accept(this,new AcceptHandler());
		System.out.println("dadad");
	}
}
