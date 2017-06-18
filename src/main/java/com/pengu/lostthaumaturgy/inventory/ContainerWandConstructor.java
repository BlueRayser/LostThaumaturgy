package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;
import com.pengu.lostthaumaturgy.inventory.slot.SlotWandComponent;
import com.pengu.lostthaumaturgy.tile.TileWandConstructor;

public class ContainerWandConstructor extends Container
{
	public final TileWandConstructor tile;
	public Slot[] slots = new Slot[4];
	
	public ContainerWandConstructor(TileWandConstructor tile, InventoryPlayer player)
	{
		this.tile = tile;
		
		addSlotToContainer(slots[0] = new SlotWandComponent(true, tile.inventory, 0, 23, 19));
		addSlotToContainer(slots[1] = new SlotWandComponent(true, tile.inventory, 1, 59, 55));
		addSlotToContainer(slots[2] = new SlotWandComponent(false, tile.inventory, 2, 41, 37));
		addSlotToContainer(slots[3] = new SlotOutput(tile.inventory, 3, 119, 37));
		
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player, j, 8 + j * 18, 146));
		
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player, k + i * 9 + 9, 8 + k * 18, 88 + i * 18));
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
			if(index < 4 && !mergeItemStack(itemstack1, 4, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			if(index > 3 && !mergeItemStack(itemstack1, 0, 3, false))
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