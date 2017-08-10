package com.hello.netty.xieyi.coder;

import java.util.HashMap;
import java.util.Map;

import com.hello.netty.xieyi.coder.util.MarshallingCodeCFactory;
import com.hello.netty.xieyi.coder.util.NettyMarshallingD;
import com.hello.netty.xieyi.entry.Header;
import com.hello.netty.xieyi.entry.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
	

	private NettyMarshallingD marshallingDecoder; 
	/**
	 * 
	 * @param maxFrameLength  允许直接长度
	 * @param lengthFieldOffset 偏移量，便宜多少位为length的长度
	 * @param lengthFieldLength 标识长度数据占多少字符
	 * @param lengthAdjustment  实际字节数 = length(总字节)+（lenthAdjustment）
	 * @param initialBytesToStrip  标识decode的时候去掉的直接。   
	 */
	public NettyMessageDecoder(
            int maxFrameLength,
            int lengthFieldOffset, int lengthFieldLength,
            int lengthAdjustment, int initialBytesToStrip){
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
		
		//1024*1024,4,4,-8,0
		this.marshallingDecoder = MarshallingCodeCFactory.buildMarshallingDecoder();
	}
	
	/*解码*/
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if(frame==null){
			return null;
		}
		
		NettyMessage message = new NettyMessage();//实例化协议
		
		Header header =  new Header();
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionid(frame.readLong());
		header.setType(frame.readByte());
		header.setPriority(frame.readByte());
		
		int size = frame.readInt();
		//判断是否有附件数据
		if(size>0){
			Map<String,Object>attach = new HashMap<String,Object>();
			int keySize= 0;
			byte [] keyArray = null;
			String key = null;
			for(int i = 0 ;i<size;i++){
				keySize = frame.readInt();
				keyArray = new byte[keySize];
				frame.readBytes(keyArray);//默认 写入数据为in。  
				key = new String(keyArray,"utf-8");
				attach.put(key, marshallingDecoder.decode(ctx, frame));//默认为in  
			}
			 key = null;  
	         keyArray = null;  
	         header.setAttachment(attach); 
		}
		
		if(frame.readableBytes()>4){
			message.setBody(marshallingDecoder.decode(ctx, frame));
		}
		message.setHeader(header);
		return message;
	}
	
}
