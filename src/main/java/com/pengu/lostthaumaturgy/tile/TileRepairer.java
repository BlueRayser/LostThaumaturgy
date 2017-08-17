package com.pengu.lostthaumaturgy.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;

import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.api.restorer.ICustomRepairable;
import com.pengu.lostthaumaturgy.api.restorer.RestorerManager;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;
import com.pengu.lostthaumaturgy.client.gui.GuiRepairer;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.init.SoundEventsLT;
import com.pengu.lostthaumaturgy.inventory.ContainerRepairer;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.net.PacketSmallGreenFlameFX;

public class TileRepairer extends TileVisUser implements IUpgradable, ISidedInventory
{
	public final InventoryNonTile inventory = new InventoryNonTile(12);
	public float sucked = 0;
	public float currentVis = 0;
	public float maxVis = 50;
	public int boost = 0;
	private int[] upgrades = new int[] { -1 };
	public boolean worked;
	int boostDelay = 20;
	int soundDelay = 0;
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiRepairer(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerRepairer(this, player);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		AtmosphereChunk ac = AuraTicker.getAuraChunkFromBlockCoords(loc);
		worked = false;
		
		if(world.isRemote)
			return;
		
		boolean flag1 = false;
		for(int a = 0; a < 6; ++a)
		{
			ItemStack s = inventory.getStackInSlot(a);
			ICustomRepairable cr = RestorerManager.findCustomRepairable(s.getItem());
			boolean isVisRepair = cr.canRepair(s, loc);
			if(!s.isEmpty() && (s.getItem().isRepairable() || isVisRepair))
			{
				float ra = cr.getVisCost(s, loc);
				ra *= hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)) ? .8 : 1;
				if(s.isItemEnchanted())
					ra *= 1.5;
				if(currentVis > ra)
				{
					currentVis -= ra;
					flag1 = true;
					cr.attempRepair(s, loc);
					worked = true;
					if(!hasUpgrade(-1))
					{
						switch(world.rand.nextInt(4))
						{
						case 0:
						{
							HCNetwork.getManager("particles").sendToAllAround(new PacketSmallGreenFlameFX(new Vec3d(pos).addVector(.25, 1.15, .25)), getSyncPoint(32));
							break;
						}
						case 1:
						{
							HCNetwork.getManager("particles").sendToAllAround(new PacketSmallGreenFlameFX(new Vec3d(pos).addVector(.25, 1.15, .75)), getSyncPoint(32));
							break;
						}
						case 2:
						{
							HCNetwork.getManager("particles").sendToAllAround(new PacketSmallGreenFlameFX(new Vec3d(pos).addVector(.75, 1.15, .25)), getSyncPoint(32));
							break;
						}
						case 3:
						{
							HCNetwork.getManager("particles").sendToAllAround(new PacketSmallGreenFlameFX(new Vec3d(pos).addVector(.75, 1.15, .75)), getSyncPoint(32));
						}
						}
					}
				}
			}
		}
		moveRepairedItems();
		float suckLimit = .5F + .05F * (float) boost + (hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? .5F : 0);
		for(int a2 = 0; a2 < 6; ++a2)
		{
			if(inventory.getStackInSlot(a2).isEmpty())
				continue;
			if(currentVis + suckLimit > maxVis)
				break;
			currentVis += getAvailablePureVis(suckLimit);
			break;
		}
		if(flag1 && this.soundDelay <= 0)
		{
			this.soundDelay = 50;
			SoundEventsLT.TINKERING.playAt(loc, .5F, 1, SoundCategory.BLOCKS);
			if(ac != null)
				++ac.badVibes;
		}
		if(this.soundDelay > 0)
			--this.soundDelay;
		if(this.boostDelay <= 0 || this.boostDelay == 10)
		{
			if(ac != null && boost < 10 && ac.boost > 0)
			{
				++boost;
				--ac.boost;
			}
		}
		if(boostDelay <= 0)
		{
			if(boost > 0)
				--boost;
			boostDelay = 20;
		} else
			--boostDelay;
	}
	
	private void moveRepairedItems()
	{
		cc: for(int i = 0; i < 6; ++i)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack.isEmpty())
				continue;
			ICustomRepairable repairable = RestorerManager.findCustomRepairable(stack.getItem());
			if(!repairable.canRepair(stack, getLocation()))
			{
				for(int j = 6; j < 12; ++j)
				{
					ItemStack s = inventory.getStackInSlot(j);
					
					if(s.isEmpty())
					{
						inventory.setInventorySlotContents(j, stack.copy());
						inventory.setInventorySlotContents(i, ItemStack.EMPTY);
						continue cc;
					}
				}
			}
		}
	}
	
	@Override
	public int[] getUpgrades()
	{
		if(upgrades.length != 1)
			upgrades = new int[] { -1 };
		return upgrades;
	}
	
	@Override
	public boolean canAcceptUpgrade(int type)
	{
		if(type != ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE) && type != ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY))
			return false;
		if(hasUpgrade(type))
			return false;
		return true;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		NBTTagCompound tag;
		inventory.writeToNBT(tag = new NBTTagCompound());
		nbt.setTag("Items", tag);
		nbt.setIntArray("Upgrades", upgrades);
		nbt.setFloat("CurrentVis", currentVis);
		nbt.setFloat("MaxVis", maxVis);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
		upgrades = nbt.getIntArray("Upgrades");
		currentVis = nbt.getFloat("CurrentVis");
		maxVis = nbt.getFloat("MaxVis");
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}
	
	@Override
	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return inventory.decrStackSize(index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return inventory.removeStackFromSlot(index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.setInventorySlotContents(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return inventory.isUsableByPlayer(player, pos);
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index < 6 && RestorerManager.findCustomRepairable(stack.getItem()).canRepair(stack, loc);
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		inventory.clear();
	}
	
	@Override
	public String getName()
	{
		return "Thaumic Repairer";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return inventory.getAllAvaliableSlots();
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return !canInsertItem(index, stack, direction);
	}
}