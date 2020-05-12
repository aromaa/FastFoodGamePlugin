package fi.joniaromaa.FastFoodGamePlugin.Outgoing;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;

public class LeaveGameQueueComposer extends MessageComposer
{
	private static final int HEADER_ID = 1477;
	
	private final int gameId;
	
	public LeaveGameQueueComposer(int gameId)
	{
		this.gameId = gameId;
	}
	
	
	@Override
	public ServerMessage compose()
	{
		this.response.init(LeaveGameQueueComposer.HEADER_ID);
		this.response.appendInt32(this.gameId);
		return this.response;
	}
}
