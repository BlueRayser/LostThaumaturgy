package com.pengu.lostthaumaturgy.emote;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.pengu.hammercore.utils.IndexedMap;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;

public class EmoteManager
{
	private EmoteManager()
	{
	}
	
	public static final class DefaultEmotes
	{
		private DefaultEmotes()
		{
		}
		
		public static final String //
		        QUESTION = "3F", //
		        EXCLAMATION = "21", //
		        OK = "ok", //
		        PAUSE = "paused", //
		        WORKING = "working", //
		        SLEEPING = "z", //
		        WTF = "wtf";
	}
	
	private static final Map<String, ResourceLocation> TEXTURES = new HashMap<>();
	private static final IndexedMap<String, String> EMOTES = new IndexedMap<String, String>();
	
	public static void registerEmote(String id, String icon)
	{
		if(EMOTES.containsKey(id))
			throw new RuntimeException("ID " + id + " already in use!");
		if(EMOTES.containsValue(icon))
			throw new RuntimeException("Value " + icon + " already in use!");
		EMOTES.put(id, icon);
	}
	
	static
	{
		Field[] fs = DefaultEmotes.class.getDeclaredFields();
		for(Field f : fs)
		{
			try
			{
				f.setAccessible(true);
				String i = f.get(null) + "";
				registerEmote(i, i);
			} catch(Throwable err)
			{
			}
		}
		
		LostThaumaturgy.LOG.info("Registered " + EMOTES.size() + " default emotes!");
	}
	
	public static ResourceLocation getEmoteLocation(String id)
	{
		String tx = "";
		if(!EMOTES.containsKey(id))
			tx = "3F";
		else
			tx = EMOTES.get(id);
		
		if(TEXTURES.containsKey(tx))
			return TEXTURES.get(tx);
		
		ResourceLocation rl = new ResourceLocation(tx);
		TEXTURES.put(tx, new ResourceLocation(rl.getResourceDomain().equals("minecraft") ? LTInfo.MOD_ID : rl.getResourceDomain(), "textures/particle/emote/" + rl.getResourcePath() + ".png"));
		
		return TEXTURES.get(tx);
	}
	
	public static EmoteBuilder newEmote(World world, Vec3d pos, String id)
	{
		return new EmoteBuilder(world.provider.getDimension(), pos.x, pos.y, pos.z, id);
	}
}