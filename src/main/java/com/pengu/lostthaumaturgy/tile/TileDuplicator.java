package com.pengu.lostthaumaturgy.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.utils.NetPropertyBool;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.api.items.INotCloneable;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;
import com.pengu.lostthaumaturgy.client.gui.GuiDuplicator;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.inventory.ContainerDuplicator;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.net.machine.PacketDuplicatorFinish;

public class TileDuplicator extends TileVisUser implements IUpgradable
{
	public static NonNullList<ItemStack> DUPLICATOR_BLACKLIST = NonNullList.<ItemStack> create();
	public final InventoryNonTile inventory = new InventoryNonTile(10);
	public int[] upgrades = new int[] { -1 };
	
	public final NetPropertyNumber<Integer> orientation;
	public final NetPropertyNumber<Float> press;
	
	public float duplicatorCopyTime;
	public float currentItemCopyCost;
	public float sucked;
	public final NetPropertyBool repeat;
	public int boost;
	private boolean doPress;
	private int pressDelay;
	int boostDelay = 20;
	
	public boolean forbid = false;
	
	{
		orientation = new NetPropertyNumber<Integer>(this, -1);
		press = new NetPropertyNumber<Float>(this, 0F);
		repeat = new NetPropertyBool(this, false);
	}
	
	@Override
	public void tick()
	{
		forbid = false;
		AtmosphereChunk ac = AuraTicker.getAuraChunkFromBlockCoords(world, pos);
		
		boolean flag;
		
		super.tick();
		
		if(world.isRemote)
			return;
		
		if(press.get() > 0 && !doPress)
			press.set(press.get() * .97F);
		
		if(press.get() < .125F && !doPress)
			press.set(.125F);
		
		if(press.get() < .1875F && doPress)
			press.set(press.get() * 1.1F);
		
		if(press.get() >= .1875F && doPress && pressDelay <= 0)
		{
			pressDelay = 12;
			press.set(.1875F);
		}
		
		if(pressDelay > 0)
			--pressDelay;
		
		if(pressDelay <= 0 && doPress && press.get() >= .1875F)
			doPress = false;
		
		boolean flag1 = false;
		boolean bl = flag = duplicatorCopyTime > 0.0f;
		
		if(canProcess() && currentItemCopyCost > 0.0f && !gettingPower())
		{
			float sa = 0.5f + 0.05f * (float) boost + (hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? .5F : 0);
			sucked = getAvailablePureVis(sa);
			duplicatorCopyTime += sucked;
		} else
			sucked = 0.0f;
		
		if(duplicatorCopyTime >= currentItemCopyCost && flag)
		{
			if(ac != null)
				ac.badVibes = (short) ((float) ac.badVibes + Math.max(1.0f, currentItemCopyCost / 20.0f));
			
			ac.radiation += .0004F * currentItemCopyCost / 5F;
			
			addProcessedItem();
			doPress = true;
			duplicatorCopyTime = 0;
			currentItemCopyCost = 0;
			
			sync();
			
			HCNetwork.manager.sendToAllAround(new PacketDuplicatorFinish(pos), getSyncPoint(48));
		}
		
		if(currentItemCopyCost != 0.0f && currentItemCopyCost != (float) getCopyCost())
		{
			duplicatorCopyTime = 0.0f;
			currentItemCopyCost = 0.0f;
			HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":fizz", pos, 1F, 1.6F, SoundCategory.BLOCKS);
		}
		
		if(duplicatorCopyTime == 0.0f && canProcess())
		{
			currentItemCopyCost = getCopyCost();
			sync();
		}
		
		if(flag != duplicatorCopyTime > 0.0f)
			flag1 = true;
		
		if(flag1)
			sync();
		
