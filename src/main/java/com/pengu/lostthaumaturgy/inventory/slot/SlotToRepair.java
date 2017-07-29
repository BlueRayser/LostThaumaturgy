package com.pengu.lostthaumaturgy.inventory.slot;

import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.restorer.RestorerManager;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotToRepair extends Slot
{
	public final WorldLocation loc;
	
	public SlotToRepair(IInventory inventoryIn, int index, int xPosition, int yPosition, WorldLocation loc)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.loc = loc;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return RestorerManager.findCustomRepairable(stack.getItem()).canRepair(stack, loc);
	}
}