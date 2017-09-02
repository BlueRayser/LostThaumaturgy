package com.pengu.lostthaumaturgy.core.tile;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.api.tiles.IBellowBoostable;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;

public class TileBellows extends TileSyncableTickable implements IConnection
{
	public float scale = 1;
	boolean direction = false;
	boolean firstrun = true;
	public int orientation = 0;
	public final int forceSuction;
	
	public TileBellows(int baseSuction)
	{
		forceSuction = baseSuction;
	}
	
	public TileBellows()
	{
		this(10);
	}
	
	public boolean isBoosting(TileEntity te)
	{
		return this.isBoosting() && te.getPos().equals(pos.offset(EnumFacing.VALUES[(orientation + 2) % EnumFacing.VALUES.length].getOpposite()));
	}
	
	protected boolean gettingPower()
	{
		return world.isBlockIndirectlyGettingPowered(pos) > 0;
	}
	
	public boolean isBoosting()
	{
		if(gettingPower())
			return false;
		
		EnumFacing baseFace = EnumFacing.VALUES[(orientation + 2) % EnumFacing.VALUES.length];
		TileEntity te = world.getTileEntity(pos.offset(baseFace.getOpposite()));
		if(te instanceof IBellowBoostable && ((IBellowBoostable) te).canBeBoosted(baseFace))
			return true;
		if(te != null && (te instanceof TileCrucible ||
		// te instanceof TileArcaneFurnace ||
		        te instanceof TileConduit || te instanceof TileVisPump && !this.gettingPower() || te instanceof TileVisTank && !this.gettingPower()))
			return true;
		return false;
	}
	
	public void tick()
	{
		if(this.firstrun && this.isBoosting())
		{
			this.scale = 0.35f + world.rand.nextFloat() * 0.55f;
		}
		this.firstrun = false;
		if(this.isBoosting())
		{
			if(this.scale > 0.35f && !this.direction)
				this.scale -= 0.075f;
			if(this.scale <= 0.35f && !this.direction)
				this.direction = true;
		} else
			this.direction = true;
		if(this.scale < 1.0f && this.direction)
			this.scale += 0.025f;
		if(this.scale >= 1.0f && this.direction)
			this.direction = false;
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
		this.orientation = nbt.getShort("Orientation");
	}
	
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setShort("Orientation", (short) this.orientation);
	}
	
	// @Override
	// public boolean rotate()
	// {
	// ++this.orientation;
	// if(this.orientation > 3)
	// {
	// this.orientation -= 4;
	// }
	// return true;
	// }
	
	@Override
	public boolean getConnectable(EnumFacing face)
	{
		return face.getOpposite().ordinal() == orientation + 2;
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("facing", EnumFacing.VALUES[orientation + 2]);
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
		return new float[] { 0, 0 };
	}
	
	@Override
	public float getPureVis()
	{
		return 0;
	}
	
	@Override
	public void setPureVis(float amount)
	{
	}
	
	@Override
	public float getTaintedVis()
	{
		return 0;
	}
	
	@Override
	public float getMaxVis()
	{
		return 0;
	}
	
	@Override
	public void setTaintedVis(float amount)
	{
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
		return this.gettingPower() ? 0 : forceSuction;
	}
}