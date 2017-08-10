package com.bio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.bio.util.ServerHandle;

/**
 * 创建socket bio server端。
 * @author Administrator
 *
 */
public class TimeServer {
	
	public static void main(String[] args) {
		
		int port = 8080;
		
		if(args!=null && args.length>0){
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			
			Socket socket = null;
			while(true){
				socket = server.accept();
				new Thread(new ServerHandle(socket)).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(server!=null){
				try {
					server.close();
					server=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
