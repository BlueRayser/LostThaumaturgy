package com.pengu.lostthaumaturgy.client.render.tesr;

import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class TESRSilverwoodVisTank extends TESRVisTank
{
	public static final TESRSilverwoodVisTank INSTANCE = new TESRSilverwoodVisTank();
	
	protected void setupTextures()
	{
		bottom = ClientProxy.getSprite(Info.MOD_ID + ":blocks/silverwood_vis_tank/bottom");
		side = ClientProxy.getSprite(Info.MOD_ID + ":blocks/silverwood_vis_tank/side");
		side_to_bottom = ClientProxy.getSprite(Info.MOD_ID + ":blocks/silverwood_vis_tank/side_to_bottom");
		side_to_top = ClientProxy.getSprite(Info.MOD_ID + ":blocks/silverwood_vis_tank/side_to_top");
		side_to_top_bottom = ClientProxy.getSprite(Info.MOD_ID + ":blocks/silverwood_vis_tank/side_to_top_bottom");
		top = ClientProxy.getSprite(Info.MOD_ID + ":blocks/silverwood_vis_tank/top");
	}
}