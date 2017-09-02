package com.pengu.lostthaumaturgy.core.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.core.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;

public class TileVisFilter extends TileConduit implements IUpgradable
{
	public short taintedStore;
	public short visStore;
	public short stack;
	public int[] upgrades = { -1 };
	
	@Override
	public void tick()
	{
		if(world.isRemote)
			return;
		
		if(prevdisplayPure != displayPure || prevdisplayTaint != displayTaint)
		{
			prevdisplayPure = displayPure;
			prevdisplayTaint = displayTaint;
			sendChangesToNearby();
		}
		
		int suction1 = visSuction, suction2 = taintSuction;
		calculateSuction();
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_PURITY)))
		{
			if(visSuction < 15)
				setVisSuction(15);
		} else
		{
			if(taintSuction < 15)
				setTaintSuction(15);
		}
		if(getSuction(null) > 0)
			equalizeWithNeighbours();
		if(suction1 != visSuction || suction2 != taintSuction)
			sendChangesToNearby();
		
		if(displayPure != pureVis || displayTaint != taintedVis)
		{
			displayPure = pureVis;
			displayTaint = taintedVis;
		}
		
		if(displayTaint + displayPure < .1F)
		{
			displayTaint = 0;
			displayPure = 0;
			sendChangesToNearby();
		}
		
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_PURITY)))
		{
			if(visStore <= 40 - stack && pureVis >= .025F)
			{
				visStore++;
				pureVis -= .025f;
				stack = 0;
				int up = 1;
				TileEntity te = world.getTileEntity(pos.up(up));
				
				while(te != null && te instanceof TileVisFilter && pos.getY() + stack + up < world.getHeight())
				{
					stack++;
					up++;
					te = world.getTileEntity(pos.up(up));
				}
				
				if(visStore % 8 == 0)
					HCNetwork.manager.sendToAllAround(new PacketFXWisp2((float) getPos().getX() + 0.5f, (float) getPos().getY() + stack + .8F, (float) getPos().getZ() + 0.5f, (float) getPos().getX() + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()), (float) getPos().getY() + 3.0f + (float) stack + world.rand.nextFloat(), (float) getPos().getZ() + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()), 1.5F, world.rand.nextInt(5)), getSyncPoint(50));
				
				sendChangesToNearby();
			}
			
			if(visStore > 0 && visStore > 40 - stack)
			{
				AtmosphereChunk ac = (AtmosphereChunk) AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
				if(ac != null)
				{
					visStore = 0;
					ac.vis++;
				}
			}
			
		} else
		{
			if(taintedStore < 40 + stack * 4 && taintedVis >= .025F)
			{
				taintedStore = (short) (taintedStore + 1);
				taintedVis -= .025f;
				stack = 0;
				int up = 1;
				TileEntity te = world.getTileEntity(pos.up(up));
				
				while(te != null && te instanceof TileVisFilter && pos.getY() + stack + up < world.getHeight())
				{
					stack++;
					up++;
					te = world.getTileEntity(pos.up(up));
				}
				
				if(taintedStore % 16 == 0)
				{
					PacketFXWisp2 ef = new PacketFXWisp2((float) getPos().getX() + 0.5f, (float) getPos().getY() + stack + .8F, (float) getPos().getZ() + 0.5f, (float) getPos().getX() + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()), (float) getPos().getY() + 3.0f + (float) stack + world.rand.nextFloat(), (float) getPos().getZ() + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()), 1.5F, 5);
					HCNetwork.manager.sendToAllAround(ef, getSyncPoint(50));
				}
				
				sendChangesToNearby();
			}
			
			if(taintedStore >= 40 + stack * 4)
			{
				AtmosphereChunk ac = (AtmosphereChunk) AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
				if(ac != null)
				{
					taintedStore = 0;
					ac.taint++;
					ac.radiation += .0001F;
				}
			}
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setShort("TaintedStore", taintedStore);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		taintedStore = nbt.getShort("TaintedStore");
	}
	
	@Override
	public boolean getConnectable(EnumFacing face)
	{
		return face.getAxis() != Axis.Y;
	}
	
	@Override
	public int[] getUpgrades()
	{
		return upgrades;
	}
	
	@Override
	public boolean canAcceptUpgrade(int type)
	{
		if(type != ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_PURITY))
			return false;
		if(hasUpgrade(type))
			return false;
		return true;
	}
}