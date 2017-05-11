package com.pengu.lostthaumaturgy.intr.hc.rts;

import net.minecraft.nbt.NBTTagCompound;

import com.mrdimka.hammercore.recipeAPI.types.IRecipeType;
import com.pengu.lostthaumaturgy.intr.hc.rts.RecipeTypeCrucible.CrucibleRecipe;

public class RecipeTypeCrucible implements IRecipeType<CrucibleRecipe>
{
	@Override
	public void addRecipe(CrucibleRecipe recipe)
	{
	}
	
	@Override
	public CrucibleRecipe createRecipe(NBTTagCompound json) throws com.mrdimka.hammercore.recipeAPI.types.IRecipeType.RecipeParseException
	{
	    return null;
	}
	
	@Override
	public String getTypeId()
	{
	    return null;
	}
	
	@Override
	public boolean isJeiSupported(CrucibleRecipe recipe)
	{
	    return false;
	}
	
	@Override
	public void removeRecipe(CrucibleRecipe recipe)
	{
	}
	
	public static class CrucibleRecipe
	{
		public String object;
		public float value;
	}
}