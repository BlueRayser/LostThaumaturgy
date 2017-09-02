package com.pengu.lostthaumaturgy.core.items.tools.shovel;

import net.minecraft.item.ItemSpade;

import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;

public class ItemShovelThaumium extends ItemSpade
{
	public ItemShovelThaumium()
	{
		super(ItemMaterialsLT.tool_thaumium);
		setUnlocalizedName("thaumium_shovel");
	}
}