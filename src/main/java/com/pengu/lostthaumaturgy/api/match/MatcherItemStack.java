package com.pengu.lostthaumaturgy.api.match;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MatcherItemStack implements IMatcher<ItemStack>
{
	private ItemStack sample;
	
	public MatcherItemStack(ItemStack sample)
	{
		this.sample = sample;
	}
	
	@Override
	public boolean matches(ItemStack t)
	{
		if(t.isItemEqual(sample))
			return true;
		if(sample.getItemDamage() == -1 || sample.getItemDamage() == OreDictionary.WILDCARD_VALUE)
		{
			if(sample.getItem() == t.getItem())
				return true;
		}
		return false;
	}
	
	@Override
	public ItemStack defaultInstance()
	{
		return sample.copy();
	}
}