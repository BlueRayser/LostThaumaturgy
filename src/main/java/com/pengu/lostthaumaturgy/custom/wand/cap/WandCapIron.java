package com.pengu.lostthaumaturgy.custom.wand.cap;

import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class WandCapIron extends WandCap
{
	public WandCapIron()
	{
		super("iron");
		capItem = EnumMultiMaterialType.CAP_IRON.stack();
	}
	
	@Override
	public float getUseCost()
	{
		return 5;
	}
	
	@Override
	public String getCapTexture()
	{
		return Info.MOD_ID + ":items/wand/cap_iron_mat";
	}
}