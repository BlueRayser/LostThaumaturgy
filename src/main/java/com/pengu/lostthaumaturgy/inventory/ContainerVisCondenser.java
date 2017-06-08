package com.pengu.lostthaumaturgy.inventory;

import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;
import com.pengu.lostthaumaturgy.tile.TileVisCondenser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerVisCondenser extends Container
{
	public final TileVisCondenser tile;
	
	public ContainerVisCondenser(EntityPlayer player, TileVisCondenser tile)
	{
		this.tile = tile;
		
		addSlotToContainer(new Slot(tile, 0, 46, 32));
		addSlotToContainer(new SlotOutput(tile, 1, 114, 32));
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 142));
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		tile.sync();
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
			if(index >= 0 && index < 2 && !mergeItemStack(itemstack1, 2, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			if(index >= 2 && !mergeItemStack(itemstack1, 0, 1, false))
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
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.isUsableByPlayer(playerIn);
	}
}