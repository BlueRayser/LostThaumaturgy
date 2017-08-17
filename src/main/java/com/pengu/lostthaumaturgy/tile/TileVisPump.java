package com.pengu.lostthaumaturgy.tile;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.tile.IMalfunctionable;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.api.tiles.ConnectionManager;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;

public class TileVisPump extends TileSyncableTickable implements IConnection, IMalfunctionable
{
	public int malfunctionTime = 0;
	public float pureVis = 0.0f;
	public float taintedVis = 0.0f;
	public float maxVis = 4.0f;
	float fillAmount = 1.0f;
	protected int baseSuction = 20;
	public EnumFacing orientation = EnumFacing.NORTH;
	
	public void tick()
	{
		if(malfunctionTime > 0)
			malfunctionTime--;
		
		if(gettingPower() || malfunctionTime > 0)
		{
			ticksExisted--;
			return;
		}
		
		if(world.isRemote)
			return;
		
		IConnection c = ConnectionManager.getConnection(world, pos, orientation);
		
		if(c == null || !getConnectable(orientation))
			return;
		
		if(pureVis + taintedVis < maxVis && c != null && c.isVisConduit() || c.isVisSource())
		{
			float suckamount = Math.min(this.fillAmount, this.maxVis - (this.pureVis + this.taintedVis));
			float[] yum = c.subtractVis(suckamount);
			this.pureVis += yum[0];
			this.taintedVis += yum[1];
		}
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("output", orientation.getOpposite().getName());
		properties.put("input", orientation.getName());
	}
	
	public void readNBT(NBTTagCompound nbttagcompound)
	{
		this.pureVis = nbttagcompound.getFloat("PureVis");
		this.taintedVis = nbttagcompound.getFloat("TaintedVis");
		this.orientation = EnumFacing.VALUES[nbttagcompound.getByte("Orientation") % EnumFacing.VALUES.length];
		malfunctionTime = nbttagcompound.getInteger("MalfunctionTime");
	}
	
	public void writeNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setFloat("PureVis", this.pureVis);
		nbttagcompound.setFloat("TaintedVis", this.taintedVis);
		nbttagcompound.setByte("Orientation", (byte) orientation.ordinal());
		nbttagcompound.setInteger("MalfunctionTime", malfunctionTime);
	}
	
	@Override
	public boolean getConnectable(EnumFacing face)
	{
		return face.getAxis() == orientation.getAxis();
	}
	
	@Override
	public boolean isVisSource()
	{
		return true;
	}
	
	@Override
	public boolean isVisConduit()
	{
		return false;
	}
	
	@Override
	public float getPureVis()
	{
		return this.pureVis;
	}
	
	@Override
	public void setPureVis(float amount)
	{
		this.pureVis = amount;
	}
	
	@Override
	public float getTaintedVis()
	{
		return this.taintedVis;
	}
	
	@Override
	public void setTaintedVis(float amount)
	{
		this.taintedVis = amount;
	}
	
	@Override
	public float getMaxVis()
	{
		return this.maxVis;
	}
	
	@Override
	public float[] subtractVis(float amount)
	{
		float pureAmount = amount / 2;
		float taintAmount = amount / 2;
		float[] result = new float[] { 0, 0 };
		if(amount < 0.001F)
			return result;
		if(this.pureVis < pureAmount)
			pureAmount = this.pureVis;
		if(this.taintedVis < taintAmount)
			taintAmount = this.taintedVis;
		
		// Is this block even alive??
		if(pureAmount < amount / 2 && taintAmount == amount / 2)
			taintAmount = Math.min(amount - pureAmount, this.taintedVis);
		else if(taintAmount < amount / 2 && pureAmount == amount / 2)
			pureAmount = Math.min(amount - taintAmount, this.pureVis);
		
		this.pureVis -= pureAmount;
		this.taintedVis -= taintAmount;
		result[0] = pureAmount;
		result[1] = taintAmount;
		return result;
	}
	
	@Override
	public int getVisSuction(BlockPos loc)
	{
		return this.getSuction(loc);
	}
	
	@Override
	public void setVisSuction(int suction)
	{
	}
	
	@Override
	public int getTaintSuction(BlockPos loc)
	{
		return this.getSuction(loc);
	}
	
	@Override
	public void setTaintSuction(int suction)
	{
	}
	
	@Override
	public void setSuction(int suction)
	{
	}
	
	@Override
	public int getSuction(BlockPos loc)
	{
		if(loc == null)
			loc = pos.offset(orientation);
		if(this.gettingPower())
			return 0;
		int bellows = 0;
		for(int dir = 0; dir < 4; ++dir)
		{
			EnumFacing face = EnumFacing.VALUES[2 + dir];
			TileBellows te = WorldUtil.cast(world.getTileEntity(pos.offset(face)), TileBellows.class);
			if(te == null || !((TileBellows) te).isBoosting(this))
				continue;
			bellows += te.forceSuction;
		}
		if(loc.equals(pos.offset(orientation)))
			return baseSuction + bellows;
		return 0;
	}
	
	// @Override
	// public boolean rotate()
	// {
	// ++this.orientation;
	// if(this.orientation > 5)
	// {
	// this.orientation -= 6;
	// }
	// return true;
	// }
	
	public boolean gettingPower()
	{
		return world.isBlockIndirectlyGettingPowered(pos) > 0;
	}
	
	@Override
	public void causeGeneralMalfunction()
	{
		malfunctionTime = 40 + getRNG().nextInt(100);
	}
}