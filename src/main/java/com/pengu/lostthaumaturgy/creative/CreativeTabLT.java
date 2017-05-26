package com.pengu.lostthaumaturgy.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class CreativeTabLT extends CreativeTabs
{
	public CreativeTabLT()
	{
		super(LTInfo.MOD_ID);
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(BlocksLT.INFUSER);
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(BlocksLT.INFUSER);
	}
}