package com.nio.server;

public class TimeServer {
	public static void main(String[] args) {
		
		int port = 8080;
		MultiplexerTimerServer mTS = new MultiplexerTimerServer(port);
		new Thread(mTS,"Thread001").start();;
	}
}
