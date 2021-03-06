package com.hello.netty.messagepack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

/**
 * 测试message框架api
 * @author Administrator
 *
 */
public class FirstMesssagePack {
	
	public static void main(String[] args) throws IOException {
		List<String>strs = new ArrayList<String>();
		strs.add("你好");
		strs.add("还行吧");
		strs.add("凑活干着点");
		MessagePack msgPack  = new MessagePack();
		byte [] bytes = msgPack.write(strs); //序列化为二进制数据
		
		List<String>dsts = msgPack.read(bytes,Templates.tList(Templates.TString));
		System.out.println(dsts.get(0));
		System.out.println(dsts.get(1));
		System.out.println(dsts.get(2));
	}
	
}
