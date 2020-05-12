package fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.OutgoingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class UpdateUserCreditsOutgoingPacket implements OutgoingPacket
{
	private static final short HEADER_ID = 9;
	
	private final int userId;
	private final int credits;
	
	public UpdateUserCreditsOutgoingPacket(int userId, int credits)
	{
		this.userId = userId;
		this.credits = credits;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeShortLE(UpdateUserCreditsOutgoingPacket.HEADER_ID);
		buffer.writeIntLE(this.userId);
		buffer.writeIntLE(this.credits);
		return buffer;
	}
}
