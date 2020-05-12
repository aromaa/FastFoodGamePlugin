package fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing;

import java.util.Map.Entry;

import fi.joniaromaa.FastFoodGamePlugin.API.Communication.OutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Utils.GamePowerup;
import fi.joniaromaa.FastFoodGamePlugin.Utils.HotelSettings;
import fi.joniaromaa.FastFoodGamePlugin.Utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class UpdateHotelSettingsOutgoingPacket implements OutgoingPacket
{
	private static final short HEADER_ID = 12;
	
	private final HotelSettings settings;
	
	public UpdateHotelSettingsOutgoingPacket(HotelSettings settings)
	{
		this.settings = settings;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeShortLE(UpdateHotelSettingsOutgoingPacket.HEADER_ID);
		
		if (this.settings != null)
		{
			if (this.settings.getTexts() != null)
			{
				buffer.writeByte(this.settings.getTexts().size());
				
				for(Entry<String, String> text : this.settings.getTexts().entrySet())
				{
					StringUtils.writeCharSequence(buffer, text.getKey());
					StringUtils.writeCharSequence(buffer, text.getValue());
				}
			}
			else
			{
				buffer.writeByte(0);
			}
			
			if (this.settings.getPowerups() != null)
			{
				buffer.writeByte(this.settings.getPowerups().size());
				
				for(GamePowerup powerup : this.settings.getPowerups())
				{
					StringUtils.writeCharSequence(buffer, powerup.getPackageName());
					buffer.writeByte(powerup.getType().getId());
					buffer.writeIntLE(powerup.getAmount());
					buffer.writeIntLE(powerup.getCost());
				}
			}
			else
			{
				buffer.writeByte(0);
			}
		}
		else
		{
			buffer.writeByte(0); //texts
			buffer.writeByte(0); //powerups
		}
		return buffer;
	}
}
