package fi.joniaromaa.FastFoodGamePlugin.Utils;

import lombok.Getter;

public class GamePowerup
{
	@Getter private final String packageName;
	@Getter private final GamePowerupType type;
	@Getter private final int amount;
	@Getter private final int cost;
	
	public GamePowerup(String packageName, GamePowerupType type, int amount, int cost)
	{
		this.packageName = packageName;
		this.type = type;
		this.amount = amount;
		this.cost = cost;
	}
}
