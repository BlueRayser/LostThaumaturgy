package com.pengu.lostthaumaturgy.core.tile;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;
import com.pengu.lostthaumaturgy.client.gui.GuiAuxiliumTable;
import com.pengu.lostthaumaturgy.core.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.core.items.ItemResearch;
import com.pengu.lostthaumaturgy.core.items.ItemResearch.EnumResearchItemType;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.inventory.ContainerAuxiliumTable;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWispColor;

public class TileAuxiliumTable extends TileVisUser
{
	public InventoryNonTile inventory = new InventoryNonTile(2);
	public float progress = 0;
	public float visConsumed = 0;
	public int lastBoost = 0;
	
	@Override
	public void tick()
	{
		boolean spawn = world.rand.nextInt(35) == 0;
		
		lastBoost = Math.min(getBoost(), 27);
		
		setSuction(0);
		
		if(world.isRemote)
			return;
		
		int fragmentCount = 9;
		if(lastBoost > 0)
			fragmentCount = Math.round((27 - lastBoost) / 3F);
		if(fragmentCount == 0)
			fragmentCount = 1;
		
		boolean canOperate = !inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() == ItemsLT.DISCOVERY && ItemResearch.getType(inventory.getStackInSlot(0)) == EnumResearchItemType.FRAGMENT && inventory.getStackInSlot(0).getCount() >= fragmentCount;
		boolean canOutput = inventory.getStackInSlot(1).isEmpty() || (canOperate && inventory.getStackInSlot(1).getItem() == ItemsLT.DISCOVERY && ItemResearch.getType(inventory.getStackInSlot(1)) == EnumResearchItemType.THEORY && ItemResearch.getFromStack(inventory.getStackInSlot(1)) == ItemResearch.getFromStack(inventory.getStackInSlot(0)));
		
		if(canOperate && canOutput && !world.isRemote)
		{
			if(visConsumed < 32F)
				visConsumed += getAvailablePureVis(32 - visConsumed);
			else
				spawn = true;
			
			if(visConsumed >= 32F && world.rand.nextInt(27 * 2) < lastBoost + 6)
			{
				progress += 0.005F;
				sync();
			}
			
			if(progress >= 1F)
			{
				progress = 0;
				visConsumed = 0;
				
				if(inventory.getStackInSlot(1).isEmpty())
					inventory.setInventorySlotContents(1, ItemResearch.create(ItemResearch.getFromStack(inventory.getStackInSlot(0)), EnumResearchItemType.THEORY));
				else
					inventory.getStackInSlot(1).grow(1);
				
				inventory.getStackInSlot(0).shrink(fragmentCount);
				
				sync();
			}
		} else if(visConsumed > 0F)
		{
			visConsumed *= 0.99F;
			if(visConsumed < 0.01F)
				visConsumed = 0;
			sync();
		} else if(progress > 0F)
		{
			progress -= 0.01F;
			sync();
		}
		
		if(lastBoost > 0 && !world.isRemote && spawn)
			spawnBoostParticles();
	}
	
	public void spawnBoostParticles()
	{
		int cr = 0;
		Random rand = world.rand;
		for(int x = -2; x < 3; ++x)
		{
			for(int y = -2; y < 3; ++y)
				for(int z = -2; z < 3; ++z)
				{
					BlockPos boostPos = pos.add(x, y, z);
					
					if(world.isBlockLoaded(boostPos))
					{
						Block b = world.getBlockState(boostPos).getBlock();
						if(b instanceof BlockOreCrystal && rand.nextInt(3) == 0)
						{
							cr++;
							if(cr == 27)
								return;
							
							int col = ((BlockOreCrystal) b).getCrystalColor();
							HCNetwork.manager.sendToAllAround(new PacketFXWispColor(boostPos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3, boostPos.getY() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3, boostPos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3, pos.getX() + .5, pos.getY() + (visConsumed >= 32F ? 1 : .5), pos.getZ() + .5, .4F, col), getSyncPoint(40));
						}
					}
				}
		}
	}
	
	public int getBoost()
	{
		int boost = 0;
		
		for(int x = -2; x < 3; ++x)
		{
			for(int y = -2; y < 3; ++y)
				for(int z = -2; z < 3; ++z)
				{
					BlockPos boostPos = pos.add(x, y, z);
					
					if(world.isBlockLoaded(boostPos))
					{
						Block b = world.getBlockState(boostPos).getBlock();
						if(b instanceof BlockOreCrystal)
							boost += 1;
					}
				}
		}
		
		return boost;
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiAuxiliumTable(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerAuxiliumTable(this, player);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("Progress", progress);
		NBTTagCompound tag = new NBTTagCompound();
		inventory.writeToNBT(tag);
		nbt.setTag("Items", tag);
		nbt.setFloat("VisConsumed", visConsumed);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		visConsumed = nbt.getFloat("VisConsumed");
		progress = nbt.getFloat("Progress");
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
	}
}