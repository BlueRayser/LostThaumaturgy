package com.pengu.lostthaumaturgy.custom.wand.cap;

import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class WandCapVoid extends WandCap
{
	public WandCapVoid()
	{
		super("void");
		capItem = EnumMultiMaterialType.CAP_VOID.stack();
	}
	
	@Override
	public float getUseCost()
	{
		return -20;
	}
	
	@Override
	public String getCapTexture()
	{
		return Info.MOD_ID + ":items/wand/cap_void_mat";
	}
	
	@Override
	public int getCraftCost()
	{
		return 30;
	}
}