package com.pengu.lostthaumaturgy.tile.monolith;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.net.utils.NetPropertyNumber;

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