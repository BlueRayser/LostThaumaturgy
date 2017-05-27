package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.tile.TileVoidChest;

public class ContainerVoidChest extends Container
{
	public final TileVoidChest chest;
	
	public ContainerVoidChest(TileVoidChest chest, EntityPlayer player)
	{
		this.chest = chest;
		
		for(int i = 0; i < 8; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(chest, k + i * 9, 8 + k * 18, 9 + i * 18));
		
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 158 + i * 18));
		
		for(int j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 216));
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		
		World worldIn = playerIn.world;
		if(!worldIn.isRemote)
			HammerCore.audioProxy.playSoundAt(worldIn, LTInfo.MOD_ID + ":void_chest_close", chest.getPos(), .3F, 1F, SoundCategory.BLOCKS);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return chest.isUsableByPlayer(playerIn);
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
			if(index >= 0 && index < 72 && !mergeItemStack(itemstack1, 72, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			if(index >= 72 && !mergeItemStack(itemstack1, 0, 72, false))
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