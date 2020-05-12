package fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.OutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Utils.GamePowerupType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class UpdateUserPowerupOutgoingPacket implements OutgoingPacket
{
	private static final short HEADER_ID = 3;
	
	private final int userId;
	private final GamePowerupType type;
	private final int amount;
	
	public UpdateUserPowerupOutgoingPacket(int userId, GamePowerupType type, int amount)
	{
		this.userId = userId;
		this.type = type;
		this.amount = amount;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeShortLE(UpdateUserPowerupOutgoingPacket.HEADER_ID);
		buffer.writeIntLE(this.userId);
		buffer.writeByte(this.type.getId());
		buffer.writeIntLE(this.amount);
		return buffer;
	}
}
