package com.pengu.lostthaumaturgy.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.items.ItemWand;

public class SlotWand extends Slot
{
	public SlotWand(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack.getMaxStackSize() == 1 && stack.getItem() instanceof ItemWand;
	}
}