package com.nio.client;

public class TimeClient {
	
	public static void main(String[] args) {
		TimeClientHandle tcHandle = new TimeClientHandle(8080, "127.0.0.1");
		new Thread(tcHandle,"clientConnetion").start();
	}
	
}
