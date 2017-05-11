package com.pengu.lostthaumaturgy.intr.jei.infuser;

import java.util.Arrays;
import java.util.List;

import com.pengu.lostthaumaturgy.api.RecipesInfuser.InfuserRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;

public class InfuserRecipeWrapper implements IRecipeWrapper
{
	public final InfuserRecipe recipe;
	
	public InfuserRecipeWrapper(InfuserRecipe recipe)
    {
		this.recipe = recipe;
    }
	
	@Override
	public void drawInfo(Minecraft mc, int arg1, int arg2, int arg3, int arg4)
	{
		if(recipe.depletedShards > 0) mc.fontRendererObj.drawString("0-" + recipe.depletedShards, 68, 148, 0xFFFFFF, true);
		
		FontRenderer f = mc.fontRendererObj;
		String s = recipe.cost + " Vis";
		f.drawString(s, 76 - f.getStringWidth(s) / 2, 86, 0xFFFFFF, true);
	}
	
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setOutput(ItemStack.class, recipe.result);
		ingredients.setInputs(ItemStack.class, Arrays.asList(recipe.components));
	}
	
	@Override
	public List<String> getTooltipStrings(int x, int y)
	{
		return null;
	}
	
	@Override
	public boolean handleClick(Minecraft mc, int x, int y, int type)
	{
		return false;
	}
}