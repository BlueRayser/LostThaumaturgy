package com.pengu.lostthaumaturgy.tile;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.common.ForgeHooks;

import com.google.common.base.Predicate;
import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.api.research.ResearchManager;
import com.pengu.lostthaumaturgy.client.gui.GuiStudiumTable;
import com.pengu.lostthaumaturgy.inventory.ContainerStudiumTable;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;

public class TileStudiumTable extends TileSyncableTickable
{
	public InventoryNonTile inventory = new InventoryNonTile(11);
	public float researchProgress = 0;
	public float lastBoost = 0;
	
	@Override
	public void tick()
	{
		EntityPlayer player = world.getClosestPlayer(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 4, new Predicate<Entity>()
		{
			@Override
			public boolean apply(Entity input)
			{
				if(input instanceof EntityPlayer)
				{
					if(((EntityPlayer) input).noClip)
						return false;
					RayTraceResult r = RayTracer.retrace((EntityPlayer) input, 2.3);
					if(r != null && r.typeOfHit == Type.BLOCK && r.getBlockPos().equals(pos))
						return true;
				}
				return false;
			}
		});
		
		float val = RecipesCrucible.getSmeltingValue(inventory.getStackInSlot(0));
		// val = 1;
		
		lastBoost = getBoost() + val;
		boolean spawn = world.rand.nextInt(40) == 0;
		
		if(player != null && !inventory.getStackInSlot(0).isEmpty() && canOutput())
		{
			double speed = 2.6 - player.getDistance(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
			
			ItemStack paper = inventory.getStackInSlot(1);
			if(researchProgress < 1F && !paper.isEmpty() && paper.getItem() == Items.PAPER)
			{
				if(!world.isRemote)
					researchProgress += speed / 180;
				sync();
				if(!spawn)
					spawn = world.rand.nextInt(3) == 0;
			} else if(researchProgress > 0F)
				researchProgress = (float) MathHelper.clip(researchProgress - 0.01F, 0, 1);
			
			if(researchProgress >= 1F)
			{
				researchProgress = 0;
				
				ResearchItem r = ResearchManager.chooseRandomUnresearched(inventory.getStackInSlot(0), player, Math.round(lastBoost) + 1);
				
				if(r != null && r.getComplexity() / val >= world.rand.nextFloat() * 100)
					r = null;
				if(world.rand.nextFloat() * Math.sqrt(Math.sqrt(val) * 2) <= 1.9)
					r = null;
				
				if(!world.isRemote)
					inventory.getStackInSlot(0).shrink(1);
				
				if(r != null)
				{
					if(!world.isRemote)
						paper.shrink(1);
					outputFragment(r);
				}
				
				sync();
			}
		} else if(researchProgress > 0F)
			researchProgress = (float) MathHelper.clip(researchProgress - 0.01F, 0, 1);
		
		if(lastBoost > 0 && world.isRemote && spawn)
			spawnBoostParticles();
	}
	
	public boolean canOutput()
	{
		for(int i = 2; i < inventory.getSizeInventory(); ++i)
			if(inventory.getStackInSlot(i).isEmpty())
				return true;
		return false;
	}
	
	public void outputFragment(ResearchItem research)
	{
		if(!canOutput() || world.isRemote)
			return;
		ItemStack stack = ItemResearch.create(research, EnumResearchItemType.FRAGMENT);
		for(int i = 2; i < inventory.getSizeInventory(); ++i)
			if(inventory.getStackInSlot(i).isEmpty())
			{
				inventory.setInventorySlotContents(i, stack);
				break;
			} else if(inventory.getStackInSlot(i).isItemEqual(stack) && ItemStack.areItemStackTagsEqual(stack, inventory.getStackInSlot(i)) && inventory.getStackInSlot(i).getCount() < stack.getMaxStackSize())
			{
				inventory.getStackInSlot(i).grow(1);
				break;
			}
	}
	
	public void spawnBoostParticles()
	{
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
						if(ForgeHooks.getEnchantPower(world, boostPos) > 0 && rand.nextInt(16) == 0)
							world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.getX() + .5, pos.getY() + 2, pos.getZ() + .5, (x + rand.nextFloat()) - .5, (y - rand.nextFloat() - 1.0F), (z + rand.nextFloat()) - .5);
					}
				}
		}
	}
	
	public float getBoost()
	{
		float boost = 0;
		
		for(int x = -2; x < 3; ++x)
		{
			for(int y = -2; y < 3; ++y)
				for(int z = -2; z < 3; ++z)
				{
					BlockPos boostPos = pos.add(x, y, z);
					
					if(world.isBlockLoaded(boostPos))
					{
						Block b = world.getBlockState(boostPos).getBlock();
						boost += ForgeHooks.getEnchantPower(world, boostPos);
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
		return new GuiStudiumTable(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerStudiumTable(this, player);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("ResearchProgress", researchProgress);
		NBTTagCompound tag = new NBTTagCompound();
		inventory.writeToNBT(tag);
		nbt.setTag("Items", tag);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		researchProgress = nbt.getFloat("ResearchProgress");
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
	}
}