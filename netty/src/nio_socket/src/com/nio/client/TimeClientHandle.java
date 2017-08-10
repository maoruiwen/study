package com.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandle implements Runnable{
	private SocketChannel socketChannel;
	private Selector selector;
	private int port;
	private String host;
	private volatile boolean isStop = false;
	
	public TimeClientHandle(int port,String host) {
		try {
			this.port = port;
			this.host = host;
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void run() {
		
		try {
			doConnect();
		} catch (IOException e) {
			System.exit(1);
			e.printStackTrace();
		}
		
		while(!isStop){
			try {
				selector.select(1000);
				Set<SelectionKey>selectedKeys =selector.selectedKeys();
				Iterator<SelectionKey>iters = selectedKeys.iterator();
				SelectionKey selectionkey = null;
				while(iters.hasNext()){
					selectionkey = iters.next();
					iters.remove();
					try {
						handleInput(selectionkey);
					} catch (Exception e) {
						if(selectionkey!=null){
							selectionkey.cancel();
							if(selectionkey.channel()!=null){
								selectionkey.channel().close();
							}
						}
						e.printStackTrace();
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 读取一条数据
	 * @throws IOException 
	 */
	private void handleInput(SelectionKey key) throws IOException{
		
		if(key.isValid()){
			
			SocketChannel sc = (SocketChannel) key.channel();
			if(key.isConnectable()){
				if(sc.finishConnect()){
					sc.register(selector, SelectionKey.OP_READ);
					doWriter(sc);
				}else{
					System.exit(1);
					System.out.println("链接失败");
				}
			}
			
			
			if(key.isReadable()){
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readByte = sc.read(readBuffer);
				if(readByte>0){
					readBuffer.flip();
					byte [] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					
					String body = new String(bytes,"utf-8");
					System.out.println("收取到数据："+body);
					
					//this.isStop = true;
				}else if(readByte<0){
					key.cancel();
					sc.close();
				}
			}
			
		}
		
	}
	
	//建立链接
	private void doConnect() throws IOException{
		
		boolean flag = socketChannel.connect(new InetSocketAddress(host, port));
		if(flag){
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWriter(socketChannel);
		}else{
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}
	
	//写出数据
	private void doWriter(SocketChannel socketChannel) throws IOException{
		for(int i =0 ;i<3;i++){
			byte [] req = "QUERY TIME ORDER".getBytes();
			ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
			byteBuffer.put(req);
			byteBuffer.flip();
			socketChannel.write(byteBuffer);
			if(!byteBuffer.hasRemaining()){
				System.out.println("发送数据成功");
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
