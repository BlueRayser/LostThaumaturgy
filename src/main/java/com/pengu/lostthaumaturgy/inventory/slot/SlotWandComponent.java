package com.pengu.lostthaumaturgy.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.api.wand.WandRegistry;

public class SlotWandComponent extends Slot
{
	public boolean capOrRod;
	
	public SlotWandComponent(boolean capOrRod, IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.capOrRod = capOrRod;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return capOrRod ? WandRegistry.selectCap(stack) != null : WandRegistry.selectRod(stack) != null;
	}
}