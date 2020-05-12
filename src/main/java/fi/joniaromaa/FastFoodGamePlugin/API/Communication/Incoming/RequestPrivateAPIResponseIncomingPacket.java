package fi.joniaromaa.FastFoodGamePlugin.API.Communication.Incoming;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.IncomingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

public class RequestPrivateAPIResponseIncomingPacket implements IncomingPacket
{
	@Getter private boolean success;
	
	@Override
	public void handle(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.success = buffer.readBoolean();
	}
}
