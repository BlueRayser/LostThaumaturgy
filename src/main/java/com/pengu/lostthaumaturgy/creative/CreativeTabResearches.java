package com.pengu.lostthaumaturgy.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.init.ResearchesLT;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;

public class CreativeTabResearches extends CreativeTabs
{
	public CreativeTabResearches()
	{
		super(LTInfo.MOD_ID + ".researches");
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return ItemResearch.create(ResearchesLT.REINFORCED_VIS_TANK, EnumResearchItemType.THEORY);
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		return getTabIconItem();
	}
}