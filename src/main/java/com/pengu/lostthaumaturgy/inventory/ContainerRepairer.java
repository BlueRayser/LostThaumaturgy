package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;
import com.pengu.lostthaumaturgy.inventory.slot.SlotToRepair;
import com.pengu.lostthaumaturgy.tile.TileRepairer;

public class ContainerRepairer extends Container
{
	public final TileRepairer tile;
	
	public ContainerRepairer(TileRepairer tile, EntityPlayer player)
	{
		this.tile = tile;
		
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 2; ++k)
				addSlotToContainer(new SlotToRepair(tile, k + i * 2, 37 + k * 18, 16 + i * 18, tile.getLocation()));
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 2; ++k)
				addSlotToContainer(new SlotOutput(tile, 6 + (k + i * 2), 90 + k * 18, 16 + i * 18));
		
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 142));
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int i)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = getSlot(i);
		if(slot != null)
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(i < 12 && !mergeItemStack(itemstack1, 12, inventorySlots.size(), true))
				return ItemStack.EMPTY;
			if(i >= 12 && !mergeItemStack(itemstack1, 0, 12, false))
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