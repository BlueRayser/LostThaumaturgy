package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.core.tile.TileDarknessGenerator;
import com.pengu.lostthaumaturgy.inventory.slot.SlotDarknessGenerator;
import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;

public class ContainerDarkGenerator extends Container
{
	public final TileDarknessGenerator tile;
	
	public ContainerDarkGenerator(TileDarknessGenerator tile, InventoryPlayer player)
	{
		this.tile = tile;
		
		addSlotToContainer(new SlotDarknessGenerator(tile.inv, 0, 46, 32));
		addSlotToContainer(new SlotOutput(tile.inv, 1, 114, 32));
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player, j, 8 + j * 18, 142));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.inv.isUsableByPlayer(playerIn, tile.getPos());
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
			
			if(index > 2 && !mergeItemStack(itemstack1, 0, 1, false))
				return ItemStack.EMPTY;
			
			if(index < 3 && !mergeItemStack(itemstack1, 2, inventorySlots.size(), false))
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