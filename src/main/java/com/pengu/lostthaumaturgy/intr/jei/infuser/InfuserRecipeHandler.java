package com.pengu.lostthaumaturgy.intr.jei.infuser;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import com.pengu.lostthaumaturgy.LTInfo.JEIConstans;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.InfuserRecipe;

public class InfuserRecipeHandler implements IRecipeHandler<InfuserRecipe>
{
	@Override
	public String getRecipeCategoryUid(InfuserRecipe arg0)
	{
		return JEIConstans.INFUSER;
	}
	
	@Override
	public Class<InfuserRecipe> getRecipeClass()
	{
		return InfuserRecipe.class;
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(InfuserRecipe recipe)
	{
		return new InfuserRecipeWrapper(recipe);
	}
	
	@Override
	public boolean isRecipeValid(InfuserRecipe recipe)
	{
		return recipe != null && recipe.result != null && !recipe.result.isEmpty() && recipe.components != null && recipe.components.length > 0;
	}
}