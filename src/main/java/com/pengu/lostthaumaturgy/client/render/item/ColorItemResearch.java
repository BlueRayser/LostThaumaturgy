package com.pengu.lostthaumaturgy.client.render.item;

import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.items.ItemResearch;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ColorItemResearch implements IItemColor
{
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		if(tintIndex == 1)
		{
			Research r = ItemResearch.getFromStack(stack);
			if(r != null) return r.getColor();
		}
		return 0xFFFFFF;
	}
}