package fi.joniaromaa.FastFoodGamePlugin.Outgoing;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;

import fi.joniaromaa.FastFoodGamePlugin.FastFoodGamePlugin;

public class GamesComposer extends MessageComposer
{
	private static final int HEADER_ID = 222;
	
	@Override
	public ServerMessage compose()
	{
		this.response.init(GamesComposer.HEADER_ID);
		this.response.appendInt32(1); //count
		this.response.appendInt32(3); //id?
		this.response.appendString("basejump"); //name
        this.response.appendString("68bbd2"); //bacground color
        this.response.appendString(""); //text color
        this.response.appendString(FastFoodGamePlugin.getPluginConfig().getString("hotel.c_images.folder"));
        this.response.appendString(""); //idk
		return this.response;
	}
}
