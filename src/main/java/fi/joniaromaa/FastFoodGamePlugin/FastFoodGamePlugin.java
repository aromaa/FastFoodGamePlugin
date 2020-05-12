package fi.joniaromaa.FastFoodGamePlugin;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.EventListener;
import com.eu.habbo.plugin.HabboPlugin;
import com.eu.habbo.plugin.events.emulator.EmulatorLoadedEvent;

import fi.joniaromaa.FastFoodGamePlugin.API.APIConnection;
import fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming.GetAccountGameStatusIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming.GetGamesIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming.LoadGameIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming.QuitGameIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Managers.FastFoodManager;
import fi.joniaromaa.FastFoodGamePlugin.Utils.GamePowerup;
import fi.joniaromaa.FastFoodGamePlugin.Utils.GamePowerupType;
import fi.joniaromaa.FastFoodGamePlugin.Utils.PingRunnable;
import fi.joniaromaa.FastFoodGamePlugin.Utils.PluginConfig;
import gnu.trove.map.hash.THashMap;
import lombok.Getter;

public class FastFoodGamePlugin extends HabboPlugin implements EventListener
{
	private static final Integer GET_GAMES = 741;
	private static final Integer GET_ACCOUNT_GAME_STATUS = 11;
	private static final Integer LOAD_GAME = 1458;
	private static final Integer QUIT_GAME = 3207;
	
	@Getter private static PluginConfig pluginConfig;
	@Getter private static APIConnection APIConnection;
	@Getter private static FastFoodManager fastFoodManager;
	@Getter private static HashMap<String, String> texts = new HashMap<String, String>();
	@Getter private static HashMap<String, GamePowerup> powerups = new HashMap<String, GamePowerup>();
	
	@Override
	public void onEnable()
	{
		try
		{
			FastFoodGamePlugin.pluginConfig = new PluginConfig(Paths.get(System.getProperty("user.dir"), "fastfood.ini"));
			
			PreparedStatement statement = null;
			try
			{
				statement = Emulator.getDatabase().prepare("SELECT `key`, value FROM emulator_texts WHERE `key` LIKE 'basejump%'");
				
				try(ResultSet result = statement.executeQuery())
				{
					while (result.next())
					{
						FastFoodGamePlugin.texts.put(result.getString("key"), result.getString("value"));
					}
				}
			}
			finally
			{
				if (statement != null)
				{
					statement.close();
					statement.getConnection().close();
				}
			}
			
			if (!FastFoodGamePlugin.getPluginConfig().getBoolean("hotel.unlimited.powerups"))
			{
				try
				{
					statement = Emulator.getDatabase().prepare("SELECT package_name, type, amount, cost FROM fastfood_settings");
							 
					try(ResultSet result = statement.executeQuery())
					{
						while (result.next())
						{
							GamePowerupType type = GamePowerupType.MISSILE;
							
							switch (result.getString("type"))
							{
								case "missile":
									type = GamePowerupType.MISSILE;
									break;
								case "shield":
									type = GamePowerupType.SHIELD;
									break;
								case "parachute":
									type = GamePowerupType.PARACHUTE;
									break;
							}
							
							FastFoodGamePlugin.powerups.put(result.getString("package_name"), new GamePowerup(result.getString("package_name"), type, result.getInt("amount"), result.getInt("cost")));
						}
					}
				}
				finally
				{
					if (statement != null)
					{
						statement.close();
						statement.getConnection().close();
					}
				}
			}
			
			FastFoodGamePlugin.tryConnectToAPIServer();
			
			FastFoodGamePlugin.fastFoodManager = new FastFoodManager();
			
	        Emulator.getPluginManager().registerEvents(this, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable()
	{
		FastFoodGamePlugin.APIConnection.shutdown();
	}
	
	@Override
	public boolean hasPermission(Habbo habbo, String node)
	{
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public static void onEmulatorLoadedEvent(EmulatorLoadedEvent event)
	{
		try
		{
			Field field = Emulator.getGameServer().getPacketManager().getClass().getDeclaredField("incoming");
			field.setAccessible(true);
			THashMap<Integer, Class<? extends MessageHandler>> incoming = (THashMap<Integer, Class<? extends MessageHandler>>) field.get(Emulator.getGameServer().getPacketManager());
			
			incoming.remove(FastFoodGamePlugin.GET_GAMES);
			incoming.remove(FastFoodGamePlugin.GET_ACCOUNT_GAME_STATUS);
			incoming.remove(FastFoodGamePlugin.LOAD_GAME);
			incoming.remove(FastFoodGamePlugin.QUIT_GAME);
			
			Emulator.getGameServer().getPacketManager().registerHandler(FastFoodGamePlugin.GET_GAMES, GetGamesIncomingPacket.class);
			Emulator.getGameServer().getPacketManager().registerHandler(FastFoodGamePlugin.GET_ACCOUNT_GAME_STATUS, GetAccountGameStatusIncomingPacket.class);
			Emulator.getGameServer().getPacketManager().registerHandler(FastFoodGamePlugin.LOAD_GAME, LoadGameIncomingPacket.class);
			Emulator.getGameServer().getPacketManager().registerHandler(FastFoodGamePlugin.QUIT_GAME, QuitGameIncomingPacket.class);

			Emulator.getThreading().run(new PingRunnable(), 20L * 1000L);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void tryConnectToAPIServer()
	{
		FastFoodGamePlugin.APIConnection = new APIConnection(FastFoodGamePlugin.getPluginConfig().getString("api.host"), FastFoodGamePlugin.getPluginConfig().getInt("api.port"));
	}
}
