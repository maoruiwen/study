package com.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import javax.script.ScriptException;

import com.nio.util.Calculator;

public class MultiplexerTimerServer implements Runnable{
	
	private ServerSocketChannel Serverchannel;
	private Selector selector;
	private volatile boolean isClose = false;
	
	public  MultiplexerTimerServer(int port) {
		try {
			Serverchannel = ServerSocketChannel.open();
			Serverchannel.configureBlocking(false);
			Serverchannel.socket().bind(new InetSocketAddress(port), 1024);  //backlog 请求链接队列的最大长度。
			
			selector = Selector.open();//打开selector  
			Serverchannel.register(selector, SelectionKey.OP_ACCEPT);  //accept 如果发现当前链接是读，就去读另外的客户端链接。
			
			System.out.println("链接端口："+port);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	  public void stop(){  
		  isClose = true;  
	  }

	
	@Override
	public void run() {
		//异步读取消息
		while(!isClose){
			try {
				selector.select(1000);//每隔1s 轮询一次数据
				Set<SelectionKey>keys = selector.selectedKeys();
				Iterator<SelectionKey>iters = keys.iterator();
				SelectionKey selectionKey =null;
				while(iters.hasNext()){
					selectionKey = iters.next();
					iters.remove();//删除当前已经获取到的元素。
					try {
						handleInput(selectionKey);
					} catch (Exception e) {
						if(selectionKey!=null){
							selectionKey.cancel();
							if(selectionKey.channel()!=null){
								selectionKey.channel().close();
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
	
	//读取消息
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){//判断key 是否有效。
			
			//处理新接入数据请求消息  新的通道
			if(key.isAcceptable()){
				//同意一个新的链接
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			
			//读取消息
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer buffer = ByteBuffer.allocate(1024);//创建byteBuffer  并开辟1m的缓存。
				
				int readBytes = sc.read(buffer);
				if(readBytes > 0){
					
					buffer.flip();//将缓冲区当前的limit postion  设置为0  用于后续缓冲操作。
					byte [] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);//把缓冲区的数据放到新数组中。
					String expression = new String(bytes,"utf-8");
					System.out.println("服务器接收到的消息："+expression);
					
					String result = null;
					
					result = "服务器发送数据";
					doWrite(sc, result);
				}else if(readBytes<0){
					key.cancel();
					sc.close();
				}
				
			}
			
			if(key.isWritable()){
				
			}
		}
	}
	
	//异步发送应答消息
	private void doWrite(SocketChannel channel,String response) throws IOException{
		byte [] bytes = response.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		channel.write(writeBuffer);
	}

}
