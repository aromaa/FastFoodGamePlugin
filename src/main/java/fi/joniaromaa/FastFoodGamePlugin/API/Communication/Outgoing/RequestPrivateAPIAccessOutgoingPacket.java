package fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing;

import com.google.common.base.Preconditions;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.OutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RequestPrivateAPIAccessOutgoingPacket implements OutgoingPacket
{
	private static final short HEADER_ID = 24;
	
	private final String key;
	private final String sign;
	
	public RequestPrivateAPIAccessOutgoingPacket(String key, String sign)
	{
		Preconditions.checkNotNull(key, "Key may not be null!");
		Preconditions.checkNotNull(sign, "Sign may not be null!");
		
		Preconditions.checkArgument(key.length() >= 1, "Key has to be atleast one char long!");
		Preconditions.checkArgument(key.length() <= 64, "Key can not be longer then 64 chars!");
		Preconditions.checkArgument(sign.length() >= 32, "Sign has to be atlest 32 chars long!");
		Preconditions.checkArgument(sign.length() <= 258, "Sign can not be longer thne 258 chars!");
		
		this.key = key;
		this.sign = sign;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeShortLE(RequestPrivateAPIAccessOutgoingPacket.HEADER_ID);
		StringUtils.writeCharSequence(buffer, this.key);
		StringUtils.writeCharSequence(buffer, this.sign);
		return buffer;
	}
}
