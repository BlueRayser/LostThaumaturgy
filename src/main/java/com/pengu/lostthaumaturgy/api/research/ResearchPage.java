package com.pengu.lostthaumaturgy.api.research;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.api.RecipesInfuser.DarkInfuserRecipe;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.InfuserRecipe;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;

public class ResearchPage
{
	public ResearchPage.PageType type = ResearchPage.PageType.TEXT;
	public String text;
	public String research;
	public ResourceLocation image;
	private Object recipe;
	public ItemStack recipeOutput;
	
	public static List<IRecipe> findRecipesFor(ItemStack out)
	{
		List<IRecipe> recipes = new ArrayList<>();
		
		CraftingManager.REGISTRY.forEach(recipe ->
		{
			if(recipe.getRecipeOutput().isItemEqual(out))
				recipes.add(recipe);
		});
		
		return recipes;
	}
	
	public Object getRecipe()
    {
		if(type == PageType.NORMAL_CRAFTING)
		{
			if(recipe instanceof ItemStack[])
			{
				List<IRecipe> rs = new ArrayList<>();
				for(ItemStack r : (ItemStack[]) recipe)
					rs.addAll(findRecipesFor(r));
				return rs.toArray(new ItemStack[0]);
			} else if(recipe instanceof ItemStack)
				return findRecipesFor((ItemStack) recipe).toArray(new ItemStack[0]);
		}
		
	    return recipe;
    }
	
	public ResearchPage(String text)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.TEXT;
		this.text = text;
	}
	
	public ResearchPage(String research, String text)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.TEXT_CONCEALED;
		this.research = research;
		this.text = text;
	}
	
	public ResearchPage(ItemStack recipe, boolean unused)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.NORMAL_CRAFTING;
		this.recipe = recipe;
		this.recipeOutput = recipe;
	}
	
	public ResearchPage(ItemStack[] recipe, boolean unused)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.NORMAL_CRAFTING;
		this.recipe = recipe;
	}
	
	public ResearchPage(IFuserRecipe[] recipe)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.ARCANE_CRAFTING;
		this.recipe = recipe;
	}
	
	public ResearchPage(InfuserRecipe[] recipe)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.INFUSION_CRAFTING;
		this.recipe = recipe;
	}
	
	public ResearchPage(List recipe)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.COMPOUND_CRAFTING;
		this.recipe = recipe;
	}
	
	public ResearchPage(IFuserRecipe recipe)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.ARCANE_CRAFTING;
		this.recipe = recipe;
		this.recipeOutput = recipe.getOutput();
	}
	
	public ResearchPage(ItemStack input)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.SMELTING;
		this.recipe = input;
		this.recipeOutput = FurnaceRecipes.instance().getSmeltingResult(input).copy();
	}
	
	public ResearchPage(InfuserRecipe recipe)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.INFUSION_CRAFTING;
		this.recipe = recipe;
		recipeOutput = recipe.result.copy();
	}
	
	public ResearchPage(DarkInfuserRecipe recipe)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.DARK_INFUSION_CRAFTING;
		this.recipe = recipe;
	}
	
	public ResearchPage(ResourceLocation image, String caption)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.IMAGE;
		this.image = image;
		this.text = caption;
	}
	
	public String getTranslatedText()
	{
		String ret = "";
		
		if(this.text != null)
		{
			ret = I18n.format(this.text);
			if(ret.isEmpty())
				ret = this.text;
		}
		
		return ret;
	}
	
	public static enum PageType
	{
		TEXT("TEXT", 0), //
		TEXT_CONCEALED("TEXT_CONCEALED", 1), //
		IMAGE("IMAGE", 2), //
		ARCANE_CRAFTING("ARCANE_CRAFTING", 4), //
		NORMAL_CRAFTING("NORMAL_CRAFTING", 6), //
		INFUSION_CRAFTING("INFUSION_CRAFTING", 7), //
		COMPOUND_CRAFTING("COMPOUND_CRAFTING", 8), //
		DARK_INFUSION_CRAFTING("INFUSION_ENCHANTMENT", 9), //
		SMELTING("SMELTING", 10);
		
		private final String v1;
		private final int v2;
		
		private PageType(String var1, int var2)
		{
			v1 = var1;
			v2 = var2;
		}
		
		public String getV1()
		{
			return v1;
		}
		
		public int getV2()
		{
			return v2;
		}
	}
}