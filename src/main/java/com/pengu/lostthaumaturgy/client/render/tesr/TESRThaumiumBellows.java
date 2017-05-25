package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.LTInfo;

public class TESRThaumiumBellows extends TESRBellows
{
	public static final TESRThaumiumBellows INSTANCE = new TESRThaumiumBellows();
	
	{
		bellow_texture = new ResourceLocation(LTInfo.MOD_ID, "textures/models/thaumium_bellows.png");
	}
}