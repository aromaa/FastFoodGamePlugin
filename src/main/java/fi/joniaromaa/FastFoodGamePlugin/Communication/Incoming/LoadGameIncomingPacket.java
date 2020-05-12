package fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming;

import com.eu.habbo.messages.incoming.MessageHandler;

import fi.joniaromaa.FastFoodGamePlugin.FastFoodGamePlugin;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.IncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.Incoming.AuthenicateUserResponseIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing.AuthenicateUserOutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Outgoing.GameButtonStatusComposer;
import fi.joniaromaa.FastFoodGamePlugin.Outgoing.LeaveGameQueueComposer;
import fi.joniaromaa.FastFoodGamePlugin.Outgoing.LoadGameComposer;
import fi.joniaromaa.FastFoodGamePlugin.Utils.AttributeUtils;
import fi.joniaromaa.FastFoodGamePlugin.Utils.IncomingPacketRunnable;

public class LoadGameIncomingPacket extends MessageHandler
{
	@Override
	public void handle() throws Exception
	{
		int gameId = this.packet.readInt();
		
		if (gameId == 3)
		{
			FastFoodGamePlugin.getAPIConnection().getActiveConnection().registerRunnable(AuthenicateUserResponseIncomingPacket.class, new IncomingPacketRunnable()
			{
				@Override
				public void run(IncomingPacket packet)
				{
					FastFoodGamePlugin.getAPIConnection().getActiveConnection().unregisterRunnable(AuthenicateUserResponseIncomingPacket.class, this);
					
					AuthenicateUserResponseIncomingPacket incoming = (AuthenicateUserResponseIncomingPacket)packet;
					if (incoming.isResult())
					{
						LoadGameIncomingPacket.this.client.sendResponse(new LoadGameComposer(gameId, incoming.getSessionToken()));
						
						LoadGameIncomingPacket.this.client.getChannel().attr(AttributeUtils.PLAYING_FASTFOOD).set(true);
					}
					else
					{
						LoadGameIncomingPacket.this.client.sendResponse(new LeaveGameQueueComposer(gameId));
						LoadGameIncomingPacket.this.client.sendResponse(new GameButtonStatusComposer(gameId));
					}
				}
			});

			FastFoodGamePlugin.getAPIConnection().getActiveConnection().getChannel().writeAndFlush(new AuthenicateUserOutgoingPacket(FastFoodGamePlugin.getFastFoodManager().getOrCreate(this.client)));
		}
	}
}
