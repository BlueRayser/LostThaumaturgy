package com.pengu.lostthaumaturgy.intr.jei.infuser;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LTInfo.JEIConstans;
import com.pengu.lostthaumaturgy.custom.research.ResearchPredicate;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;

public class InfuserRecipeCategory implements IRecipeCategory<InfuserRecipeWrapper>
{
	IDrawable background;
	
	public InfuserRecipeCategory(IGuiHelper helper)
    {
		background = helper.createDrawable(new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_infuser_jei.png"), 0, 0, 154, 154);
    }
	
	@Override
    public void drawExtras(Minecraft mc)
    {
    }

	@Override
    public IDrawable getBackground()
    {
	    return background;
    }

	@Override
    public IDrawable getIcon()
    {
	    return null;
    }

	@Override
    public String getTitle()
    {
	    return BlocksLT.INFUSER.getLocalizedName();
    }

	@Override
    public String getUid()
    {
	    return JEIConstans.INFUSER;
    }
	
	@Override
    public void setRecipe(IRecipeLayout layout, InfuserRecipeWrapper recipe, IIngredients ingredients)
    {
		IGuiItemStackGroup items = layout.getItemStacks();
		
		items.init(0, false, 68, 68);
		items.set(0, recipe.recipe.result);
		
		if(recipe.recipe.depletedShards > 0)
		{
			items.init(1, false, 68, 131);
			items.set(1, EnumMultiMaterialType.DEPLETED_CRYSTAL.stack());
		}
		
		int[] xs = { 68, 16, 120, 38, 98, 68 };
		int[] ys = { 7, 98, 98, 51, 51, 102 };
		
		int inputCount = Math.min(recipe.recipe.components.length, xs.length);
		for(int i = 0; i < inputCount; ++i)
		{
			items.init(2 + i, true, xs[i], ys[i]);
			items.set(2 + i, recipe.recipe.components[i]);
		}
		
		int start = 2 + inputCount;
		
		if(recipe.recipe.predicate instanceof ResearchPredicate)
		{
			ResearchPredicate pred = (ResearchPredicate) recipe.recipe.predicate;
			ItemStack[] stacks = pred.getResearchItems(EnumResearchItemType.DISCOVERY);
			int xStart = 69 - stacks.length / 2;
			for(int i = 0; i < stacks.length; ++i)
			{
				items.init(start + i, false, xStart + i * 16, 32);
				items.set(start + i, stacks[i]);
			}
			start += stacks.length;
		}
    }
}