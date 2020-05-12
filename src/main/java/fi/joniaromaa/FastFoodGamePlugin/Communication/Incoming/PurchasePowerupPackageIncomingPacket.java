package fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.IncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

public class PurchasePowerupPackageIncomingPacket implements IncomingPacket
{
	@Getter private int userId;
	@Getter private String packageName;
	
	@Override
	public void handle(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.userId = buffer.readIntLE();
		this.packageName = StringUtils.readString(buffer);
	}
}
