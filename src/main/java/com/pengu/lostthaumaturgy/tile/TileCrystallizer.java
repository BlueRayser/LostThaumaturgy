package com.pengu.lostthaumaturgy.tile;

import net.minecraft.nbt.NBTTagCompound;

import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;

public class TileCrystallizer extends TileVisUser implements IUpgradable
{
	private byte[] upgrades = new byte[] { -1 };
	
	@Override
	public boolean canAcceptUpgrade(byte upgrade)
	{
		if(upgrade != 0 && upgrade != 1 && upgrade != 3)
			return false;
		if(this.hasUpgrade(upgrade))
			return false;
		return true;
	}
	
	@Override
	public int getUpgradeLimit()
	{
		return 1;
	}
	
	@Override
	public byte[] getUpgrades()
	{
		return this.upgrades;
	}
	
	@Override
	public boolean hasUpgrade(byte upgrade)
	{
		if(this.upgrades.length < 1)
		{
			return false;
		}
		for(int a = 0; a < this.getUpgradeLimit(); ++a)
		{
			if(this.upgrades[a] != upgrade)
				continue;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean setUpgrade(byte upgrade)
	{
		for(int a = 0; a < this.getUpgradeLimit(); ++a)
		{
			if(this.upgrades[a] >= 0 || !this.canAcceptUpgrade(upgrade))
				continue;
			this.upgrades[a] = upgrade;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean clearUpgrade(int index)
	{
		if(this.upgrades[index] >= 0)
		{
			this.upgrades[index] = -1;
			return true;
		}
		return false;
	}
}