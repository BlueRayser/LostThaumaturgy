package com.pengu.lostthaumaturgy.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
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
		
		if(Math.round(prevdisplayPure) != Math.round(displayPure) || Math.round(prevdisplayTaint) != Math.round(displayTaint))
		{
			prevdisplayPure = displayPure;
			prevdisplayTaint = displayTaint;
			sync();
		}
		
		calculateSuction();
		
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_PURITY)))
		{
			if(visSuction < 15)
				setVisSuction(15);
		}else
		{
			if(taintSuction < 15)
				setTaintSuction(15);
		}
		
		if(getSuction(null) > 0)
			equalizeWithNeighbours();
		
		if(displayPure != pureVis || displayTaint != taintedVis)
		{
			displayPure = pureVis;
			displayTaint = taintedVis;
		}
		
		if(displayTaint + displayPure < .1F)
		{
			displayTaint = 0.0f;
			displayPure = 0.0f;
		}
		
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_PURITY)))
		{
			
			if(visStore < 40 + stack * 4 && pureVis >= .025F)
			{
				visStore = (short) (visStore + 1);
				pureVis -= .025f;
				stack = 0;
				int up = 1;
				TileEntity te = world.getTileEntity(pos.up(up));
				
				while(te != null && te instanceof TileVisFilter && pos.getY() + stack + up < world.getHeight())
				{
					stack = (short) (stack + 1);
					up++;
					te = world.getTileEntity(pos.up(up));
				}
				
				if(visStore % 16 == 0)
					HCNetwork.manager.sendToAllAround(new PacketFXWisp2((float) getPos().getX() + 0.5f, (float) getPos().getY() + stack + .8F, (float) getPos().getZ() + 0.5f, (float) getPos().getX() + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()), (float) getPos().getY() + 3.0f + (float) stack + world.rand.nextFloat(), (float) getPos().getZ() + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()), 1.5F, world.rand.nextInt(5)), getSyncPoint(50));
			}
			
			if(visStore > 0 && visStore >= 40 - stack)
			{
				SIAuraChunk ac = (SIAuraChunk) AuraTicker.getAuraChunkFromBlockCoords(world, pos);
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
					stack = (short) (stack + 1);
					up++;
					te = world.getTileEntity(pos.up(up));
				}
				
				if(taintedStore % 16 == 0)
				{
					PacketFXWisp2 ef = new PacketFXWisp2((float) getPos().getX() + 0.5f, (float) getPos().getY() + stack + .8F, (float) getPos().getZ() + 0.5f, (float) getPos().getX() + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()), (float) getPos().getY() + 3.0f + (float) stack + world.rand.nextFloat(), (float) getPos().getZ() + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()), 1.5F, 5);
					HCNetwork.manager.sendToAllAround(ef, getSyncPoint(50));
				}
			}
			
			if(taintedStore >= 40 + stack * 4)
			{
				SIAuraChunk ac = (SIAuraChunk) AuraTicker.getAuraChunkFromBlockCoords(world, pos);
				if(ac != null)
				{
					taintedStore = 0;
					ac.taint++;
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