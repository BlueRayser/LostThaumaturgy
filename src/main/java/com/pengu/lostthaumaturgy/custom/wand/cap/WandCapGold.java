package com.pengu.lostthaumaturgy.custom.wand.cap;

import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class WandCapGold extends WandCap
{
	public WandCapGold()
	{
		super("gold");
		capItem = EnumMultiMaterialType.CAP_GOLD.stack();
	}
	
	@Override
	public float getUseCost()
	{
		return 0;
	}
	
	@Override
	public int getCraftCost()
	{
		return 5;
	}
	
	@Override
	public String getCapTexture()
	{
		return Info.MOD_ID + ":items/wand/cap_gold_mat";
	}
}