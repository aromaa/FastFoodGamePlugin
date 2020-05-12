package fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming;

import com.eu.habbo.messages.incoming.MessageHandler;

import fi.joniaromaa.FastFoodGamePlugin.Outgoing.GamesComposer;

public class GetGamesIncomingPacket extends MessageHandler
{
	@Override
	public void handle() throws Exception
	{
        this.client.sendResponse(new GamesComposer());
	}
}
