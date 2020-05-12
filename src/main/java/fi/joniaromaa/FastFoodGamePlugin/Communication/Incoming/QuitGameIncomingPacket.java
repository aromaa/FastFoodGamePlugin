package fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming;

import com.eu.habbo.messages.incoming.MessageHandler;

import fi.joniaromaa.FastFoodGamePlugin.Outgoing.GameButtonStatusComposer;
import fi.joniaromaa.FastFoodGamePlugin.Utils.AttributeUtils;

public class QuitGameIncomingPacket extends MessageHandler
{
	@Override
	public void handle() throws Exception
	{
		int gameId = this.packet.readInt();
		
		this.client.sendResponse(new GameButtonStatusComposer(gameId));
		
		this.client.getChannel().attr(AttributeUtils.PLAYING_FASTFOOD).set(null);
	}
}
