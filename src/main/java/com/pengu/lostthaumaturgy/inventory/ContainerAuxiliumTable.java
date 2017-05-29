package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.inventory.slot.SlotFragment;
import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;
import com.pengu.lostthaumaturgy.tile.TileAuxiliumTable;

public class ContainerAuxiliumTable extends Container
{
	public final TileAuxiliumTable tile;
	
	public ContainerAuxiliumTable(TileAuxiliumTable tile, EntityPlayer player)
	{
		this.tile = tile;
		
		addSlotToContainer(new SlotFragment(tile.inventory, 0, 22, 49));
		addSlotToContainer(new SlotOutput(tile.inventory, 1, 116, 49));
		
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 146));
		
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 88 + i * 18));
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
			if(index < 2 && !mergeItemStack(itemstack1, 2, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			if(index >= 2 && getSlot(0).isItemValid(itemstack1) && !mergeItemStack(itemstack1, 0, 1, false))
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