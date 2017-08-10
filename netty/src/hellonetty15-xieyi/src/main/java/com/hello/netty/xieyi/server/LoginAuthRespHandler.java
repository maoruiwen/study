package com.hello.netty.xieyi.server;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hello.netty.xieyi.entry.Header;
import com.hello.netty.xieyi.entry.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端登录验证hander
 * @author Administrator
 *
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter {
	
	private Map<String,Boolean>nodecheck = new ConcurrentHashMap<String,Boolean>();
	private String [] whitekList = {"127.0.0.1"};//白名单

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		if(message.getHeader()!=null&&message.getHeader().getType()==3){
			String nodeIndex = ctx.channel().remoteAddress().toString();//获取链接ip
			NettyMessage loginResult = null;
			if(nodecheck.containsKey(nodeIndex)){
				System.out.println("重复登录，关闭客户端链接");
				loginResult = buildRespon((byte)-1);//验证重复登录
			}else{
				/*开始校验登录白名单*/
				InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
				String ip = address.getAddress().getHostAddress();
				boolean isOk = false;
				for(String WIP:whitekList){
					if(WIP.equals(ip)){
						isOk = true;
						break;
					}
				}
				/*登录白名单校验完毕*/
				loginResult = isOk?buildRespon((byte)0):buildRespon((byte)-1);
				if(isOk){
					nodecheck.put(nodeIndex, true);
				}
				System.out.println("login response is"+loginResult);
				ctx.writeAndFlush(loginResult);
			}
		}else{
			ctx.fireChannelRead(msg);//如果为其他消息则往后执行。
		}
	}
	
	
	/*握手应答消息*/
	private NettyMessage buildRespon(byte result) {  
        NettyMessage message = new NettyMessage();  
        Header header = new Header();  
        header.setType((byte)4); //握手应答消息  
        message.setHeader(header);  
        message.setBody(result);  
        return message;  
    }  
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		nodecheck.remove(ctx.channel().remoteAddress().toString());
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}
	

}
