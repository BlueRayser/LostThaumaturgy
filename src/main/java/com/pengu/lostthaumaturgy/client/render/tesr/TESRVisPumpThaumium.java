package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.tile.TileVisPumpThaumium;

public class TESRVisPumpThaumium extends TESRVisPump<TileVisPumpThaumium>
{
	public static final TESRVisPumpThaumium INSTANCE = new TESRVisPumpThaumium();
	
	{
		pump = new ResourceLocation(Info.MOD_ID, "textures/models/thaumium_pump.png");
		pump_malfunction = new ResourceLocation(Info.MOD_ID, "textures/models/thaumium_pump_malfunction.png");
		pump_off = new ResourceLocation(Info.MOD_ID, "textures/models/thaumium_pump_off.png");
	}
}