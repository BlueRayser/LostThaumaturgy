package com.pengu.lostthaumaturgy.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import com.google.common.base.Predicate;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.math.MathHelper;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.mrdimka.hammercore.vec.Cuboid6;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;

public class TileConduit extends TileSyncableTickable implements IConnection, Predicate<EnumFacing>
{
	public float pureVis = 0.0f;
	public float taintedVis = 0.0f;
	public float maxVis = 4.0f;
	float fillAmount = 4.0f;
	public float displayPure;
	public float displayTaint;
	public float prevdisplayPure;
	public float prevdisplayTaint;
	public int visSuction = 0;
	public int taintSuction = 0;
	
	public Cuboid6[] hitboxes = null;
	
	public void tick()
	{
		if(hitboxes == null || hitboxes.length == 0) rebake();
		
		if(ticksExisted % 20 == 0) rebake();
		if(world.isRemote) return;
		
		int suction1 = visSuction, suction2 = taintSuction;
		calculateSuction();
		if(getSuction(null) > 0) equalizeWithNeighbours();
		
		boolean sync = false;
		
		if(suction1 != visSuction || suction2 != taintSuction)
		{
			sync = true;
		}
		
		if(prevdisplayPure != displayPure || prevdisplayTaint != displayTaint)
		{
			prevdisplayPure = displayPure;
			prevdisplayTaint = displayTaint;
			sync = true;
		}
		
		if(displayPure != pureVis || displayTaint != taintedVis)
		{
			displayPure = pureVis;
			displayTaint = taintedVis;
		}
		
		if(displayTaint + displayPure < .1F)
		{
			displayTaint = 0F;
			displayPure = 0F;
		}
		
		if(sync) sync();
	}
	
	public void rebake()
	{
		double bp = 6 / 16D;
		double ep = 10 / 16D;
		
		List<Cuboid6> cuboids = new ArrayList<>();
		cuboids.add(new Cuboid6(bp, bp, bp, ep, ep, ep));
		
		for(EnumFacing facing : EnumFacing.VALUES) if(apply(facing))
		{
			if(facing == EnumFacing.UP) cuboids.add(new Cuboid6(6 / 16D, 10 / 16D, 6 / 16D, 10 / 16D, 1, 10 / 16D));
			if(facing == EnumFacing.DOWN) cuboids.add(new Cuboid6(6 / 16D, 0, 6 / 16D, 10 / 16D, 6 / 16D, 10 / 16D));
			if(facing == EnumFacing.EAST) cuboids.add(new Cuboid6(10 / 16D, 6 / 16D, 6 / 16D, 1, 10 / 16D, 10 / 16D));
			if(facing == EnumFacing.WEST) cuboids.add(new Cuboid6(0, 6 / 16D, 6 / 16D, 6 / 16D, 10 / 16D, 10 / 16D));
			if(facing == EnumFacing.SOUTH) cuboids.add(new Cuboid6(6 / 16D, 6 / 16D, 10 / 16D, 10 / 16D, 10 / 16D, 1));
			if(facing == EnumFacing.NORTH) cuboids.add(new Cuboid6(6 / 16D, 6 / 16D, 0, 10 / 16D, 10 / 16D, 6 / 16D));
		}
		
		hitboxes = cuboids.toArray(new Cuboid6[0]);
	}
	
	protected void calculateSuction()
	{
		setSuction(0);
		for(EnumFacing f : EnumFacing.VALUES)
		{
			TileEntity te = world.getTileEntity(pos.offset(f));
			if(!getConnectable(f) || te == null || !(te instanceof IConnection)) continue;
			IConnection ic2 = (IConnection) te;
			if(getVisSuction(null) < ic2.getVisSuction(pos) - 1) setVisSuction(ic2.getVisSuction(pos) - 1);
			if(getTaintSuction(null) >= ic2.getTaintSuction(pos) - 1) continue;
			setTaintSuction(ic2.getTaintSuction(pos) - 1);
		}
	}
	
