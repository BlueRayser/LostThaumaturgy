package com.pengu.lostthaumaturgy.custom.wand.rod;

import com.pengu.lostthaumaturgy.api.wand.WandRod;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class WandRodGreatwood extends WandRod
{
	public WandRodGreatwood()
	{
		super("greatwood");
		rodItem = EnumMultiMaterialType.ROD_GREATWOOD.stack();
	}
	
	@Override
	public float getBaseCost()
	{
		return 95;
	}
	
	@Override
	public float getRodCapacity()
	{
		return 50;
	}
	
	@Override
	public String getRodTexture()
	{
		return Info.MOD_ID + ":items/wand/rod_greatwood_mat";
	}
	
	@Override
	public int getCraftCost()
	{
		return 10;
	}
}