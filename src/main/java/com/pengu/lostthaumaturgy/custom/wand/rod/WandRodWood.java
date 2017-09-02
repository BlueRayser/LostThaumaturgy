package com.pengu.lostthaumaturgy.custom.wand.rod;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.api.wand.WandRod;
import com.pengu.lostthaumaturgy.core.Info;

public class WandRodWood extends WandRod
{
	public WandRodWood()
	{
		super("wood");
		rodItem = new ItemStack(Items.STICK);
	}
	
	@Override
	public float getBaseCost()
	{
		return 105;
	}
	
	@Override
	public float getRodCapacity()
	{
		return 15;
	}
	
	@Override
	public String getRodTexture()
	{
		return Info.MOD_ID + ":items/wand/rod_wood_mat";
	}
}