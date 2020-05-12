package fi.joniaromaa.FastFoodGamePlugin.Utils;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class HotelSettings
{
	@Getter @Setter private HashMap<String, String> texts;
	@Getter @Setter private List<GamePowerup> powerups;
	
	public HotelSettings(HashMap<String, String> texts, List<GamePowerup> powerups)
	{
		this.texts = texts;
		this.powerups = powerups;
	}
}
