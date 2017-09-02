package com.pengu.lostthaumaturgy.core.tile.monolith;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.hammercore.tile.TileSyncable;

public class TileExtraRoom extends TileSyncable
{
	public final NetPropertyNumber<Integer> orientation;
	
	{
		orientation = new NetPropertyNumber<Integer>(this, EnumFacing.NORTH.ordinal());
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