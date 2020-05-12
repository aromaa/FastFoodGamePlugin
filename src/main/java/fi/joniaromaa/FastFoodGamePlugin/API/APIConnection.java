package fi.joniaromaa.FastFoodGamePlugin.API;

import java.nio.ByteOrder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Getter;

public class APIConnection
{
	private EventLoopGroup group;
	private Bootstrap boostrap;
	
	@Getter private APIConnectionHandler activeConnection;
	
	public APIConnection(String ip, int port)
	{
		this.group = new NioEventLoopGroup();
		
		this.boostrap = new Bootstrap();
		this.boostrap.group(this.group)
		.channel(NioSocketChannel.class)
		.handler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(SocketChannel ch) throws Exception
			{
				ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Short.MAX_VALUE, 0, 2, 0, 2, false));
				ch.pipeline().addLast(APIConnection.this.activeConnection = new APIConnectionHandler());
				
				ch.pipeline().addLast(new OutgoingPacketsEncoder());
			}
		});
		
		this.boostrap.connect(ip, port);
	}
	
	public void shutdown()
	{
		this.group.shutdownGracefully();
	}
}
