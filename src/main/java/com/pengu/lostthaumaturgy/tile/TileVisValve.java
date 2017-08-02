package com.pengu.lostthaumaturgy.tile;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;

import org.apache.commons.lang3.ArrayUtils;

import com.mrdimka.hammercore.vec.Cuboid6;

public class TileVisValve extends TileConduit
{
	public boolean open;
	private boolean prevPower;
	
	@Override
	public void rebake()
	{
		super.rebake();
		hitboxes = ArrayUtils.add(hitboxes, new Cuboid6(3.5 / 16, 3.5 / 16, 3.5 / 16, 12.5 / 16, 12.5 / 16, 12.5 / 16));
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("enabled", open);
	}
	
	@Override
	public void tick()
	{
		if(ticksExisted % 20 == 0)
			rebake();
		if(world.isRemote)
			return;
		if(prevdisplayPure != displayPure || prevdisplayTaint != displayTaint)
		{
			sync();
			prevdisplayPure = displayPure;
			prevdisplayTaint = displayTaint;
		}
		calculateSuction();
		if(!open)
			setSuction(0);
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
			prevPower = true;
			open = false;
			sync();
		}
		
		if(!gettingPower() && prevPower)
		{
			open = true;
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
		nbt.setBoolean("Open", open);
		nbt.setBoolean("PrevPower", prevPower);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		open = nbt.getBoolean("Open");
		prevPower = nbt.getBoolean("PrevPower");
	}
}