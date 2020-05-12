package fi.joniaromaa.FastFoodGamePlugin.API;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.OutgoingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class OutgoingPacketsEncoder extends MessageToByteEncoder<OutgoingPacket>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, OutgoingPacket outgoing, ByteBuf buffer) throws Exception
	{
		ByteBuf bytes = outgoing.getBytes();
		
		buffer.writeShortLE(bytes.readableBytes());
		buffer.writeBytes(bytes);
	}
}
