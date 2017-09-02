package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.core.Info;

public class TESRThaumiumBellows extends TESRBellows
{
	public static final TESRThaumiumBellows INSTANCE = new TESRThaumiumBellows();
	
	{
		bellow_texture = new ResourceLocation(Info.MOD_ID, "textures/models/thaumium_bellows.png");
	}
}