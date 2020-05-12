package fi.joniaromaa.FastFoodGamePlugin.Outgoing;

import java.util.concurrent.atomic.AtomicInteger;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;

import fi.joniaromaa.FastFoodGamePlugin.FastFoodGamePlugin;

public class LoadGameComposer extends MessageComposer
{
	private static final int HEADER_ID = 3654;
	private static final AtomicInteger GameClientID = new AtomicInteger(0);
	
	private final int gameId;
	private final String accessToken;
	
	public LoadGameComposer(int gameId, String accessToken)
	{
		this.gameId = gameId;
		this.accessToken = accessToken;
	}
	
	@Override
	public ServerMessage compose()
	{
		this.response.init(LoadGameComposer.HEADER_ID);
		this.response.appendInt32(gameId);
		this.response.appendString(LoadGameComposer.GameClientID.getAndIncrement() + "");
		this.response.appendString(FastFoodGamePlugin.getPluginConfig().getString("game.swf.url")); //swf url
		this.response.appendString("best"); //quality
		this.response.appendString("showAll"); //scale mode
		this.response.appendInt32(60); //fps
		this.response.appendInt32(10); //flash major version min
		this.response.appendInt32(0); //flash minor version min
		
		this.response.appendInt32(6);
		this.response.appendString("habboHost");
		this.response.appendString(FastFoodGamePlugin.getPluginConfig().getString("game.host"));
		
		this.response.appendString("accessToken");
		this.response.appendString(this.accessToken);
		
		this.response.appendString("assetUrl");
		this.response.appendString(FastFoodGamePlugin.getPluginConfig().getString("game.swf.asset.url"));
		
		this.response.appendString("gameServerHost");
		this.response.appendString(FastFoodGamePlugin.getPluginConfig().getString("game.server.host"));
		
		this.response.appendString("gameServerPort");
		this.response.appendString(FastFoodGamePlugin.getPluginConfig().getString("game.server.tcp.port"));
		
		this.response.appendString("socketPolicyPort");
		this.response.appendString(FastFoodGamePlugin.getPluginConfig().getString("game.server.policy.port"));
		return this.response;
	}
}
