package com.hello.netty.coder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class TestUserInfo {
	public static void main(String[] args) throws IOException {
		int loop = 100000;
		UserInfo info = new UserInfo(10, "gawgeawgawe");
		long starttime = System.currentTimeMillis();
		for(int i =0 ; i<loop;i++){
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			os.writeObject(info);
			os.flush();
			os.close();
			byte[]b = bos.toByteArray();
		}
		System.out.println("耗费时间："+(System.currentTimeMillis()-starttime)+"ms");
		//System.out.println("jdk serializable:"+b.length);
		
		System.out.println("---------------------------");
		
		starttime = System.currentTimeMillis();
		ByteBuffer buffer = ByteBuffer.allocate(2048);
		for(int i =0 ;i<loop;i++){
			byte [] b = info.codeC(buffer);
		}
		System.out.println("The byte array serializable length is :"+(System.currentTimeMillis()-starttime));
	}
}
