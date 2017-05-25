package com.pengu.lostthaumaturgy.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.api.RecipesCrucible;

public class SlotCrucible extends Slot
{
	public SlotCrucible(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return RecipesCrucible.getSmeltingValue(stack) > 0F;
	}
}