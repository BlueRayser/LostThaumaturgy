package com.pengu.lostthaumaturgy.api.wand;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class WandCap
{
	public final String id;
	protected ItemStack capItem = ItemStack.EMPTY;
	
	public WandCap(String id)
	{
		this.id = id;
	}
	
	/**
	 * Make this value negative to decrease vis/taint usage
	 */
	public float getUseCost()
	{
		return 10;
	}
	
	public int getCraftCost()
	{
		return 0;
	}
	
	public String getCapTexture()
	{
		return "minecraft:missing";
	}
	
	public int getColorMultiplierARGB(ItemStack wand, EnumCapLocation capLocation, boolean capsSame)
	{
		return 0xFFFFFFFF;
	}
	
	public boolean isValid(ItemStack stack)
	{
		return capItem.isItemEqual(stack);
	}
	
	public void onUpdate(ItemStack wand, EnumCapLocation capLocation, boolean capsSame)
	{
		
	}
	
	public void onEntityItemUpdate(EntityItem wand, EnumCapLocation capLocation, boolean capsSame)
	{
		
	}
}