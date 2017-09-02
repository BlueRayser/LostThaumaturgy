package com.pengu.lostthaumaturgy.api.research;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.net.PacketUpdateClientRD;

@MCFBus
public class ResearchSystem
{
	public static final HashMap<String, ArrayList<String>> COMPLETED = new HashMap<>();
	
	public static ArrayList<String> getResearchForPlayerSafe(String playername)
	{
		return COMPLETED.get(playername);
	}
	
	public static ArrayList<String> getResearchForPlayer(String playername)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		ArrayList<String> out = getResearchForPlayerSafe(playername);
		try
		{
			if(out == null && LostThaumaturgy.proxy.getClientWorld() == null && server != null)
			{
				COMPLETED.put(playername, out = new ArrayList());
				UUID id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playername).getBytes(Charsets.UTF_8));
				EntityPlayerMP entityplayermp = new EntityPlayerMP(server, server.getWorld(0), new GameProfile(id, playername), new PlayerInteractionManager(server.getWorld(0)));
				if(entityplayermp != null)
				{
					IPlayerFileData playerNBTManagerObj = server.getWorld(0).getSaveHandler().getPlayerNBTManager();
					SaveHandler sh = (SaveHandler) playerNBTManagerObj;
					Field f = SaveHandler.class.getDeclaredFields()[2];
					f.setAccessible(true);
					File dir = (File) f.get(sh);
					File file1 = new File(dir, id + ".lt");
					File file2 = new File(dir, id + ".ltbak");
					ResearchManager.loadPlayerData((EntityPlayer) entityplayermp, file1, file2, false);
				}
				out = getResearchForPlayerSafe(playername);
			}
		} catch(Exception e)
		{
			// empty catch block
		}
		return out;
	}
	
	@SubscribeEvent
	public void playerJoin(PlayerLoggedInEvent evt)
	{
		getResearchForPlayer(evt.player.getName());
		
		for(int i = 0; i < 32; ++i)
			try
			{
				if(evt.player instanceof EntityPlayerMP)
					HCNetwork.manager.sendTo(getPacketFor(evt.player), (EntityPlayerMP) evt.player);
			} catch(Throwable err)
			{
			}
	}
	
	@SubscribeEvent
	public void playerLeave(PlayerLoggedOutEvent evt)
	{
		UUID id = evt.player.getGameProfile().getId();
		
		MinecraftServer server;
		if((server = evt.player.getServer()) != null)
		{
			try
			{
				IPlayerFileData playerNBTManagerObj = server.getWorld(0).getSaveHandler().getPlayerNBTManager();
				SaveHandler sh = (SaveHandler) playerNBTManagerObj;
				Field f = SaveHandler.class.getDeclaredFields()[2];
				f.setAccessible(true);
				File dir = (File) f.get(sh);
				File file1 = new File(dir, id + ".lt");
				File file2 = new File(dir, id + ".ltbak");
				
				ResearchManager.savePlayerData(evt.player, file1, file2);
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
	}
	
	public static PacketUpdateClientRD getPacketFor(EntityPlayer player)
	{
		return new PacketUpdateClientRD(getResearchForPlayer(player.getName()));
	}
	
	public static void setResearchCompleted(EntityPlayer player, ResearchItem res, boolean isCompleted)
	{
		ArrayList<String> researches = COMPLETED.getOrDefault(player.getName(), new ArrayList<>());
		COMPLETED.put(player.getName(), researches);
		
		if(isCompleted)
			researches.add(res.key);
		else
			researches.remove(res.key);
	}
	
	public static boolean isResearchCompleted(EntityPlayer player, ResearchItem res)
	{
		if(res == null)
			return true;
		ArrayList<String> researches = COMPLETED.getOrDefault(player.getName(), new ArrayList<>());
		COMPLETED.put(player.getName(), researches);
		return researches != null && researches.contains(res.key);
	}
}