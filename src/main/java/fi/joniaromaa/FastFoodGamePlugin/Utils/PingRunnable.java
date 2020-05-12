package fi.joniaromaa.FastFoodGamePlugin.Utils;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.messages.outgoing.handshake.PongComposer;

public class PingRunnable implements Runnable
{
	@Override
	public void run()
	{
		for(GameClient client : Emulator.getGameServer().getGameClientManager().getSessions().values())
		{
			if (client.getChannel().hasAttr(AttributeUtils.PLAYING_FASTFOOD))
			{
				client.sendResponse(new PongComposer(-1));
			}
		}
		
		Emulator.getThreading().run(this, 20L * 1000L);
	}
}
