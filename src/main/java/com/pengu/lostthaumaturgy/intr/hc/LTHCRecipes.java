package com.pengu.lostthaumaturgy.intr.hc;

import com.pengu.hammercore.recipeAPI.IRecipePlugin;
import com.pengu.hammercore.recipeAPI.IRecipeTypeRegistry;
import com.pengu.hammercore.recipeAPI.RecipePlugin;
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