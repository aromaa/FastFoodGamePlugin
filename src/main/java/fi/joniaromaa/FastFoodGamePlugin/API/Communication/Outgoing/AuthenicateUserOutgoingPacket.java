package fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing;

import com.google.common.base.Preconditions;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.OutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Utils.FastFoodUser;
import fi.joniaromaa.FastFoodGamePlugin.Utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class AuthenicateUserOutgoingPacket implements OutgoingPacket
{
	private static final short HEADER_ID = 11;
	
	private final FastFoodUser user;
	
	public AuthenicateUserOutgoingPacket(FastFoodUser user)
	{
		Preconditions.checkNotNull(user, "User can not be null!");
		
		this.user = user;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeShortLE(AuthenicateUserOutgoingPacket.HEADER_ID);
		buffer.writeIntLE(user.getId());
		StringUtils.writeCharSequence(buffer, user.getUsername());
		StringUtils.writeCharSequence(buffer, user.getFigure());
		StringUtils.writeCharSequence(buffer, user.getGender());
		
		if (user.getBadges() != null)
		{
			buffer.writeByte(user.getBadges().size());
			
			for(String badge : user.getBadges())
			{
				StringUtils.writeCharSequence(buffer, badge);
			}
		}
		else
		{
			buffer.writeByte(0);
		}
		
		buffer.writeIntLE(user.getMissiles());
		buffer.writeIntLE(user.getParachutes());
		buffer.writeIntLE(user.getShilds());
		buffer.writeIntLE(user.getCredits());
		return buffer;
	}
}
