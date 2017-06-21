package com.pengu.lostthaumaturgy.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.pengu.hammercore.utils.IndexedMap;

public class ItemUpgrade extends Item
{
	private static int lastType = 0;
	private final int type;
	
	private static final IndexedMap<Integer, ItemUpgrade> upgradeMap = new IndexedMap<>();
	
	public ItemUpgrade(String name)
	{
		setUnlocalizedName("upgrades/" + name);
		setMaxStackSize(16);
		type = lastType;
		lastType++;
		
		upgradeMap.put(getUpgradeId(), this);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}
	
	public int getUpgradeId()
	{
		return type;
	}
	
	public static ItemUpgrade byId(int id)
	{
		return upgradeMap.get(id);
	}
	
	public static int idFromItem(ItemUpgrade upgrade)
	{
		Integer i = upgradeMap.getKey(upgrade);
		return i == null ? 0 : i.intValue();
	}
}