package fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming;

import com.eu.habbo.messages.incoming.MessageHandler;

import fi.joniaromaa.FastFoodGamePlugin.Outgoing.AccountGameStatusComposer;

public class GetAccountGameStatusIncomingPacket extends MessageHandler
{
	@Override
	public void handle() throws Exception
	{
		int gameId = this.packet.readInt();
		
        this.client.sendResponse(new AccountGameStatusComposer(gameId));
	}
}
