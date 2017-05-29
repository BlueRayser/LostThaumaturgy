package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.inventory.slot.SlotCrucible;
import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;
import com.pengu.lostthaumaturgy.tile.TileDuplicator;

public class ContainerDuplicator extends Container
{
	public final TileDuplicator tile;
	
	public ContainerDuplicator(TileDuplicator tile, EntityPlayer player)
	{
		this.tile = tile;
		
		addSlotToContainer(new SlotCrucible(tile.inventory, 9, 23, 34));
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 3; ++k)
				addSlotToContainer(new SlotOutput(tile.inventory, k + i * 3, 90 + k * 18, 17 + i * 18));
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 142));
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.inventory.isUsableByPlayer(playerIn, tile.getPos());
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
			
			if(index > 9 && !mergeItemStack(itemstack1, 0, 1, false))
				return ItemStack.EMPTY;
			
			if(index >= 0 && index < 10 && !mergeItemStack(itemstack1, 10, inventorySlots.size(), false))
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