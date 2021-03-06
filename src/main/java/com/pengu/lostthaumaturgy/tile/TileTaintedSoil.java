package com.pengu.lostthaumaturgy.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.BlockSnapshot;

import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.net.utils.NetPropertyBool;
import com.pengu.hammercore.net.utils.NetPropertyNBT;

public class TileTaintedSoil extends TileSyncable
{
	public final NetPropertyNBT<NBTTagCompound> BLOCK_SNAPSHOT;
	public final NetPropertyBool DROP_CONGEALED_TAINT;
	
	public TileTaintedSoil()
	{
		BLOCK_SNAPSHOT = new NetPropertyNBT<NBTTagCompound>(this);
		DROP_CONGEALED_TAINT = new NetPropertyBool(this, false);
	}
	
	public TileTaintedSoil(BlockSnapshot snapshot)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		snapshot.writeToNBT(nbt);
		BLOCK_SNAPSHOT = new NetPropertyNBT<NBTTagCompound>(this, nbt);
		DROP_CONGEALED_TAINT = new NetPropertyBool(this, false);
	}
	
	public BlockSnapshot getSnapshot()
	{
		NBTTagCompound nbt = BLOCK_SNAPSHOT.get();
		if(nbt != null)
			return BlockSnapshot.readFromNBT(nbt);
		return null;
	}
	
	public void setSnapshot(BlockSnapshot snapshot)
	{
		if(world != null && world.isRemote)
			return;
		NBTTagCompound nbt = new NBTTagCompound();
		snapshot.writeToNBT(nbt);
		BLOCK_SNAPSHOT.set(nbt);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
	}
}