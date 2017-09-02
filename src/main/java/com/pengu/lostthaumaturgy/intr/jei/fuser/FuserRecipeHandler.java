package com.pengu.lostthaumaturgy.intr.jei.fuser;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;
import com.pengu.lostthaumaturgy.core.Info.JEIConstans;

public class FuserRecipeHandler implements IRecipeHandler<IFuserRecipe>
{
	@Override
	public String getRecipeCategoryUid(IFuserRecipe recipe)
	{
		return JEIConstans.FUSER;
	}
	
	@Override
	public Class<IFuserRecipe> getRecipeClass()
	{
		return IFuserRecipe.class;
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(IFuserRecipe recipe)
	{
		return new FuserRecipeWrapper(recipe);
	}
	
	@Override
	public boolean isRecipeValid(IFuserRecipe recipe)
	{
		return !InterItemStack.isStackNull(recipe.getOutput());
	}
}