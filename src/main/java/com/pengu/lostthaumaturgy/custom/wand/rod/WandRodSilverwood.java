package com.pengu.lostthaumaturgy.custom.wand.rod;

import com.pengu.lostthaumaturgy.api.wand.WandRod;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class WandRodSilverwood extends WandRod
{
	public WandRodSilverwood()
	{
		super("silverwood");
		rodItem = EnumMultiMaterialType.ROD_SILVERWOOD.stack();
	}
	
	@Override
	public float getBaseCost()
	{
		return 80;
	}
	
	@Override
	public float getRodCapacity()
	{
		return 100;
	}
	
	@Override
	public String getRodTexture()
	{
		return Info.MOD_ID + ":items/wand/rod_silverwood_mat";
	}
	
	@Override
	public int getCraftCost()
	{
		return 25;
	}
}