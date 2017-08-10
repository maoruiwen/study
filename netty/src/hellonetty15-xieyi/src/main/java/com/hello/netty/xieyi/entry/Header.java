package com.hello.netty.xieyi.entry;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义消息请求头
 * @author maorw
 *
 */
public final class Header {

	private int crcCode = 0xabef0101;//消息校验码（2884567297）1，0xabef（44015） + 主版本号 +次版本号 
	private int length ; //消息体长度。
	private long sessionid;//回话id。
	private byte type; //消息类型。
	private byte priority;//优先级
	private Map<String,Object>attachment = new HashMap<String, Object>();//附件，扩展字段。
	public int getCrcCode() {
		return crcCode;
	}
	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public long getSessionid() {
		return sessionid;
	}
	public void setSessionid(long sessionid) {
		this.sessionid = sessionid;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getPriority() {
		return priority;
	}
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	public Map<String, Object> getAttachment() {
		return attachment;
	}
	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
	@Override
	public String toString() {
		return "Header [crcCode=" + crcCode + ", length=" + length + ", sessionid=" + sessionid + ", type=" + type
				+ ", priority=" + priority + ", attachment=" + attachment + "]";
	}
	
	
	
	
}
