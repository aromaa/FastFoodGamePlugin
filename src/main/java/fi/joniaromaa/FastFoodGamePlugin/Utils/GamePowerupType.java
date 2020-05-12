package fi.joniaromaa.FastFoodGamePlugin.Utils;

import lombok.Getter;

public enum GamePowerupType
{
	PARACHUTE(0),
	MISSILE(1),
	SHIELD(2);
	
	@Getter private final int id;
	
	private GamePowerupType(int id)
	{
		this.id = id;
	}
}
