package com.pengu.lostthaumaturgy.api.items;

import net.minecraft.item.ItemStack;

public interface IVisRepairable
{
	float visRepairCost(ItemStack stack);
}