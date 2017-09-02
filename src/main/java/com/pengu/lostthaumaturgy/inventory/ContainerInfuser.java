package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.core.tile.TileInfuser;
import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;

public class ContainerInfuser extends Container
{
	private TileInfuser infuser;
	private int lastCookTime = 0;
	
	public ContainerInfuser(InventoryPlayer inventoryplayer, TileInfuser tileInfuser)
	{
		if(tileInfuser.sucked <= 0F)
			tileInfuser.initiator = inventoryplayer.player.getGameProfile().getId();
		
		infuser = tileInfuser;
		addSlotToContainer(new Slot(tileInfuser.infuserItemStacks, 2, 80, 11));
		addSlotToContainer(new Slot(tileInfuser.infuserItemStacks, 3, 28, 102));
		addSlotToContainer(new Slot(tileInfuser.infuserItemStacks, 4, 132, 102));
		addSlotToContainer(new Slot(tileInfuser.infuserItemStacks, 5, 50, 55));
		addSlotToContainer(new Slot(tileInfuser.infuserItemStacks, 6, 110, 55));
		addSlotToContainer(new Slot(tileInfuser.infuserItemStacks, 7, 80, 106));
		
		addSlotToContainer(new SlotOutput(tileInfuser.infuserItemStacks, 0, 80, 72));
		addSlotToContainer(new SlotOutput(tileInfuser.infuserItemStacks, 1, 80, 135));
		
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 216));
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 158 + i * 18));
	}
	
	@Override
	public void updateProgressBar(int id, int data)
	{
		if(id == 0)
		{
			infuser.infuserCookTime = data;
		}
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		super.onCraftMatrixChanged(inventoryIn);
		
		for(int i = 0; i < listeners.size(); ++i)
		{
			IContainerListener icrafting = listeners.get(i);
			if((float) lastCookTime == infuser.infuserCookTime)
				continue;
			icrafting.sendWindowProperty(this, 0, Math.round(infuser.infuserCookTime));
		}
		lastCookTime = Math.round(infuser.infuserCookTime);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return infuser.isUsableByPlayer(playerIn);
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
			if(i < 8 ? !mergeItemStack(itemstack1, 8, 35, true) : (i >= 8 ? !mergeItemStack(itemstack1, 0, 6, false) : (i > 35 && i <= 44 ? !mergeItemStack(itemstack1, 8, 35, false) : !mergeItemStack(itemstack1, 8, 44, false))))
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