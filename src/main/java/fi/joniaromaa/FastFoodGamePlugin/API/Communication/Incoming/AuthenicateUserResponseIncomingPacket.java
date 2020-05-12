package fi.joniaromaa.FastFoodGamePlugin.API.Communication.Incoming;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.IncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

public class AuthenicateUserResponseIncomingPacket implements IncomingPacket
{
	@Getter private boolean result;
	@Getter private String sessionToken;
	
	@Override
	public void handle(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		if (this.result = buffer.readBoolean())
		{
			this.sessionToken = StringUtils.readString(buffer);
		}
	}
}
