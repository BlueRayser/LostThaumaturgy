package com.pengu.lostthaumaturgy.init;

import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.api.wand.WandRegistry;
import com.pengu.lostthaumaturgy.api.wand.WandRod;
import com.pengu.lostthaumaturgy.custom.wand.cap.WandCapGold;
import com.pengu.lostthaumaturgy.custom.wand.cap.WandCapIron;
import com.pengu.lostthaumaturgy.custom.wand.cap.WandCapThaumium;
import com.pengu.lostthaumaturgy.custom.wand.cap.WandCapVoid;
import com.pengu.lostthaumaturgy.custom.wand.rod.WandRodSilverwood;
import com.pengu.lostthaumaturgy.custom.wand.rod.WandRodWood;

public class WandsLT
{
	public static final WandCap //
	        CAP_IRON = new WandCapIron(), //
	        CAP_GOLD = new WandCapGold(), //
	        CAP_THAUMIUM = new WandCapThaumium(), //
	        CAP_VOID = new WandCapVoid();
	
	public static final WandRod //
	        ROD_WOOD = new WandRodWood(), //
	        ROD_SILVERWOOD = new WandRodSilverwood();
	
	public static void init()
	{
		try
		{
			Thread.sleep(500L);
		} catch(Throwable err)
		{
		}
	}
	
	static
	{
		WandRegistry.registerCap(CAP_IRON);
		WandRegistry.registerCap(CAP_GOLD);
		WandRegistry.registerCap(CAP_THAUMIUM);
		WandRegistry.registerCap(CAP_VOID);
		
		WandRegistry.registerRod(ROD_WOOD);
		WandRegistry.registerRod(ROD_SILVERWOOD);
		
		WandRegistry.registerCreativeSubitem(ROD_WOOD, CAP_IRON);
		WandRegistry.registerCreativeSubitem(ROD_SILVERWOOD, CAP_THAUMIUM);
		WandRegistry.registerCreativeSubitem(ROD_SILVERWOOD, CAP_VOID);
	}
}