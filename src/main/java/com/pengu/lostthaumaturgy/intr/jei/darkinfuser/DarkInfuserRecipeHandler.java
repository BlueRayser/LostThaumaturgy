package com.pengu.lostthaumaturgy.intr.jei.darkinfuser;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import com.pengu.lostthaumaturgy.LTInfo.JEIConstans;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.DarkInfuserRecipe;

public class DarkInfuserRecipeHandler implements IRecipeHandler<DarkInfuserRecipe>
{
	@Override
	public String getRecipeCategoryUid(DarkInfuserRecipe arg0)
	{
		return JEIConstans.DARK_INFUSER;
	}
	
	@Override
	public Class<DarkInfuserRecipe> getRecipeClass()
	{
		return DarkInfuserRecipe.class;
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(DarkInfuserRecipe recipe)
	{
		return new DarkInfuserRecipeWrapper(recipe);
	}
	
	@Override
	public boolean isRecipeValid(DarkInfuserRecipe recipe)
	{
		return recipe != null && recipe.result != null && !recipe.result.isEmpty() && recipe.components != null && recipe.components.length > 0;
	}
}