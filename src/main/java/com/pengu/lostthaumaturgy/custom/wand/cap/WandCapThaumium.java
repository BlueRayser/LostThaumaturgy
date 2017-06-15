package com.pengu.lostthaumaturgy.custom.wand.cap;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

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
		return LTInfo.MOD_ID + ":items/wand/cap_thaumium_mat";
	}
}