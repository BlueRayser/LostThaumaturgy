package com.pengu.lostthaumaturgy.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import com.pengu.hammercore.bookAPI.ItemBook;
import com.pengu.lostthaumaturgy.LTInfo;

public class ItemThaumonomicon extends ItemBook
{
	public ItemThaumonomicon()
	{
		setUnlocalizedName("thaumonomicon");
		setMaxStackSize(1);
	}
	
	@Override
	public String getBookId()
	{
		return LTInfo.MOD_ID + ":thaumonomicon";
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}
}