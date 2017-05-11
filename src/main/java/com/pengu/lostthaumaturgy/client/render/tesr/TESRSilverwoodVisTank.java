package com.pengu.lostthaumaturgy.client.render.tesr;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class TESRSilverwoodVisTank extends TESRVisTank
{
	public static final TESRSilverwoodVisTank INSTANCE = new TESRSilverwoodVisTank();
	
	protected void setupTextures()
	{
		bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/silverwood_vis_tank/bottom");
		side = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/silverwood_vis_tank/side");
		side_to_bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/silverwood_vis_tank/side_to_bottom");
		side_to_top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/silverwood_vis_tank/side_to_top");
		side_to_top_bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/silverwood_vis_tank/side_to_top_bottom");
		top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/silverwood_vis_tank/top");
	}
}