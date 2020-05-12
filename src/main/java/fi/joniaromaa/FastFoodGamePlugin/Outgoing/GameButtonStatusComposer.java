package fi.joniaromaa.FastFoodGamePlugin.Outgoing;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;

public class GameButtonStatusComposer extends MessageComposer
{
	private static final int HEADER_ID = 3805;
	
	private final int gameId;
	
	public GameButtonStatusComposer(int gameId)
	{
		this.gameId = gameId;
	}

	@Override
	public ServerMessage compose()
	{
		this.response.init(GameButtonStatusComposer.HEADER_ID);
		this.response.appendInt32(this.gameId);
		this.response.appendInt32(0);
		return this.response;
	}
}
