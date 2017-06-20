package com.pengu.lostthaumaturgy.items.tools.axe;

import net.minecraft.item.ItemAxe;

import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;

public class ItemAxeThaumium extends ItemAxe
{
	public ItemAxeThaumium()
	{
		super(ItemMaterialsLT.tool_thaumium, 8, -3.2F);
		setUnlocalizedName("thaumium_axe");
	}
}