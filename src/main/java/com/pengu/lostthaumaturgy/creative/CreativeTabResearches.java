package com.pengu.lostthaumaturgy.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class CreativeTabResearches extends CreativeTabs
{
	public CreativeTabResearches()
	{
		super(LTInfo.MOD_ID + ".researches");
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ItemsLT.DISCOVERY);
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		return getTabIconItem();
	}
}