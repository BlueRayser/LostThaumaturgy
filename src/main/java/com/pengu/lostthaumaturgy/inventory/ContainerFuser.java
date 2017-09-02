package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.pengu.lostthaumaturgy.core.tile.TileFuser;
import com.pengu.lostthaumaturgy.inventory.slot.SlotFuserOutput;
import com.pengu.lostthaumaturgy.inventory.slot.SlotWand;

public class ContainerFuser extends Container implements IContainerListener
{
	public final TileFuser tile;
	public final SlotFuserOutput output;
	
	public ContainerFuser(TileFuser tile, InventoryPlayer player)
	{
		this.tile = tile;
		if(tile.gui != null)
			tile.gui.craftingPlayer = player.player;
		tile.craftingPlayer = player.player;
		
		addSlotToContainer(new SlotWand(tile.inventory, 9, 132, 71));
		addSlotToContainer(output = new SlotFuserOutput(tile.inventory, 10, -10000, -10000));
		
		for(int i = 0; i < 9; ++i)
			addSlotToContainer(new Slot(tile.inventory, i, 36 + (i % 3) * 20, 18 + (i / 3) * 20));
		
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player, j, 8 + j * 18, 164));
		
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player, k + i * 9 + 9, 8 + k * 18, 106 + i * 18));
		
		addListener(this);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.inventory.craftingInv.isUsableByPlayer(playerIn, tile.getPos());
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		if(index == 1)
			return ItemStack.EMPTY;
		
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = getSlot(index);
		if(slot != null)
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index < 11 && !mergeItemStack(itemstack1, 11, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			
			if(index > 10 && !mergeItemStack(itemstack1, 0, 11, false))
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
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
	{
	}
	
	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory)
	{
	}
	
	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
	{
	}
	
	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue)
	{
	}
}