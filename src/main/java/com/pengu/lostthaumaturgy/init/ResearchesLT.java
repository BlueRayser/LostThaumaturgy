package com.pengu.lostthaumaturgy.init;

import java.lang.reflect.Field;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.custom.research.ResearchRegistry;

public class ResearchesLT
{
	public static final Research //
	        SILVERWOOD_VIS_TANK = new Research(LTInfo.MOD_ID + ":silverwood_vis_tank", 25).setColor(0xBBBBBB), //
	        REINFORCED_VIS_TANK = new Research(LTInfo.MOD_ID + ":reinforced_vis_tank", 30).setColor(0x911CFF), //
	        CRYSTALLINE_BELL = new Research(LTInfo.MOD_ID + ":crystalline_bell", 75).setColor(0xAFFFFF), //
	        PRESSURIZED_COUNDUIT = new Research(LTInfo.MOD_ID + ":pressurized_conduit", 50)
	        ;
	
	public static void registerResearches()
	{
		for(Field f : ResearchesLT.class.getDeclaredFields()) if(Research.class.isAssignableFrom(f.getType())) try
		{
			Research r = (Research) f.get(null);
			ResearchRegistry.registerResearch(r);
		} catch(Throwable er) {}
	}
}