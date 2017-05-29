package com.pengu.lostthaumaturgy.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;

import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.net.utils.NetPropertyItemStack;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;

public class TilePenguCobbleGen extends TileVisUser implements IInventory, IUpgradable
{
	public final NetPropertyItemStack generated;
	public final NetPropertyItemStack pickaxe;
	
	public InventoryNonTile inventory = new InventoryNonTile(1);
	
	public int speed = 30;
	public int timer = -1;
	
	public int cooldownTimerMax = 20;
	public int cooldownTimer = -1;
	
	public float toConsume = 0F;
	public float maxConsume = .1F;
	
	public int[] upgrades = new int[] { -1 };
	
	{
		generated = new NetPropertyItemStack(this, new ItemStack(Blocks.COBBLESTONE));
		pickaxe = new NetPropertyItemStack(this, new ItemStack(Items.IRON_PICKAXE));
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)))
		{
			speed = 15;
			cooldownTimerMax = 10;
			ItemStack item = new ItemStack(Items.DIAMOND_PICKAXE);
			if(!pickaxe.get().isItemEqual(item))
				pickaxe.set(item);
		} else
		{
			speed = 30;
			cooldownTimerMax = 20;
			ItemStack item = new ItemStack(Items.IRON_PICKAXE);
			if(!pickaxe.get().isItemEqual(item))
				pickaxe.set(item);
		}
		
		ItemStack stack = inventory.getStackInSlot(0);
		
		if((stack.isEmpty() || (stack.isItemEqual(generated.get()) && stack.getCount() < stack.getMaxStackSize())) && toConsume <= 0F && timer == -1 && cooldownTimer == -1)
			toConsume = maxConsume;
		
		if(toConsume > 0F)
		{
			toConsume -= getAvailablePureVis(toConsume);
			
			if(toConsume <= 0F && cooldownTimer == -1)
			{
				cooldownTimer = cooldownTimerMax;
				sync();
			}
		}
		
		if(cooldownTimer != -1 && cooldownTimer-- == 0)
		{
			cooldownTimer = -1;
			timer = speed;
			sync();
		}
		
		if(timer != -1 && timer-- == 0)
		{
			timer = -1;
			toConsume = 0F;
			
			if(stack.isEmpty())
				inventory.setInventorySlotContents(0, generated.get().copy());
			else
				stack.grow(1);
			
			Block block = Block.getBlockFromItem(generated.get().getItem());
			IBlockState itemState = block != null ? block.getStateFromMeta(generated.get().getItemDamage()) : null;
			if(itemState != null && world.isRemote)
			{
				int i = 4;
				
				for(int j = 0; j < i; ++j)
				{
					for(int k = 0; k < i; ++k)
					{
						for(int l = 0; l < i; ++l)
						{
							double d0 = ((double) j + 0.5D) / 4.0D;
							double d1 = ((double) k + 0.5D) / 4.0D;
							double d2 = ((double) l + 0.5D) / 4.0D;
							
							world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + d0 * .5 + .25, pos.getY() + d1 * .5 + 1 / 8D, pos.getZ() + d2 * .5 + .075, d0 - .5, d1 - .5, d2 - .5, block.getStateId(itemState));
						}
					}
				}
			}
		}
		
		if(timer == -1)
			ticksExisted--;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tag = new NBTTagCompound();
		inventory.writeToNBT(tag);
		nbt.setTag("Items", tag);
		nbt.setInteger("Speed", speed);
		nbt.setFloat("Suck", toConsume);
		nbt.setFloat("MaxSuck", maxConsume);
		nbt.setInteger("Timer", timer);
		nbt.setInteger("CooldownTimer", cooldownTimer);
		nbt.setInteger("CooldownTimerMax", cooldownTimerMax);
		nbt.setIntArray("Upgrades", upgrades);
		super.writeNBT(nbt);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
		speed = nbt.getInteger("Speed");
		toConsume = nbt.getFloat("Suck");
		maxConsume = nbt.getFloat("MaxSuck");
		timer = nbt.getInteger("Timer");
		cooldownTimer = nbt.getInteger("CooldownTimer");
		cooldownTimerMax = nbt.getInteger("CooldownTimerMax");
		upgrades = nbt.getIntArray("Upgrades");
		super.readNBT(nbt);
	}
	
	@Override
	public String getName()
	{
		return "Pengu in a Cobble Generator";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return true;
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
		return inventory.getInventoryStackLimit();
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
		return false;
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
	public int[] getUpgrades()
	{
		return upgrades;
	}
	
	@Override
	public boolean canAcceptUpgrade(int type)
	{
		if(hasUpgrade(type))
			return false;
		return type == ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE);
	}
}