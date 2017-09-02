package com.pengu.lostthaumaturgy.custom.wand.cap;

import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class WandCapThaumium extends WandCap
{
	public WandCapThaumium()
	{
		super("thaumium");
		capItem = EnumMultiMaterialType.CAP_THAUMIUM.stack();
	}
	
	@Override
	public float getUseCost()
	{
		return -10;
	}
	
	@Override
	public String getCapTexture()
	{
		return Info.MOD_ID + ":items/wand/cap_thaumium_mat";
	}
	
	@Override
	public int getCraftCost()
	{
		return 15;
	}
}