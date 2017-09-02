package com.pengu.lostthaumaturgy.client.render.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.core.items.ItemResearch;

public class ColorItemResearch implements IItemColor
{
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		if(tintIndex == 1)
		{
			ResearchItem r = ItemResearch.getFromStack(stack);
			if(r != null)
				return r.getColor();
		}
		return 0xFFFFFF;
	}
}