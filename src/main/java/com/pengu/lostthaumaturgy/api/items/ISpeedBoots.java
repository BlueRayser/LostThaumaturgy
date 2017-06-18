package com.pengu.lostthaumaturgy.api.items;

import net.minecraft.item.ItemStack;

public interface ISpeedBoots
{
	float getWalkBoost(ItemStack boots);
	
	float getStepAssist(ItemStack boots);
	
	float getJumpMod(ItemStack boots);
}