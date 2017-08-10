package com.hello.netty.coder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TestUserInfo {
	public static void main(String[] args) throws IOException {
		UserInfo info = new UserInfo(10, "gawgeawgawe");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(info);
		os.flush();
		os.close();
		byte[]b = bos.toByteArray();
		System.out.println("jdk serializable:"+b.length);
		
		System.out.println("---------------------------");
		
		System.out.println("The byte array serializable length is :"+info.codeC().length);
	}
}
