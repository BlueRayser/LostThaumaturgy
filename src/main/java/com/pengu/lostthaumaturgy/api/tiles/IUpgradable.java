package com.pengu.lostthaumaturgy.api.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;

public interface IUpgradable
{
	int[] getUpgrades();
	
	default boolean setUpgrade(int upgrade)
	{
		int[] upgrades = getUpgrades();
		
		for(int a = 0; a < getUpgradeLimit(); ++a)
		{
			if(upgrades[a] >= 0 || !canAcceptUpgrade(upgrade))
				continue;
			upgrades[a] = upgrade;
			onUpgradeInstalled(upgrade, a);
			return true;
		}
		
		return false;
	}
	
	default boolean clearUpgrade(int slot)
	{
		if(getUpgrades()[slot] >= 0)
		{
			getUpgrades()[slot] = -1;
			return true;
		}
		return false;
	}
	
	boolean canAcceptUpgrade(int type);
	
	default boolean hasUpgrade(int type)
	{
		return getUpgradeCount(type) > 0;
	}
	
	default int getUpgradeCount(int type)
	{
		int c = 0;
		for(int i : getUpgrades())
			if(i == type)
				c++;
		return c;
	}
	
	default int getInstalledUpgradeCount()
	{
		int c = 0;
		for(int i : getUpgrades())
			if(i != -1)
				c++;
		return c;
	}
	
	default boolean dropUpgrade(EntityPlayer player)
	{
		int last = -1;
		int[] u = getUpgrades();
		for(int i = 0; i < u.length; ++i)
			if(u[i] != -1)
				last = i;
		
		TileEntity asTile = WorldUtil.cast(this, TileEntity.class);
		
		World world = null;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		
		if(asTile != null)
		{
			world = asTile.getWorld();
			posX = asTile.getPos().getX() + .5;
			posY = asTile.getPos().getY() + .5;
			posZ = asTile.getPos().getZ() + .5;
		} else
		{
			Entity asEntity = WorldUtil.cast(this, Entity.class);
			
			if(asEntity != null)
			{
				world = asEntity.world;
				posX = asEntity.posX;
				posY = asEntity.posY;
				posZ = asEntity.posZ;
			}
		}
		
		if(last != -1 && world != null)
		{
			EntityItem ent = new EntityItem(world, posX, posY, posZ, new ItemStack(ItemUpgrade.byId(u[last])));
			
			double mod = .10000000149011612;
			ent.motionX = (player.posX - ent.posX) * mod;
			ent.motionY = (player.posY - ent.posY) * mod;
			ent.motionZ = (player.posZ - ent.posZ) * mod;
			
			boolean cleared = clearUpgrade(last);
			if(!world.isRemote && cleared)
				world.spawnEntity(ent);
			
			return cleared;
		}
		
		return false;
	}
	
	default boolean hasUpgradeInSlot(int slot)
	{
		return getUpgrades()[slot] != -1;
	}
	
	default int getUpgradeLimit()
	{
		return getUpgrades().length;
	}
	
	default void onUpgradeInstalled(int id, int slot)
	{
		
	}
}