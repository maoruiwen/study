package com.bio.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandle implements Runnable {

	private Socket socket;
	
	public  ServerHandle(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream(),true);
			
			String body = null;
				body = in.readLine();
				System.out.println(body);
				out.println(System.currentTimeMillis());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
