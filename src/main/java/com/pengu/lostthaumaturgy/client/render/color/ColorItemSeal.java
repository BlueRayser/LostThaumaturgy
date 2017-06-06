package com.pengu.lostthaumaturgy.client.render.color;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ColorItemSeal implements IItemColor
{
	@Override
	public int getColorFromItemstack(ItemStack stack, int layer)
	{
		if(layer == 1)
		{
			int color = 0xFFFFFF;
			
			if(stack.hasTagCompound())
			{
				int[] rgb = stack.getTagCompound().getIntArray("RGB");
				if(rgb != null && rgb.length >= 3)
					color = (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
			}
			
			return color;
		}
		
	    return 0xFFFFFF;
	}
}