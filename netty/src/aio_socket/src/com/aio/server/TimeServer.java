package com.aio.server;

public class TimeServer {
	
	public static void main(String[] args) {
		new Thread(new AsyncTimeServerHandler(8080),"Server").start();
	}
}
