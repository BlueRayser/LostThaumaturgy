package com.pengu.lostthaumaturgy.api.research;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.lostthaumaturgy.api.RecipesInfuser;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.DarkInfuserRecipe;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.InfuserRecipe;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;
import com.pengu.lostthaumaturgy.api.research.client.IRenderExtension;

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
				return rs.toArray(new IRecipe[0]);
			} else if(recipe instanceof ItemStack)
				return findRecipesFor((ItemStack) recipe).toArray(new IRecipe[0]);
		}
		
		if(type == PageType.INFUSION_CRAFTING)
		{
			if(recipe == null || (recipe instanceof Object[] && ((Object[]) recipe).length == 0))
				recipe = InfuserRecipe.asRecipes(RecipesInfuser.listRecipes().findRecipes(recipeOutput, false));
		}
		
		return recipe;
	}
	
	/** For adding custom recipe support */
	public ResearchPage(Object recipe, PageType page)
    {
		type = page;
		this.recipe = recipe;
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
	
	public ResearchPage(InfuserRecipe recipe, ItemStack output)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipeOutput = null;
		this.type = ResearchPage.PageType.INFUSION_CRAFTING;
		this.recipe = null;
		recipeOutput = output;
	}
	
	public ResearchPage(DarkInfuserRecipe recipe)
	{
		this.text = null;
		this.research = null;
		this.image = null;
		this.recipeOutput = recipe.result.copy();
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
	
	public static class PageType
	{
		public static final PageType //
		        TEXT = new PageType("TEXT"), //
		        TEXT_CONCEALED = new PageType("TEXT_CONCEALED"), //
		        IMAGE = new PageType("IMAGE"), //
		        ARCANE_CRAFTING = new PageType("ARCANE_CRAFTING"), //
		        NORMAL_CRAFTING = new PageType("NORMAL_CRAFTING"), //
		        INFUSION_CRAFTING = new PageType("INFUSION_CRAFTING"), //
		        COMPOUND_CRAFTING = new PageType("COMPOUND_CRAFTING"), //
		        DARK_INFUSION_CRAFTING = new PageType("INFUSION_ENCHANTMENT"), //
		        SMELTING = new PageType("SMELTING");
		
		private final String v1;
		private Object render;
		private final String renderClass;
		
		private PageType(String var1)
		{
			this(var1, null);
		}
		
		private PageType(String var1, String renderClass)
		{
			v1 = var1;
			this.renderClass = renderClass;
		}
		
		public String getV1()
		{
			return v1;
		}
		
		@SideOnly(Side.CLIENT)
		public IRenderExtension getRender()
		{
			if(renderClass == null || renderClass.isEmpty())
				return null;
			
			if(render == null)
			{
				try
				{
					render = (IRenderExtension) Class.forName(renderClass).newInstance();
				} catch(Throwable err)
				{
				}
			}
			
			return (IRenderExtension) render;
		}
	}
}