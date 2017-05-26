package com.pengu.lostthaumaturgy.intr.hc;

import com.mrdimka.hammercore.recipeAPI.IRecipePlugin;
import com.mrdimka.hammercore.recipeAPI.RecipePlugin;
import com.mrdimka.hammercore.recipeAPI.registry.IRecipeTypeRegistry;
import com.pengu.lostthaumaturgy.intr.hc.rts.RecipeTypeCrucible;

@RecipePlugin
public class LTHCRecipes implements IRecipePlugin
{
	@Override
	public void registerTypes(IRecipeTypeRegistry reg)
	{
		reg.register(new RecipeTypeCrucible());
	}
}