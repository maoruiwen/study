package com.hello.netty.coder;

import java.io.Serializable;
import java.nio.ByteBuffer;

import org.msgpack.annotation.Message;


public class UserInfo implements Serializable {
	
	
	public UserInfo(int userId,String username){
		this.userId = userId;
		this.username = username;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int userId;
	private String username;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public byte[] codeC(ByteBuffer buffer){
		buffer.clear();
		byte[] value = this.username.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userId);
		buffer.flip();
		value= null;
		byte[]result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}
}
