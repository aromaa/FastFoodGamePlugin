package fi.joniaromaa.FastFoodGamePlugin.API.Communication;

import io.netty.buffer.ByteBuf;

public interface OutgoingPacket
{
	ByteBuf getBytes();
}
