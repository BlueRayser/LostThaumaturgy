package com.pengu.lostthaumaturgy.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.tile.TileDarknessGenerator;

public class SlotDarknessGenerator extends Slot
{
	public SlotDarknessGenerator(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return TileDarknessGenerator.isValidSeed(stack);
	}
}