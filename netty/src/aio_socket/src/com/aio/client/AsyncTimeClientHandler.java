package com.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements Runnable {
	private String host;
	private int port;
	private AsynchronousSocketChannel aSChannel;
	private CountDownLatch latch;
	public AsyncTimeClientHandler(String host,int port) {
		this.host = host;
		this.port = port;
		try {
			aSChannel = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		latch = new CountDownLatch(1);
		aSChannel.connect(new InetSocketAddress(host, port));
		new WriteCHannder(aSChannel, latch).sendMsg("发送一条消息给服务器端");
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			aSChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
