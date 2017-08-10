package com.hello.netty.messagepack.decoder;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 自定义解码器 。反解码。
 * @author Administrator
 *
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		byte [] array ;
		int length = in.readableBytes();
		array = new byte[length];
		in.getBytes(in.readerIndex(),array,0,length);
		MessagePack msgPack = new MessagePack();
		out.add(msgPack.read(array));
	}
}
