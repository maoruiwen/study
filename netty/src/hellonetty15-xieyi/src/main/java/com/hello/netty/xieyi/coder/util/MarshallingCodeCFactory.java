package com.hello.netty.xieyi.coder.util;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;  
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;  
import io.netty.handler.codec.marshalling.MarshallerProvider;  
import io.netty.handler.codec.marshalling.UnmarshallerProvider;  
  
import org.jboss.marshalling.MarshallerFactory;  
import org.jboss.marshalling.Marshalling;  
import org.jboss.marshalling.MarshallingConfiguration;  
  
public class MarshallingCodeCFactory {  
  
    public static NettyMarshallingD buildMarshallingDecoder() {  
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");  
        MarshallingConfiguration configuration = new MarshallingConfiguration();  
        configuration.setVersion(5);  
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);  
        NettyMarshallingD decoder = new NettyMarshallingD(provider, 10240);  
        return decoder;  
    }  
      
    public static NettyMarshallingE buildMarshallingEncoder() {  
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");  
        MarshallingConfiguration configuration = new MarshallingConfiguration();  
        configuration.setVersion(5);  
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);  
        NettyMarshallingE encoder = new NettyMarshallingE(provider);  
        return encoder;  
    }  
}  