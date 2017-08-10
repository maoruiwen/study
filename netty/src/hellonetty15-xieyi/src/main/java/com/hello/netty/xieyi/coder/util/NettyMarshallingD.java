package com.hello.netty.xieyi.coder.util;
import io.netty.buffer.ByteBuf;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.handler.codec.marshalling.MarshallingDecoder;  
import io.netty.handler.codec.marshalling.UnmarshallerProvider;  
  
public class NettyMarshallingD extends MarshallingDecoder{  
  
    public NettyMarshallingD(UnmarshallerProvider provider) {  
        super(provider);  
    }  
      
    public NettyMarshallingD(UnmarshallerProvider provider, int maxObjectSize) {  
        super(provider, maxObjectSize);  
    }  
      
    @Override  
    public Object decode(ChannelHandlerContext arg0, ByteBuf arg1)  
            throws Exception {  
        return super.decode(arg0, arg1);  
    }  
  
}