	protected void equalizeWithNeighbours()
	{
		for(EnumFacing f : EnumFacing.VALUES)
		{
			TileEntity te = world.getTileEntity(pos.offset(f));
			if(!getConnectable(f) || te == null || !(te instanceof IConnection))
				continue;
			IConnection ent = (IConnection) te;
			if(pureVis + taintedVis >= maxVis || getVisSuction(null) <= ent.getVisSuction(pos) && getTaintSuction(null) <= ent.getTaintSuction(pos))
				continue;
			float[] results = new float[] { 0.0f, 0.0f };
			float qq = Math.min((ent.getPureVis() + ent.getTaintedVis()) / 4.0f, fillAmount);
			results = ent.subtractVis(Math.min(qq, maxVis - (pureVis + taintedVis)));
			if(getVisSuction(null) > ent.getVisSuction(pos))
			{
				pureVis += results[0];
			} else
			{
				ent.setPureVis(results[0] + ent.getPureVis());
			}
			if(getTaintSuction(null) > ent.getTaintSuction(pos))
			{
				taintedVis += results[1];
				continue;
			}
			ent.setTaintedVis(results[1] + ent.getTaintedVis());
		}
		pureVis = (float) MathHelper.clip(pureVis,  0F, maxVis);
		taintedVis = (float) MathHelper.clip(taintedVis, 0F, maxVis);
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
		pureVis = nbt.getFloat("PureVis");
		taintedVis = nbt.getFloat("TaintedVis");
		displayPure = nbt.getFloat("DisplayPure");
		displayTaint = nbt.getFloat("DisplayTaint");
		visSuction = nbt.getInteger("VisSuction");
		taintSuction = nbt.getInteger("TaintSuction");
	}
	
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("PureVis", pureVis);
		nbt.setFloat("TaintedVis", taintedVis);
		nbt.setFloat("DisplayPure", displayPure);
		nbt.setFloat("DisplayTaint", displayTaint);
		nbt.setInteger("VisSuction", visSuction);
		nbt.setInteger("TaintSuction", taintSuction);
	}
	
	@Override
	public boolean getConnectable(EnumFacing face)
	{
		return true;
	}
	
	@Override
	public boolean isVisSource()
	{
		return false;
	}
	
	@Override
	public boolean isVisConduit()
	{
		return true;
	}
	
	@Override
	public float getPureVis()
	{
		return pureVis;
	}
	
	@Override
	public void setPureVis(float amount)
	{
		pureVis = amount;
	}
	
	@Override
	public float getTaintedVis()
	{
		return taintedVis;
	}
	
	@Override
	public void setTaintedVis(float amount)
	{
		taintedVis = amount;
	}
	
	@Override
	public float getMaxVis()
	{
		return maxVis;
	}
	
	@Override
	public float[] subtractVis(float amount)
	{
		float pureAmount = amount / 2.0f;
		float taintAmount = amount / 2.0f;
		float[] result = new float[] { 0.0f, 0.0f };
		if(amount < 0.001f) return result;
		if(pureVis < pureAmount) pureAmount = pureVis;
		if(taintedVis < taintAmount) taintAmount = taintedVis;
		if(pureAmount < amount / 2.0f && taintAmount == amount / 2.0f) taintAmount = Math.min(amount - pureAmount, taintedVis);
		else if(taintAmount < amount / 2.0f && pureAmount == amount / 2.0f) pureAmount = Math.min(amount - taintAmount, pureVis);
		pureVis -= pureAmount;
		taintedVis -= taintAmount;
		result[0] = pureAmount;
		result[1] = taintAmount;
		return result;
	}
	
	@Override
	public int getVisSuction(BlockPos loc)
	{
		return visSuction;
	}
	
	@Override
	public void setVisSuction(int suction)
	{
		visSuction = suction;
	}
	
	@Override
	public int getTaintSuction(BlockPos loc)
	{
		return taintSuction;
	}
	
	@Override
	public void setTaintSuction(int suction)
	{
		taintSuction = suction;
	}
	
	@Override
	public void setSuction(int suction)
	{
		visSuction = suction;
		taintSuction = suction;
	}
	
	@Override
	public int getSuction(BlockPos loc)
	{
		return Math.max(visSuction, taintSuction);
	}
	
	@Override
    public boolean apply(EnumFacing input)
    {
		BlockPos tpos = pos.offset(input);
		if(world.isBlockLoaded(tpos))
		{
			IConnection c = WorldUtil.cast(world.getTileEntity(tpos), IConnection.class);
			if(c != null) return getConnectable(input) && c.getConnectable(input.getOpposite());
		}
		return false;
    }
}