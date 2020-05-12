package fi.joniaromaa.FastFoodGamePlugin.Outgoing;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;

public class AccountGameStatusComposer extends MessageComposer
{
	private static final int HEADER_ID = 2893;
	
	private final int gameId;
	
	public AccountGameStatusComposer(int gameId)
	{
		this.gameId = gameId;
	}

	@Override
	public ServerMessage compose()
	{
		this.response.init(AccountGameStatusComposer.HEADER_ID);
		this.response.appendInt32(this.gameId);
		this.response.appendInt32(-1); //players left, below zero is unlimited
		this.response.appendInt32(0); //some promo stuff, looks like to be used as boolean
		return this.response;
	}
}
