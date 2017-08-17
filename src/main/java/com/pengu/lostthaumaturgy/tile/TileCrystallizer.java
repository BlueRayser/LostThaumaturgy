package com.pengu.lostthaumaturgy.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.hammercore.tile.ITileDroppable;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;
import com.pengu.lostthaumaturgy.client.gui.GuiCrystallizer;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.inventory.ContainerCrystallizer;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class TileCrystallizer extends TileVisUser implements IUpgradable, ISidedInventory, ITileDroppable
{
	public InventoryNonTile inventory = new InventoryNonTile(10);
	private int[] upgrades = new int[] { -1 };
	
	public final NetPropertyNumber<Float> crystalTime;
	
	public float maxTime = 30.0f;
	public float sucked = 0.0f;
	public int boost = 0;
	public int boostDelay = 20;
	
	{
		crystalTime = new NetPropertyNumber<Float>(this, 0F);
	}
	
	public int getCookProgressScaled(int i)
	{
		return Math.round(crystalTime.get() / maxTime * i);
	}
	
	public int getBoostScaled()
	{
		return Math.round(.1F + boost / 2) * 6;
	}
	
	public boolean isCooking()
	{
		return crystalTime.get() > 0;
	}
	
	@Override
	public void tick()
	{
		if(world.isRemote)
			return;
		
		super.tick();
		
		AtmosphereChunk ac;
		
		float crystalTime = this.crystalTime.get();
		final float crystalTimeF = crystalTime;
		
		boolean flag1 = false;
		boolean flag = crystalTime > 0;
		float f = maxTime = hasUpgrade(1) ? 25 : 30;
		
		if(crystalTime > 0)
		{
			float sa = .025F + .0025F * boost + (hasUpgrade(0) ? .025F : 0);
			sucked = getAvailablePureVis(sa);
			crystalTime -= sucked;
			
			if(sucked > 0)
			{
				AtmosphereChunk si = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
				si.radiation += .0005F * sucked;
			}
		} else
			sucked = 0;
		
		if(crystalTime > 0 && (inventory.getStackInSlot(6).isEmpty() || !EnumMultiMaterialType.isCrystal(inventory.getStackInSlot(6))))
		{
			HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":fizz", pos, 1, 1.6F, SoundCategory.BLOCKS);
			crystalTime = 0;
		}
		
		if(crystalTime < 0 && !inventory.getStackInSlot(6).isEmpty() && EnumMultiMaterialType.isCrystal(inventory.getStackInSlot(6)))
		{
			addCrystal(AtmosphereTicker.getCrystalByBiome(world, pos, hasUpgrade(3) ? 3 : 0));
			crystalTime = 0;
			ac = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
			if(ac != null)
				ac.badVibes = (short) (ac.badVibes + 5);
		}
		
		if(crystalTime == 0F && !inventory.getStackInSlot(6).isEmpty() && EnumMultiMaterialType.isCrystal(inventory.getStackInSlot(6)))
			crystalTime = inventory.getStackInSlot(6).isItemEqual(EnumMultiMaterialType.DEPLETED_CRYSTAL.stack()) ? maxTime : maxTime * (2F / 3F);
		
		if(boostDelay <= 0 || boostDelay == 10)
		{
			ac = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
			
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
		
		if(crystalTimeF != crystalTime)
			this.crystalTime.set(crystalTime);
	}
	
	private void addCrystal(int type)
	{
		ItemStack stack = new ItemStack(ItemsLT.MULTI_MATERIAL, 1, EnumMultiMaterialType.VAPOROUS_CRYSTAL.getDamage() + type);
		if(inventory.getStackInSlot(type).isEmpty())
			inventory.setInventorySlotContents(type, stack.copy());
		else if(inventory.getStackInSlot(type).isItemEqual(stack) && inventory.getStackInSlot(type).getCount() < stack.getMaxStackSize())
			inventory.getStackInSlot(type).grow(stack.getCount());
		inventory.getStackInSlot(6).shrink(1);
	}
	
	@Override
	public boolean canAcceptUpgrade(int upgrade)
	{
		if(upgrade != ItemsLT.QUICKSILVER_CORE.getUpgradeId() && upgrade != ItemsLT.STABILIZED_SINGULARITY.getUpgradeId() && upgrade != ItemsLT.CONCENTRATED_EVIL.getUpgradeId())
			return false;
		if(hasUpgrade(upgrade))
			return false;
		return true;
	}
	
	@Override
	public int[] getUpgrades()
	{
		return upgrades;
	}
	
	@Override
	public boolean clearUpgrade(int index)
	{
		if(upgrades[index] >= 0)
		{
			upgrades[index] = -1;
			return true;
		}
		return false;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setIntArray("Upgrades", upgrades);
		NBTTagCompound tag = new NBTTagCompound();
		inventory.writeToNBT(tag);
		nbt.setTag("Items", tag);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		upgrades = nbt.getIntArray("Upgrades");
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiCrystallizer(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerCrystallizer(this, player);
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
		return index == 6 && EnumMultiMaterialType.isCrystal(stack);
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
		return "Vis Crystallizer";
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
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		inventory.drop(world, pos);
	}
}