package fi.joniaromaa.FastFoodGamePlugin.Utils;

import java.util.List;

import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboInfo;

import lombok.Getter;
import lombok.Setter;

public class FastFoodUser
{
	@Getter private final int id;
	@Getter @Setter private String username;
	@Getter @Setter private String figure;
	@Getter @Setter private String gender;
	@Getter @Setter private List<String> badges;
	
	@Getter @Setter private int missiles;
	@Getter @Setter private int parachutes;
	@Getter @Setter private int shilds;
	@Getter @Setter private int credits;
	
	public FastFoodUser(int id, String username, String figure, String gender, List<String> badges, int missiles, int parachutes, int shilds, int credits)
	{
		this.id = id;
		this.username = username;
		this.figure = figure;
		this.gender = gender;
		this.badges = badges;
		
		this.missiles = missiles;
		this.parachutes = parachutes;
		this.shilds = shilds;
		this.credits = credits;
	}
	
	public void updateHabboInfo(GameClient client)
	{
		this.updateHabboInfo(client.getHabbo());
	}
	
	public void updateHabboInfo(Habbo habbo)
	{
		this.updateHabboInfo(habbo.getHabboInfo());
	}
	
	public void updateHabboInfo(HabboInfo info)
	{
		this.username = info.getUsername();
		this.figure = info.getLook();
		this.gender = info.getGender().name();
		
		this.credits = info.getCredits();
	}
}
