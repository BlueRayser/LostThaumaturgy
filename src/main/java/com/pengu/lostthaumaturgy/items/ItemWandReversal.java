package com.pengu.lostthaumaturgy.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ItemWandReversal extends Item
{
	public ItemWandReversal()
    {
		setUnlocalizedName("wand_reversal");
		setMaxStackSize(1);
		setMaxDamage(200);
    }
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		float damage = Math.max(0F, (float) (stack.getMaxDamage() - stack.getItemDamage()) / stack.getMaxDamage());
		int color = 0xE400C4;
		
		int target = (int) (damage * 255F);
		target = (target << 16) | (target << 8) | target;
		
		return MathHelper.multiplyColor(target, color);
	}
}