package com.pengu.lostthaumaturgy.custom.research;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import com.mrdimka.hammercore.annotations.MCFBus;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.net.PacketUpdateClientRD;

@MCFBus
public class ResearchSystem
{
	private static final HashMap<String, HashSet<String>> COMPLETED = new HashMap<>();
	
	@SubscribeEvent
	public void playerJoin(PlayerLoggedInEvent evt)
	{
		HashSet<String> loaded;
		try
		{
			ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(getResearchSaveFile(evt.player))));
			loaded = (HashSet<String>) in.readObject();
			in.close();
		} catch(Throwable err)
		{
			loaded = new HashSet<>();
		}
		COMPLETED.put(evt.player.getGameProfile().getId().toString(), loaded);
	}
	
	@SubscribeEvent
	public void playerLeave(PlayerLoggedOutEvent evt)
	{
		try
		{
			HashSet<String> researches = COMPLETED.remove(evt.player.getGameProfile().getId().toString());
			ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(getResearchSaveFile(evt.player))));
			out.writeObject(researches);
			out.close();
		} catch(Throwable err)
		{
			LostThaumaturgy.LOG.bigWarn("Failed to save data for " + evt.player.getGameProfile().getName() + ": " + err.getMessage());
			err.printStackTrace();
		}
	}
	
	public static PacketUpdateClientRD getPacketFor(EntityPlayer player)
	{
		return new PacketUpdateClientRD(COMPLETED.get(player.getGameProfile().getId().toString()));
	}
	
	public static void setResearchCompleted(EntityPlayer player, Research res, boolean isCompleted)
	{
		HashSet<String> researches = COMPLETED.get(player.getGameProfile().getId().toString());
		if(isCompleted)
			researches.add(res.uid);
		else
			researches.remove(res.uid);
	}
	
	public static boolean isResearchCompleted(EntityPlayer player, Research res)
	{
		HashSet<String> researches = COMPLETED.get(player.getGameProfile().getId().toString());
		return researches.contains(res.uid);
	}
	
	public static File getResearchSaveFile(EntityPlayer player)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		File lt = new File((server.isDedicatedServer() ? "" : "saves" + File.separator) + server.getFolderName(), "pengu-lt");
		if(!lt.isDirectory())
			lt.mkdir();
		File folder = new File(lt, "research");
		if(!folder.isDirectory())
			folder.mkdir();
		return new File(folder, player.getGameProfile().getId() + ".lt");
	}
}