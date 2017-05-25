package com.pengu.lostthaumaturgy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.inventory.slot.SlotOutput;
import com.pengu.lostthaumaturgy.tile.TileCrystallizer;

public class ContainerCrystallizer extends Container
{
	public TileCrystallizer tile;
	
	public final Slot darkSlot;
	
	public ContainerCrystallizer(TileCrystallizer tile, EntityPlayer player)
	{
		this.tile = tile;
		
		addSlotToContainer(new Slot(tile, 6, 80, 70));
		addSlotToContainer(new SlotOutput(tile, 0, 131, 41));
		addSlotToContainer(new SlotOutput(tile, 1, 30, 41));
		addSlotToContainer(new SlotOutput(tile, 2, 30, 100));
		addSlotToContainer(new SlotOutput(tile, 3, 131, 100));
		addSlotToContainer(new SlotOutput(tile, 4, 80, 12));
		addSlotToContainer(darkSlot = new SlotOutput(tile, 5, 80, 129));
		
		for(int i = 0; i < 3; ++i)
			for(int k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 158 + i * 18));
		
		for(int i = 0; i < 9; ++i)
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 216));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return ItemStack.EMPTY;
	}
}