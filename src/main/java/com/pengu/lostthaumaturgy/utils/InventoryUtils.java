package com.pengu.lostthaumaturgy.utils;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryUtils
{
	public static ItemStack cycleItemStack(Object input)
	{
		NonNullList<ItemStack> q;
		ItemStack it = null;
		
		if(input instanceof ItemStack)
		{
			it = (ItemStack) input;
			if(it.getMetadata() == 32767 && it.getItem().getHasSubtypes())
			{
				NonNullList<ItemStack> q2 = NonNullList.create();
				it.getItem().getSubItems(it.getItem().getCreativeTab(), q2);
				if(q2 != null && q2.size() > 0)
				{
					int md = (int) (System.currentTimeMillis() / 1000 % (long) q2.size());
					ItemStack it2 = new ItemStack(it.getItem(), 1, md);
					it2.setTagCompound(it.getTagCompound());
					it = it2;
				}
			} else if(it.getMetadata() == 32767 && it.isItemStackDamageable())
			{
				int md = (int) (System.currentTimeMillis() / 10 % (long) it.getMaxDamage());
				ItemStack it2 = new ItemStack(it.getItem(), 1, md);
				it2.setTagCompound(it.getTagCompound());
				it = it2;
			}
		} else if(input instanceof ItemStack[])
		{
			ItemStack[] q3 = (ItemStack[]) input;
			if(q3 != null && q3.length > 0)
			{
				int idx = (int) (System.currentTimeMillis() / 1000 % (long) q3.length);
				it = InventoryUtils.cycleItemStack(q3[idx]);
			}
		} else if(input instanceof Ingredient)
			it = InventoryUtils.cycleItemStack(((Ingredient) input).getMatchingStacks());
		else if(input instanceof ArrayList)
		{
			ArrayList q3 = (ArrayList) input;
			if(q3 != null && q3.size() > 0)
			{
				int idx = (int) (System.currentTimeMillis() / 1000 % (long) q3.size());
				it = InventoryUtils.cycleItemStack(q3.get(idx));
			}
		} else if(input instanceof String && (q = OreDictionary.getOres((String) (input))) != null && q.size() > 0)
		{
			int idx = (int) (System.currentTimeMillis() / 1000 % (long) q.size());
			it = InventoryUtils.cycleItemStack(q.get(idx));
		}
		
		return it;
	}
}