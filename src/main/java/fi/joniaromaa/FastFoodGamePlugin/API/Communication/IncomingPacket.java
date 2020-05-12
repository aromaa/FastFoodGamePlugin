package fi.joniaromaa.FastFoodGamePlugin.API.Communication;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IncomingPacket
{
	void handle(ChannelHandlerContext ctx, ByteBuf buffer);
}
