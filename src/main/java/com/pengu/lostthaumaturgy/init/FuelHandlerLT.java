package com.pengu.lostthaumaturgy.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class FuelHandlerLT implements IFuelHandler
{
	@Override
	public int getBurnTime(ItemStack fuel)
	{
		if(fuel.getItem() == ItemsLT.MULTI_MATERIAL)
		{
			if(fuel.getItemDamage() == EnumMultiMaterialType.ALUMENTUM.getDamage())
				return 6400;
		}
		return 0;
	}
}