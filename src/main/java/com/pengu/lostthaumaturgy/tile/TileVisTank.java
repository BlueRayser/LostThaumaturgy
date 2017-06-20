package com.pengu.lostthaumaturgy.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import com.google.common.base.Predicate;
import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.CapabilityVisConnection;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;

public class TileVisTank extends TileSyncableTickable implements IConnection, Predicate<EnumFacing>
{
	public float pureVis = 0.0f;
	public float taintedVis = 0.0f;
	float fillAmount = 1.0f;
	int wait;
	public float displayPure;
	public float displayTaint;
	public float prevdisplayPure;
	public float prevdisplayTaint;
	public int visSuction = 10;
	public int taintSuction = 10;
	public int breakchance = 999;
	public boolean canBreak = true;
	
	public void tick()
	{
		if(world.isRemote)
			return;
		--wait;
		if(wait <= 0)
		{
			wait = 10;
			calculateSuction();
			
			if(taintedVis > getMaxVis() * 0.9f)
			{
				if(world.rand.nextInt(breakchance) == 123 && canBreak)
				{
					AuraTicker.taintExplosion(world, pos.getX(), pos.getY(), pos.getZ());
					world.destroyBlock(pos, true);
				} else if(world.rand.nextInt(breakchance / 8) == 42)
					HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":creaking", pos, .75F, 1, SoundCategory.BLOCKS);
			}
		}
		
		equalizeWithNeighbours();
		
		if(pureVis - Math.floor(pureVis) > 0.97F)
			pureVis = (float) Math.ceil(pureVis);
		if(taintedVis - Math.floor(taintedVis) > 0.97F)
			taintedVis = (float) Math.ceil(taintedVis);
		
		if(pureVis - Math.floor(pureVis) < 0.005F)
			pureVis = (float) Math.floor(pureVis);
		if(taintedVis - Math.floor(taintedVis) < 0.005F)
			taintedVis = (float) Math.floor(taintedVis);
		
		if(prevdisplayPure != displayPure || prevdisplayTaint != displayTaint)
		{
			sync();
			prevdisplayPure = displayPure;
			prevdisplayTaint = displayTaint;
		}
		
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
	}
	
	public void calculateSuction()
	{
		setSuction(10);
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			if(facing.getAxis() == Axis.Y || !world.isBlockLoaded(pos.offset(facing)))
				continue;
			TileEntity te = world.getTileEntity(pos.offset(facing));
			
			// bellows
			if(!getConnectable(facing) || te == null || !(te instanceof TileBellows) || !((TileBellows) te).isBoosting(this))
				continue;
			setSuction(getSuction(null) + 10);
		}
	}
	
	protected void equalizeWithNeighbours()
	{
		TileEntity ts;
		float stackpureVis = pureVis;
		float stacktaintedVis = taintedVis;
		float stackmaxVis = getMaxVis();
		int count = 1;
		while((ts = world.getTileEntity(pos.up(count))) instanceof TileVisTank)
		{
			stackpureVis += ((TileVisTank) ts).pureVis;
			stacktaintedVis += ((TileVisTank) ts).taintedVis;
			stackmaxVis += ((TileVisTank) ts).getMaxVis();
			++count;
		}
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			TileEntity te = world.getTileEntity(pos.offset(facing));
			if(!getConnectable(facing) || te == null || !(te instanceof IConnection))
				continue;
			IConnection ent = (IConnection) te;
			if(!ent.getConnectable(facing.getOpposite()) || te instanceof TileVisTank || stackpureVis + stacktaintedVis >= stackmaxVis || getVisSuction(null) <= ent.getVisSuction(pos) && getTaintSuction(null) <= ent.getTaintSuction(pos))
				continue;
			float[] results = new float[] { 0.0f, 0.0f };
			results = ent.subtractVis(Math.min(fillAmount, stackmaxVis - (stackpureVis + stacktaintedVis)));
			if(getVisSuction(null) > ent.getVisSuction(pos))
			{
				stackpureVis += results[0];
			} else
			{
				ent.setPureVis(results[0] + ent.getPureVis());
			}
			if(getTaintSuction(null) > ent.getTaintSuction(pos))
			{
				stacktaintedVis += results[1];
				continue;
			}
			ent.setTaintedVis(results[1] + ent.getTaintedVis());
		}
		float total = stackpureVis + stacktaintedVis;
		if(Math.round(total) >= stackmaxVis)
			setSuction(0);
		float pratio = stackpureVis / total;
		float tratio = stacktaintedVis / total;
		count = 0;
		boolean clearrest = false;
		while((ts = world.getTileEntity(pos.up(count))) instanceof TileVisTank)
		{
			if(clearrest)
			{
				((TileVisTank) ts).pureVis = 0.0f;
				((TileVisTank) ts).taintedVis = 0.0f;
			} else if(total <= ((TileVisTank) ts).getMaxVis())
			{
				((TileVisTank) ts).pureVis = stackpureVis;
				((TileVisTank) ts).taintedVis = stacktaintedVis;
				clearrest = true;
			} else
			{
				((TileVisTank) ts).pureVis = ((TileVisTank) ts).getMaxVis() * pratio;
				((TileVisTank) ts).taintedVis = ((TileVisTank) ts).getMaxVis() * tratio;
				stackpureVis -= ((TileVisTank) ts).pureVis;
				stacktaintedVis -= ((TileVisTank) ts).taintedVis;
			}
			total = stackpureVis + stacktaintedVis;
			++count;
		}
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
		pureVis = nbt.getFloat("PureVis");
		taintedVis = nbt.getFloat("TaintedVis");
		displayPure = nbt.getFloat("DisplayPure");
		displayTaint = nbt.getFloat("DisplayTaint");
		breakchance = nbt.getInteger("BreakChance");
	}
	
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("PureVis", pureVis);
		nbt.setFloat("TaintedVis", taintedVis);
		nbt.setFloat("MaxVis", getMaxVis());
		nbt.setFloat("DisplayPure", displayPure);
		nbt.setFloat("DisplayTaint", displayTaint);
		nbt.setInteger("BreakChance", breakchance);
	}
	
	@Override
	public boolean getConnectable(EnumFacing face)
	{
		return true;
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
		return 500.0f;
	}
	
	@Override
	public float[] subtractVis(float amount)
	{
		float pureAmount = amount / 2.0f;
		float taintAmount = amount / 2.0f;
		float[] result = new float[] { 0.0f, 0.0f };
		if(amount < 0.001f)
		{
			return result;
		}
		if(pureVis < pureAmount)
		{
			pureAmount = pureVis;
		}
		if(taintedVis < taintAmount)
		{
			taintAmount = taintedVis;
		}
		if(pureAmount < amount / 2.0f && taintAmount == amount / 2.0f)
		{
			taintAmount = Math.min(amount - pureAmount, taintedVis);
		} else if(taintAmount < amount / 2.0f && pureAmount == amount / 2.0f)
		{
			pureAmount = Math.min(amount - taintAmount, pureVis);
		}
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
		if(!getConnectable(input))
			return false;
		BlockPos tpos = pos.offset(input);
		if(world.isBlockLoaded(tpos))
		{
			IConnection c2 = WorldUtil.cast(world.getTileEntity(tpos), IConnection.class);
			if(c2 instanceof TileVisTank)
				return false;
			if(c2 == null || !c2.getConnectable(input.getOpposite()))
				return false;
		}
		return true;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityVisConnection.VIS)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityVisConnection.VIS)
			return (T) this;
		return super.getCapability(capability, facing);
	}
}