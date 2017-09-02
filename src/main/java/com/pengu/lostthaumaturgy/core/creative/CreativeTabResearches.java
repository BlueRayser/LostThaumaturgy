package com.pengu.lostthaumaturgy.core.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class CreativeTabResearches extends CreativeTabs
{
	public CreativeTabResearches()
	{
		super(Info.MOD_ID + ".researches");
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