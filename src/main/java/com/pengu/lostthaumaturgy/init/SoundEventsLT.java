package com.pengu.lostthaumaturgy.init;

import java.lang.reflect.Field;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.utils.SoundObject;

public class SoundEventsLT
{
	public static final SoundObject //
	        BUBBLING = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "bubbling")), //
	        INFUSER = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "infuser")), //
	        DARK_INFUSER = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "dark_infuser")), //
	        ROOTS = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "roots")), //
	        FIZZ = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "fizz")), //
	        CREAKING = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "creaking")), //
	        DISCOVER = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "discover")), //
	        UPGRADE = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "upgrade")), //
	        ZAP = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "zap")), //
	        VOID_CHEST_OPEN = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "void_chest_open")), //
	        VOID_CHEST_CLOSE = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "void_chest_close")), //
	        ELECLOOP = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "elecloop")), //
	        STOMP = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "stomp")), //
	        MONOLITH = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "monolith")), //
	        PLACE = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "place")), //
	        RUMBLE = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "rumble")), //
	        SINGULARITY = new SoundObject(new ResourceLocation(LTInfo.MOD_ID, "singularity"));
	
	public static void register()
	{
		Field[] fs = SoundEventsLT.class.getFields();
		
		int steps = 0;
		for(Field f : fs)
			if(SoundObject.class.isAssignableFrom(f.getType()))
				++steps;
		
		ProgressBar bar = ProgressManager.push("Adding Sounds", steps);
		
		int i = 0;
		for(Field f : fs)
			if(SoundObject.class.isAssignableFrom(f.getType()))
				try
				{
					++i;
					SoundObject so = (SoundObject) f.get(null);
					bar.step(so.name + " (" + i + "/" + steps + ")");
					so.sound = GameRegistry.register(so.sound = new SoundEvent(so.name).setRegistryName(so.name));
				} catch(Throwable err)
				{
				}
		
		ProgressManager.pop(bar);
	}
}