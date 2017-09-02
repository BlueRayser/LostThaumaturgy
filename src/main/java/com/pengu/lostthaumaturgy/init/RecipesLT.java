package com.pengu.lostthaumaturgy.init;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class RecipesLT
{
	public static void registerRecipes()
	{
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(BlocksLT.CINNABAR_ORE), EnumMultiMaterialType.QUICKSILVER.stack(), 0.3F);
	}
}