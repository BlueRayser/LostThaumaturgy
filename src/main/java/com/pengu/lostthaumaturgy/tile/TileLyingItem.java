package com.pengu.lostthaumaturgy.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.net.utils.NetPropertyAbstract;
import com.pengu.hammercore.net.utils.NetPropertyItemStack;

public class TileLyingItem extends TileSyncable
{
	public final NetPropertyItemStack lying;
	
	{
		lying = new NetPropertyItemStack(this, ItemStack.EMPTY);
	}
	
	@Override
	public void notifyOfChange(NetPropertyAbstract prop)
	{
		if(lying.get().isEmpty() && !world.isRemote)
			world.setBlockToAir(pos);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
}