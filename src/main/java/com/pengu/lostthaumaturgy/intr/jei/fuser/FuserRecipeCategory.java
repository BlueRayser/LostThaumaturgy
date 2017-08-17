package com.pengu.lostthaumaturgy.intr.jei.fuser;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LTInfo.JEIConstans;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class FuserRecipeCategory implements IRecipeCategory<FuserRecipeWrapper>
{
	IDrawable background;
	IDrawable icon;
	
	public FuserRecipeCategory(IGuiHelper helper)
	{
		background = helper.createDrawable(new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_fuser_jei.png"), 0, 0, 152, 90);
		icon = helper.createDrawable(new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_fuser_jei.png"), 240, 0, 16, 16);
		
	}
	
	@Override
	public void drawExtras(Minecraft arg0)
	{
		
	}
	
	@Override
	public IDrawable getBackground()
	{
		GlStateManager.enableBlend();
		return background;
	}
	
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}
	
	@Override
	public String getTitle()
	{
		return BlocksLT.FUSER_MB.getLocalizedName();
	}
	
	@Override
	public String getUid()
	{
		return JEIConstans.FUSER;
	}
	
	@Override
	public void setRecipe(IRecipeLayout layout, FuserRecipeWrapper wrap, IIngredients ings)
	{
		NonNullList<List<ItemStack>> stacks = wrap.recipe.bakeInputItems();
		IGuiItemStackGroup items = layout.getItemStacks();
		for(int x = 0; x < 3; ++x)
			for(int y = 0; y < 3; ++y)
			{
				items.init(x + y * 3, true, 24 + x * 20, 12 + y * 20);
				items.set(x + y * 3, stacks.get(x + y * 3));
			}
		items.init(9, false, 120, 16);
		items.set(9, wrap.recipe.getJEIOutput());
	}
	
	@Override
	public String getModName()
	{
		return LTInfo.MOD_NAME;
	}
}