package fi.joniaromaa.FastFoodGamePlugin.API;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.Habbo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import fi.joniaromaa.FastFoodGamePlugin.FastFoodGamePlugin;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.IncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.Incoming.AuthenicateUserResponseIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.Incoming.RequestPrivateAPIResponseIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing.RequestPrivateAPIAccessOutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing.UpdateHotelSettingsOutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing.UpdateUserCreditsOutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.API.Communication.Outgoing.UpdateUserPowerupOutgoingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming.FastFoodGameFinishedIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Communication.Incoming.PurchasePowerupPackageIncomingPacket;
import fi.joniaromaa.FastFoodGamePlugin.Utils.FastFoodUser;
import fi.joniaromaa.FastFoodGamePlugin.Utils.GamePowerup;
import fi.joniaromaa.FastFoodGamePlugin.Utils.HotelSettings;
import fi.joniaromaa.FastFoodGamePlugin.Utils.IncomingPacketRunnable;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;

public class APIConnectionHandler extends SimpleChannelInboundHandler<ByteBuf>
{
	private static final HashMap<Short, Class<? extends IncomingPacket>> incomingPackets = new HashMap<>();
	
	static
	{
		APIConnectionHandler.incomingPackets.put((short)14, RequestPrivateAPIResponseIncomingPacket.class);
		APIConnectionHandler.incomingPackets.put((short)65, AuthenicateUserResponseIncomingPacket.class);
		APIConnectionHandler.incomingPackets.put((short)17, PurchasePowerupPackageIncomingPacket.class);
		APIConnectionHandler.incomingPackets.put((short)51, FastFoodGameFinishedIncomingPacket.class);
	}
	
