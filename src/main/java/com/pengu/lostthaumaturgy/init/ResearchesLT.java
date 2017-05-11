package com.pengu.lostthaumaturgy.init;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.custom.research.ResearchRegistry;

public class ResearchesLT
{
	public static final Research //
	        SILVERWOOD_VIS_TANK = new Research(LTInfo.MOD_ID + ":silverwood_vis_tank", 25).setColor(0xBBBBBB), //
	        REINFORCED_VIS_TANK = new Research(LTInfo.MOD_ID + ":reinforced_vis_tank", 30).setColor(0x911CFF), //
	        CRYSTALLINE_BELL = new Research(LTInfo.MOD_ID + ":crystalline_bell", 75).setColor(0xAFFFFF);
	
	public static void registerResearches()
	{
		ResearchRegistry.registerResearch(SILVERWOOD_VIS_TANK);
		ResearchRegistry.registerResearch(REINFORCED_VIS_TANK);
		ResearchRegistry.registerResearch(CRYSTALLINE_BELL);
	}
}