		if(boostDelay <= 0 || boostDelay == 10)
		{
			if(ac != null && boost < 10 && ac.boost > 0)
			{
				++boost;
				ac.boost = (short) (ac.boost - 1);
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
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiDuplicator(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerDuplicator(this, player);
	}
	
	public int getCopyCost()
	{
		int tr = 0;
		
		for(ItemStack stack : DUPLICATOR_BLACKLIST)
			if(inventory.getStackInSlot(9).isItemEqual(stack))
				return 0;
		
		if(inventory.getStackInSlot(9).getItem() instanceof INotCloneable)
			return 0;
		
		if(inventory.getStackInSlot(9).getItem() instanceof ItemBlock && ((ItemBlock) inventory.getStackInSlot(9).getItem()).getBlock() instanceof INotCloneable)
			return 0;
		
		if(inventory.getStackInSlot(9).isEmpty() || inventory.getStackInSlot(9).getRarity() != EnumRarity.COMMON)
			return 0;
		
		if(inventory.getStackInSlot(9).getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE))
			return 2;
		
		float t = RecipesCrucible.getSmeltingValue(inventory.getStackInSlot(9));
		
		if(t > 0)
		{
			tr = Math.round(t * (float) (hasUpgrade(1) ? 4 : 5));
			return tr;
		}
		
		return 0;
	}
	
	private boolean canProcess()
	{
		if(inventory.getStackInSlot(9).isEmpty())
			return false;
		for(int j = 0; j < 9; ++j)
		{
			if(inventory.getStackInSlot(j).isEmpty())
				return true;
			if(!inventory.getStackInSlot(j).isItemEqual(inventory.getStackInSlot(9)))
				continue;
			int st = inventory.getStackInSlot(j).getCount() + 1;
			if(!repeat.get())
				++st;
			if(st > inventory.getInventoryStackLimit() || st > inventory.getStackInSlot(j).getMaxStackSize())
				continue;
			return true;
		}
		return false;
	}
	
	private void addProcessedItem()
	{
		if(!canProcess())
			return;
		ItemStack itemstack = new ItemStack(inventory.getStackInSlot(9).getItem(), 1, inventory.getStackInSlot(9).getItemDamage());
		itemstack.setTagCompound(inventory.getStackInSlot(9).getTagCompound());
		int repeats = repeat.get() ? 1 : 2;
		
		block0: for(int q2 = 0; q2 < repeats; ++q2)
		{
			for(int j = 0; j < 9; ++j)
			{
				if(inventory.getStackInSlot(j).isEmpty())
				{
					inventory.setInventorySlotContents(j, itemstack.copy());
					continue block0;
				}
				
				if(!inventory.getStackInSlot(j).isItemEqual(itemstack) || inventory.getStackInSlot(j).getCount() >= itemstack.getMaxStackSize())
					continue;
				inventory.getStackInSlot(j).grow(itemstack.getCount());
				continue block0;
			}
		}
		
		if(!repeat.get())
			inventory.getStackInSlot(9).shrink(1);
	}
	
	@Override
	public boolean canAcceptUpgrade(int upgrade)
	{
		if(upgrade != ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE) && upgrade != ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY))
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
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		upgrades = nbt.getIntArray("Upgrades");
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
		duplicatorCopyTime = nbt.getFloat("DuplicatorCopyTime");
		currentItemCopyCost = nbt.getFloat("CurrentItemCopyCost");
		sucked = nbt.getFloat("Sucked");
		boost = nbt.getInteger("Boost");
		doPress = nbt.getBoolean("DoPress");
		pressDelay = nbt.getInteger("PressDelay");
		boostDelay = nbt.getInteger("BoostDelay");
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setIntArray("Upgrades", upgrades);
		NBTTagCompound tag = new NBTTagCompound();
		inventory.writeToNBT(tag);
		nbt.setTag("Items", tag);
		nbt.setFloat("DuplicatorCopyTime", duplicatorCopyTime);
		nbt.setFloat("CurrentItemCopyCost", currentItemCopyCost);
		nbt.setFloat("Sucked", sucked);
		nbt.setFloat("Boost", boost);
		nbt.setBoolean("DoPress", doPress);
		nbt.setInteger("PressDelay", pressDelay);
		nbt.setInteger("BoostDelay", boostDelay);
	}
}