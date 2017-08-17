package com.pengu.lostthaumaturgy.tile;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.RecipesInfuser;
import com.pengu.lostthaumaturgy.client.gui.GuiInfuserDark;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.inventory.ContainerInfuserDark;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.net.PacketSmallGreenFlameFX;

public class TileInfuserDark extends TileInfuser
{
	public float infuserCookTimeDark;
	public float currentItemCookCostDark;
	
	{
		infuserItemStacks = new InventoryNonTile(6);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerInfuserDark(this, player);
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiInfuserDark(this, player);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		canSpawnParticle = true;
		if(soundDelay > 0)
			--soundDelay;
		angle = (int) ((infuserCookTime + infuserCookTimeDark) / (currentItemCookCost + currentItemCookCostDark) * 360);
		if(world.isRemote)
			return;
		boost = 3 + Math.abs(world.provider.getMoonPhase(world.getWorldTime()) - 4);
		boolean flag1 = false;
		boolean flag = infuserCookTime > 0.0f || infuserCookTimeDark > 0.0f;
		setSuction(0);
		if(canProcess() && (currentItemCookCost > 0.0f || currentItemCookCostDark > 0.0f) && !gettingPower())
		{
			float savis = Math.min(0.25f + 0.025f * (float) boost + (hasUpgrade(0) ? 0.25f : 0.0f), currentItemCookCost - infuserCookTime + 0.01f);
			float sataint = Math.min(0.25f + 0.025f * (float) boost + (hasUpgrade(0) ? 0.25f : 0.0f), currentItemCookCostDark - infuserCookTimeDark + 0.01f);
			float suckedvis = 0.0f;
			if(currentItemCookCost - infuserCookTime > 0.0f)
				suckedvis = getAvailablePureVis(savis);
			infuserCookTime += suckedvis;
			float suckedtaint = 0.0f;
			if(currentItemCookCostDark - infuserCookTimeDark > 0.0f)
				suckedtaint = getAvailableTaintedVis(sataint);
			infuserCookTimeDark += suckedtaint;
			sucked = suckedvis + suckedtaint;
			if(soundDelay == 0 && sucked >= 0.025f)
			{
				HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":dark_infuser", pos, .2F, 1F, SoundCategory.BLOCKS);
				soundDelay = 62;
				AtmosphereChunk ac = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
				if(ac != null)
					ac.badVibes = (short) (ac.badVibes + 2);
			}
			if(!hasUpgrade(-1))
			{
				switch(world.rand.nextInt(4))
				{
				case 0:
				{
					HCNetwork.getManager("particles").sendToAllAround(new PacketSmallGreenFlameFX(new Vec3d(pos).addVector(.1, 1.15, .1)), getSyncPoint(32));
					break;
				}
				case 1:
				{
					HCNetwork.getManager("particles").sendToAllAround(new PacketSmallGreenFlameFX(new Vec3d(pos).addVector(.1, 1.15, .9)), getSyncPoint(32));
					break;
				}
				case 2:
				{
					HCNetwork.getManager("particles").sendToAllAround(new PacketSmallGreenFlameFX(new Vec3d(pos).addVector(.9, 1.15, .1)), getSyncPoint(32));
					break;
				}
				case 3:
				{
					HCNetwork.getManager("particles").sendToAllAround(new PacketSmallGreenFlameFX(new Vec3d(pos).addVector(.9, 1.15, .9)), getSyncPoint(32));
				}
				}
			}
			if(sucked > 0)
			{
				AtmosphereChunk si = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
				si.radiation += .0005F * sucked;
				sync();
			}
		} else
			sucked = 0;
		if(infuserCookTime >= currentItemCookCost && infuserCookTimeDark >= currentItemCookCostDark && flag)
		{
			addProcessedItem();
			infuserCookTime = 0;
			currentItemCookCost = 0;
			infuserCookTimeDark = 0;
			currentItemCookCostDark = 0;
			changes++;
		}
		if((currentItemCookCost != 0 || currentItemCookCostDark != 0) && Math.round(currentItemCookCost + currentItemCookCostDark) != Math.round(getCookCost()))
		{
			infuserCookTime = 0;
			currentItemCookCost = 0;
			infuserCookTimeDark = 0;
			currentItemCookCostDark = 0;
			HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":fizz", pos, 1, 1.6F, SoundCategory.BLOCKS);
		}
		if(infuserCookTime == 0.0f && infuserCookTimeDark == 0.0f && canProcess())
		{
			float cost;
			currentItemCookCost = cost = getCookCost();
			currentItemCookCostDark = cost;
			if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL)))
			{
				currentItemCookCost /= 2;
				currentItemCookCostDark /= 2;
			} else
			{
				currentItemCookCost *= .6666667F;
				currentItemCookCostDark *= .33333334F;
			}
			changes++;
		}
		
		if(flag != (infuserCookTime > 0 || infuserCookTimeDark > 0))
		{
			flag1 = true;
		}
		
		if(flag1)
			changes++;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setFloat("CookTimeDark", infuserCookTimeDark);
		nbt.setFloat("CookCostDark", currentItemCookCostDark);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		infuserCookTimeDark = nbt.getFloat("CookTimeDark");
		currentItemCookCostDark = nbt.getFloat("CookCostDark");
	}
	
	@Override
	protected boolean canProcess()
	{
		ItemStack itemstack = getResultStack();
		if(itemstack.isEmpty())
			return false;
		if(infuserItemStacks.getStackInSlot(0).isEmpty())
			return true;
		if(!infuserItemStacks.getStackInSlot(0).isItemEqual(itemstack))
			return false;
		int st = infuserItemStacks.getStackInSlot(0).getCount() + itemstack.getCount();
		if(st <= infuserItemStacks.getInventoryStackLimit() && st <= itemstack.getMaxStackSize())
			return true;
		return false;
	}
	
	@Override
	protected float getCookCost()
	{
		ArrayList<ItemStack> isal = new ArrayList<ItemStack>();
		for(int a = 1; a <= 5; ++a)
		{
			if(infuserItemStacks.getStackInSlot(a).isEmpty())
				continue;
			ItemStack is = new ItemStack(infuserItemStacks.getStackInSlot(a).getItem(), 1, infuserItemStacks.getStackInSlot(a).getItemDamage());
			isal.add(is);
		}
		if(isal.size() > 0)
		{
			float cost = RecipesInfuser.getInfusingCost(isal.toArray(), true, this);
			if(hasUpgrade(1))
			{
				cost *= 0.8f;
			}
			return cost;
		}
		return 0.0f;
	}
	
	@Override
	protected ItemStack getResultStack()
	{
		ArrayList<ItemStack> isal = new ArrayList<ItemStack>();
		for(int a = 1; a <= 5; ++a)
		{
			if(infuserItemStacks.getStackInSlot(a).isEmpty())
				continue;
			ItemStack is = new ItemStack(infuserItemStacks.getStackInSlot(a).getItem(), 1, infuserItemStacks.getStackInSlot(a).getItemDamage());
			isal.add(is);
		}
		if(isal.size() > 0)
			return RecipesInfuser.getInfusingResult(isal.toArray(), true, this);
		return ItemStack.EMPTY;
	}
	
	@Override
	protected void addProcessedItem()
	{
		if(!canProcess())
			return;
		ItemStack itemstack = getResultStack();
		if(infuserItemStacks.getStackInSlot(0).isEmpty())
			infuserItemStacks.setInventorySlotContents(0, itemstack.copy());
		else if(infuserItemStacks.getStackInSlot(0).isItemEqual(itemstack) && infuserItemStacks.getStackInSlot(0).getCount() < itemstack.getMaxStackSize())
			infuserItemStacks.getStackInSlot(0).grow(itemstack.getCount());
		for(int a = 1; a <= 5; ++a)
		{
			if(infuserItemStacks.getStackInSlot(a).isEmpty())
				continue;
			infuserItemStacks.getStackInSlot(a).shrink(1);
		}
	}
}