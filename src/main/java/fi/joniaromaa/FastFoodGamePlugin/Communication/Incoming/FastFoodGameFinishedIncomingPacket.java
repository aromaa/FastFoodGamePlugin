package fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.IncomingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

public class FastFoodGameFinishedIncomingPacket implements IncomingPacket
{
	@Getter private int userId;
	@Getter private boolean won;
	@Getter private int parachutesUsed;
	@Getter private int missilesUsed;
	@Getter private int shildsUsed;
	
	@Override
	public void handle(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.userId = buffer.readIntLE();
		this.won = buffer.readBoolean();
		this.parachutesUsed = buffer.readIntLE();
		this.missilesUsed = buffer.readIntLE();
		this.shildsUsed = buffer.readIntLE();
	}
}