	private Multimap<Class<? extends IncomingPacket>, IncomingPacketRunnable> runnables = ArrayListMultimap.create();
	@Getter private Channel channel; //Not sure if this is "safe" as I have never done this nor needed
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception
	{
		short header = msg.readShortLE();
		
		IncomingPacket incoming = APIConnectionHandler.incomingPackets.getOrDefault(header, null).newInstance();
		if (incoming != null)
		{
			incoming.handle(ctx, msg);
			
			for (IncomingPacketRunnable runnable : this.runnables.get(incoming.getClass()).toArray(new IncomingPacketRunnable[0]))
			{
				runnable.run(incoming);
			}
		}
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
		this.channel = ctx.channel();
		
		this.registerRunnable(RequestPrivateAPIResponseIncomingPacket.class, new IncomingPacketRunnable()
		{
			@Override
			public void run(IncomingPacket packet)
			{
				APIConnectionHandler.this.unregisterRunnable(RequestPrivateAPIResponseIncomingPacket.class, this);
				
				RequestPrivateAPIResponseIncomingPacket incoming = (RequestPrivateAPIResponseIncomingPacket)packet;
				
				if (incoming.isSuccess())
				{
					ctx.channel().writeAndFlush(new UpdateHotelSettingsOutgoingPacket(new HotelSettings(FastFoodGamePlugin.getTexts(), new ArrayList<GamePowerup>(FastFoodGamePlugin.getPowerups().values()))));
					
					if (!FastFoodGamePlugin.getPluginConfig().getBoolean("hotel.unlimited.powerups"))
					{
						APIConnectionHandler.this.registerRunnable(PurchasePowerupPackageIncomingPacket.class, new IncomingPacketRunnable()
						{
							@Override
							public void run(IncomingPacket packet)
							{
								PurchasePowerupPackageIncomingPacket incoming = (PurchasePowerupPackageIncomingPacket)packet;
	
								GamePowerup powerup = FastFoodGamePlugin.getPowerups().get(incoming.getPackageName());
								if (powerup != null)
								{
									Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(incoming.getUserId());
									if (habbo != null)
									{
										FastFoodUser user = FastFoodGamePlugin.getFastFoodManager().get(incoming.getUserId());
										if (user != null)
										{
											if (habbo.getHabboInfo().getCredits() >= powerup.getCost())
											{
												habbo.giveCredits(-powerup.getCost());
												
												user.setCredits(habbo.getHabboInfo().getCredits());
												
												PreparedStatement statement = null;
												
												try
												{
													switch (powerup.getType())
													{
														case MISSILE:
															{
																user.setMissiles(user.getMissiles() + powerup.getAmount());
																
																statement = Emulator.getDatabase().prepare("INSERT INTO users_fastfood(user_id, missiles) VALUES(?, ?) ON DUPLICATE KEY UPDATE missiles = missiles + VALUES(missiles)");
																statement.setInt(1, user.getId());
																statement.setInt(2, powerup.getAmount());
																statement.execute();
																
																APIConnectionHandler.this.channel.writeAndFlush(new UpdateUserPowerupOutgoingPacket(user.getId(), powerup.getType(), user.getMissiles()));
															}
															break;
														case SHIELD:
															{
																user.setShilds(user.getShilds() + powerup.getAmount());
																
																statement = Emulator.getDatabase().prepare("INSERT INTO users_fastfood(user_id, shields) VALUES(?, ?) ON DUPLICATE KEY UPDATE shields = shields + VALUES(shields)");
																statement.setInt(1, user.getId());
																statement.setInt(2, powerup.getAmount());
																statement.execute();
																
																APIConnectionHandler.this.channel.writeAndFlush(new UpdateUserPowerupOutgoingPacket(user.getId(), powerup.getType(), user.getShilds()));
															}
															break;
														case PARACHUTE:
															{
																user.setParachutes(user.getParachutes() + powerup.getAmount());
																
																statement = Emulator.getDatabase().prepare("INSERT INTO users_fastfood(user_id, parachutes) VALUES(?, ?) ON DUPLICATE KEY UPDATE parachutes = parachutes + VALUES(parachutes)");
																statement.setInt(1, user.getId());
																statement.setInt(2, powerup.getAmount());
																statement.execute();
																
																APIConnectionHandler.this.channel.writeAndFlush(new UpdateUserPowerupOutgoingPacket(user.getId(), powerup.getType(), user.getParachutes()));
															}
															break;
													}
													
													APIConnectionHandler.this.channel.writeAndFlush(new UpdateUserCreditsOutgoingPacket(user.getId(), habbo.getHabboInfo().getCredits()));
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
														catch(Exception ex)
														{
															
														}
													}
												}
											}
										}
									}
								}
							}
						});
					}
						
					APIConnectionHandler.this.registerRunnable(FastFoodGameFinishedIncomingPacket.class, new IncomingPacketRunnable()
					{
						@Override
						public void run(IncomingPacket packet)
						{
							FastFoodGameFinishedIncomingPacket incoming = (FastFoodGameFinishedIncomingPacket)packet;

							PreparedStatement statement = null;
							try
							{
								if (!FastFoodGamePlugin.getPluginConfig().getBoolean("hotel.unlimited.powerups"))
								{
									statement = Emulator.getDatabase().prepare("INSERT INTO users_fastfood(user_id, plays, wins) VALUES(?, 1, ?) ON DUPLICATE KEY UPDATE plays = plays + 1, wins = wins + ?, missiles = missiles - ?, parachutes = parachutes - ?, shields = shields - ?");
									statement.setInt(1, incoming.getUserId());
									statement.setInt(2, incoming.isWon() ? 1 : 0);
									statement.setInt(3, incoming.isWon() ? 1 : 0);
									statement.setInt(4, incoming.getMissilesUsed());
									statement.setInt(5, incoming.getParachutesUsed());
									statement.setInt(6, incoming.getShildsUsed());
									statement.execute();
								}
								else
								{
									statement = Emulator.getDatabase().prepare("INSERT INTO users_fastfood(user_id, plays, wins) VALUES(?, 1, ?) ON DUPLICATE KEY UPDATE plays = plays + 1, wins = wins + ?");
									statement.setInt(1, incoming.getUserId());
									statement.setInt(2, incoming.isWon() ? 1 : 0);
									statement.setInt(3, incoming.isWon() ? 1 : 0);
									statement.execute();
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
									catch(Exception ex)
									{
										
									}
								}
							}
						}
					});
				}
			}
		});
		
		ctx.channel().writeAndFlush(new RequestPrivateAPIAccessOutgoingPacket(FastFoodGamePlugin.getPluginConfig().getString("hotel.name"), FastFoodGamePlugin.getPluginConfig().getString("hotel.sign")));
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception
	{
		ctx.channel().eventLoop().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				FastFoodGamePlugin.tryConnectToAPIServer();
			}
		}, 5, TimeUnit.SECONDS);
	}
	
	public void registerRunnable(Class<? extends IncomingPacket> packet, IncomingPacketRunnable runnable)
	{
		this.runnables.put(packet,  runnable);
	}
	
	public void unregisterRunnable(Class<? extends IncomingPacket> packet, IncomingPacketRunnable runnable)
	{
		this.runnables.remove(packet, runnable);
	}
}
