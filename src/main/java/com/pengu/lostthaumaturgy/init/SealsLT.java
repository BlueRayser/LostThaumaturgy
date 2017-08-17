package com.pengu.lostthaumaturgy.init;

import java.util.function.Function;

import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.seal.SealCombination;
import com.pengu.lostthaumaturgy.api.seal.SealInstance;
import com.pengu.lostthaumaturgy.api.seal.SealManager;
import com.pengu.lostthaumaturgy.custom.seals.water.SealWaterHydrate;
import com.pengu.lostthaumaturgy.tile.TileSeal;

public class SealsLT
{
	private static int seals;
	
	public static void init()
	{
		LostThaumaturgy.LOG.info("Registering seals...");
		register(new SealCombination(ItemsLT.RUNICESSENCE_WATER, null, null), seal -> new SealWaterHydrate(seal), "Water");
		LostThaumaturgy.LOG.info("-Registered " + seals + " seals.");
	}
	
	private static void register(SealCombination combo, Function<TileSeal, SealInstance> obtainer, String name)
	{
		LostThaumaturgy.LOG.info(" -" + name + "...");
		SealManager.registerCombination(combo, obtainer);
		++seals;
	}
}