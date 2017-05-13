package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.tile.TileVisPumpThaumium;

public class TESRVisPumpThaumium extends TESRVisPump<TileVisPumpThaumium>
{
	public static final TESRVisPumpThaumium INSTANCE = new TESRVisPumpThaumium();
	
	{
		pump = new ResourceLocation(LTInfo.MOD_ID, "textures/models/thaumium_pump.png");
		pump_off = new ResourceLocation(LTInfo.MOD_ID, "textures/models/thaumium_pump_off.png");
	}
}