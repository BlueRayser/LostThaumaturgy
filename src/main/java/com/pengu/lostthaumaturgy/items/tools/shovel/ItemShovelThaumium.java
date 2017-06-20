package com.pengu.lostthaumaturgy.items.tools.shovel;

import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;

import net.minecraft.item.ItemSpade;

public class ItemShovelThaumium extends ItemSpade
{
	public ItemShovelThaumium()
    {
		super(ItemMaterialsLT.tool_thaumium);
		setUnlocalizedName("thaumium_shovel");
    }
}