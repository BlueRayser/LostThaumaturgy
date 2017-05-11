package com.pengu.lostthaumaturgy.api.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.tile.TileSyncableTickable;

public class TileVisUser extends TileSyncableTickable implements IConnection
{
	public int visSuction = 0;
	public int taintSuction = 0;
	
	@Override
	public void tick()
	{
		setSuction(0);
	}
	
	public boolean getExactPureVis(float amount)
	{
		this.setVisSuction(50);
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos loc = pos.offset(facing);
			IConnection ic;
			if(!getConnectable(facing) || (ic = WorldUtil.cast(world.getTileEntity(loc), IConnection.class)) == null || !ic.isVisConduit() && !ic.isVisSource() || ic.getPureVis() < amount)
				continue;
			ic.setPureVis(ic.getPureVis() - amount);
			return true;
		}
		return false;
	}
	
	public float getAvailablePureVis(float amount)
	{
		this.setVisSuction(50);
		float gatheredVis = 0.0f;
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos loc = pos.offset(facing);
			if(!this.getConnectable(facing))
				continue;
			IConnection ic = WorldUtil.cast(world.getTileEntity(loc), IConnection.class);
			if(ic != null && (ic.isVisConduit() || ic.isVisSource()))
			{
				float sucked = Math.min(amount - gatheredVis, ic.getPureVis());
				if(sucked < 0.001f)
				{
					sucked = 0.0f;
				}
				gatheredVis += sucked;
				ic.setPureVis(ic.getPureVis() - sucked);
			}
			if(gatheredVis >= amount)
				break;
		}
		return Math.min(gatheredVis, amount);
	}
	
	public float getAvailableTaintedVis(float amount)
	{
		this.setTaintSuction(50);
		float gatheredVis = 0.0f;
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos loc = pos.offset(facing);
			if(!this.getConnectable(facing))
				continue;
			IConnection ic = WorldUtil.cast(world.getTileEntity(loc), IConnection.class);
			if(ic != null && (ic.isVisConduit() || ic.isVisSource()))
			{
				float sucked = Math.min(amount - gatheredVis, ic.getTaintedVis());
				if(sucked < 0.001f)
				{
					sucked = 0.0f;
				}
				gatheredVis += sucked;
				ic.setTaintedVis(ic.getTaintedVis() - sucked);
			}
			if(gatheredVis >= amount)
				break;
		}
		return Math.min(gatheredVis, amount);
	}
	
	protected boolean gettingPower()
	{
		return world.isBlockIndirectlyGettingPowered(pos) > 0;
	}
	
	@Override
	public boolean getConnectable(EnumFacing face)
	{
		return false;
	}
	
	@Override
	public boolean isVisSource()
	{
		return false;
	}
	
	@Override
	public boolean isVisConduit()
	{
		return false;
	}
	
	@Override
	public float[] subtractVis(float amount)
	{
		return new float[] { 0.0f, 0.0f };
	}
	
	@Override
	public float getPureVis()
	{
		return 0.0f;
	}
	
	@Override
	public void setPureVis(float amount)
	{
	}
	
	@Override
	public float getTaintedVis()
	{
		return 0.0f;
	}
	
	@Override
	public float getMaxVis()
	{
		return 0.0f;
	}
	
	@Override
	public void setTaintedVis(float amount)
	{
	}
	
	@Override
	public int getVisSuction(BlockPos loc)
	{
		return this.visSuction;
	}
	
	@Override
	public void setVisSuction(int suction)
	{
		this.visSuction = suction;
	}
	
	@Override
	public int getTaintSuction(BlockPos loc)
	{
		return this.taintSuction;
	}
	
	@Override
	public void setTaintSuction(int suction)
	{
		this.taintSuction = suction;
	}
	
	@Override
	public void setSuction(int suction)
	{
		this.visSuction = suction;
		this.taintSuction = suction;
	}
	
	@Override
	public int getSuction(BlockPos loc)
	{
		return Math.max(this.visSuction, this.taintSuction);
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