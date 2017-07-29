package com.pengu.lostthaumaturgy.api.restorer;

import net.minecraft.item.ItemStack;

import com.pengu.hammercore.utils.WorldLocation;

public interface ICustomRepairable
{
	float getVisCost(ItemStack stack, WorldLocation loc);
	
	default boolean canRepair(ItemStack stack, WorldLocation loc)
	{
		return stack.getItemDamage() > 0;
	}
	
	default boolean attempRepair(ItemStack stack, WorldLocation loc)
	{
		if(stack.getItemDamage() > 0)
		{
			stack.setItemDamage(stack.getItemDamage() - 1);
			return true;
		}
		return false;
	}
}