package fi.joniaromaa.FastFoodGamePlugin.Utils;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.IncomingPacket;

public interface IncomingPacketRunnable
{
	void run(IncomingPacket packet);
}
