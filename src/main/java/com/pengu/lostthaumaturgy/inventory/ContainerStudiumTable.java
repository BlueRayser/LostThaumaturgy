package com.pengu.lostthaumaturgy.inventory;

import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.inventory.slot.SlotCrucible;
import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;
import com.pengu.lostthaumaturgy.inventory.slot.SlotPaper;
import com.pengu.lostthaumaturgy.tile.TileStudiumTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotShulkerBox;
import net.minecraft.item.ItemStack;

public class ContainerStudiumTable extends Container
{
	public final TileStudiumTable table;
	
	public ContainerStudiumTable(TileStudiumTable table, EntityPlayer player)
	{
		this.table = table;
		
		addSlotToContainer(new SlotPaper(table.inventory, 1, 134, 9));
		addSlotToContainer(new SlotCrucible(table.inventory, 0, 22, 49));
		
		for(int x = 0; x < 3; ++x)
			for(int y = 0; y < 3; ++y)
				addSlotToContainer(new SlotOutput(table.inventory, 2 + x + y * 3, 116 + x * 18, 32 + y * 18));
		
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 88 + i * 18));
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 146));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return table.inventory.isUsableByPlayer(playerIn, table.getPos());
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = getSlot(index);
		if(slot != null && slot.canBeHovered())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index >= 0 && index < 11 && !mergeItemStack(itemstack1, 11, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			if(index >= 0 && index >= 11 && getSlot(0).isItemValid(itemstack1) && !mergeItemStack(itemstack1, 0, 1, false))
				return ItemStack.EMPTY;
			if(index >= 0 && index >= 11 && getSlot(1).isItemValid(itemstack1) && !mergeItemStack(itemstack1, 1, 2, false))
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