package fi.joniaromaa.FastFoodGamePlugin.Managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboInfo;

import fi.joniaromaa.FastFoodGamePlugin.FastFoodGamePlugin;
import fi.joniaromaa.FastFoodGamePlugin.Utils.FastFoodUser;

public class FastFoodManager
{
	private ConcurrentHashMap<Integer, FastFoodUser> users;
	
	public FastFoodManager()
	{
		this.users = new ConcurrentHashMap<>();
	}
	
	public FastFoodUser getOrCreate(GameClient client)
	{
		return this.getOrCreate(client.getHabbo());
	}
	
	public FastFoodUser getOrCreate(Habbo habbo)
	{
		return this.getOrCreate(habbo.getHabboInfo());
	}
	
	public FastFoodUser getOrCreate(HabboInfo info)
	{
		FastFoodUser user = this.users.get(info.getId());
		if (user == null)
		{
			int missiles = 0;
			int parachutes = 0;
			int shields = 0;
			
			if (!FastFoodGamePlugin.getPluginConfig().getBoolean("hotel.unlimited.powerups"))
			{
				PreparedStatement statement = null;
				
				try
				{
					statement = Emulator.getDatabase().prepare("SELECT missiles, parachutes, shields FROM users_fastfood WHERE user_id = '" + info.getId() + "' LIMIT 1");
					
					try (ResultSet result = statement.executeQuery())
					{
						if (result.next())
						{
							missiles = result.getInt("missiles");
							parachutes = result.getInt("parachutes");
							shields = result.getInt("shields");
						}
						else
						{
							if (statement != null)
							{
								try
								{
									statement.close();
									statement.getConnection().close();
								}
								catch (SQLException e)
								{
									
								}
							}
							
							statement = Emulator.getDatabase().prepare("INSERT INTO users_fastfood(user_id) VALUES(?)");
							statement.setInt(1, info.getId());
							statement.execute();
						}
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					if (statement != null)
					{
						try
						{
							statement.close();
							statement.getConnection().close();
						}
						catch (SQLException e)
						{
							
						}
					}
				}
			}
			else
			{
				missiles = 10000;
				parachutes = 10000;
				shields = 10000;
			}
			
			FastFoodUser old = this.users.putIfAbsent(info.getId(), user = new FastFoodUser(info.getId(), info.getUsername(), info.getLook(), info.getGender().name(), null, missiles, parachutes, shields, info.getCredits()));
			if (old != null)
			{
				user = old;
			}
		}
		else
		{
			user.updateHabboInfo(info);
		}
		
		return user;
	}
	
	public FastFoodUser get(int id)
	{
		return this.users.get(id);
	}
}
