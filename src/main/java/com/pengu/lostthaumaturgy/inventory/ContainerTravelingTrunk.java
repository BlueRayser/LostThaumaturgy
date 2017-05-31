package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.entity.EntityTravelingTrunk;

public class ContainerTravelingTrunk extends Container
{
	public final EntityTravelingTrunk trunk;
	
	public ContainerTravelingTrunk(EntityTravelingTrunk trunk, EntityPlayer player)
	{
		this.trunk = trunk;
		
		for(int l = 0; l < 9; ++l)
			addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 168));
		
		for(int k = 0; k < 3; ++k)
			for(int j1 = 0; j1 < 9; ++j1)
				addSlotToContainer(new Slot(player.inventory, j1 + k * 9 + 9, 8 + j1 * 18, 110 + k * 18));
		
		for(int j = 0; j < trunk.inventory.getSizeInventory() / 9; ++j)
			for(int i1 = 0; i1 < 9; ++i1)
				addSlotToContainer(new Slot(trunk.inventory, i1 + j * 9, 8 + i1 * 18, 17 + j * 18));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return trunk.inventory.isUsableByPlayer(playerIn, trunk.getPosition());
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = getSlot(index);
		if(slot != null)
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index >= 0 && index < 36 && !mergeItemStack(itemstack1, 36, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			if(index >= 36 && !mergeItemStack(itemstack1, 0, 36, false))
				return ItemStack.EMPTY;
			if(!itemstack1.isEmpty())
				slot.onSlotChanged();
			if(itemstack1.getCount() != itemstack.getCount())
				slot.putStack(itemstack1);
			else
				return ItemStack.EMPTY;
		}
		return itemstack;
	}
}