package com.pengu.lostthaumaturgy.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.net.utils.NetPropertyItemStack;

public class TileSeal extends TileSyncableTickable
{
	public final NetPropertyItemStack stack;
	
	{
		stack = new NetPropertyItemStack(this, ItemStack.EMPTY);
	}
	
	@Override
	public void tick()
	{
		
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		
	}
}