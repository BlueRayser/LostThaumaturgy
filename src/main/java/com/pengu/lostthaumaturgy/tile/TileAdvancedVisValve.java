package com.pengu.lostthaumaturgy.tile;

import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.lang3.ArrayUtils;

import com.mrdimka.hammercore.vec.Cuboid6;

public class TileAdvancedVisValve extends TileConduit
{
	private boolean prevPower;
	public int setting = 0;
	public int prevSetting = 0;
	
	@Override
	public void rebake()
	{
		super.rebake();
		hitboxes = ArrayUtils.add(hitboxes, new Cuboid6(3.5 / 16, 3.5 / 16, 3.5 / 16, 12.5 / 16, 12.5 / 16, 12.5 / 16));
	}
	
	@Override
	public void tick()
	{
		if(ticksExisted % 20 == 0) rebake();
		
		if(world.isRemote)
			return;
		
		if(prevdisplayPure != displayPure || prevdisplayTaint != displayTaint)
		{
			prevdisplayPure = displayPure;
			prevdisplayTaint = displayTaint;
			sync();
		}
		
		calculateSuction();
		
		if(setting == 0)
			setSuction(0);
		if(setting == 1)
			setTaintSuction(0);
		if(setting == 2)
			setVisSuction(0);
		if(getSuction(null) > 0)
			equalizeWithNeighbours();
		
		if(displayPure != pureVis || displayTaint != taintedVis)
		{
			displayPure = pureVis;
			displayTaint = taintedVis;
		}
		
		if(displayTaint + displayPure < 0.1f)
		{
			displayTaint = 0.0f;
			displayPure = 0.0f;
		}
		
		if(gettingPower())
		{
			if(!prevPower)
				prevSetting = setting;
			prevPower = true;
			setting = 0;
			sync();
		}
		
		if(!gettingPower() && prevPower)
		{
			setting = prevSetting;
			prevPower = false;
			sync();
		}
	}
	
	protected boolean gettingPower()
	{
		return world.isBlockIndirectlyGettingPowered(pos) > 0;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setInteger("Setting", setting);
		nbt.setInteger("PrevSetting", prevSetting);
		nbt.setBoolean("PrevPower", prevPower);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		setting = nbt.getInteger("Setting");
		prevSetting = nbt.getInteger("PrevSetting");
		prevPower = nbt.getBoolean("PrevPower");
	}
}