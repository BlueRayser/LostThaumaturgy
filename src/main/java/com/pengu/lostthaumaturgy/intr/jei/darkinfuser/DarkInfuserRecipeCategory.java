package com.pengu.lostthaumaturgy.intr.jei.darkinfuser;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.Info.JEIConstans;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class DarkInfuserRecipeCategory implements IRecipeCategory<DarkInfuserRecipeWrapper>
{
	IDrawable background;
	IDrawable icon;
	
	public DarkInfuserRecipeCategory(IGuiHelper helper)
	{
		background = helper.createDrawable(new ResourceLocation(Info.MOD_ID, "textures/gui/gui_dark_infuser_jei.png"), 0, 0, 154, 36);
		
		ResourceLocation symbol = new ResourceLocation(Info.MOD_ID, "textures/misc/dark_infuser_symbol.png");
		
		icon = new IDrawable()
		{
			@Override
			public int getWidth()
			{
				return 16;
			}
			
			@Override
			public int getHeight()
			{
				return 16;
			}
			
			@Override
			public void draw(Minecraft mc, int x, int y)
			{
				GL11.glPushMatrix();
				GL11.glTranslated(x, y, 0);
				draw(mc);
				GL11.glPopMatrix();
			}
			
			@Override
			public void draw(Minecraft mc)
			{
				GlStateManager.enableBlend();
				
				GL11.glPushMatrix();
				Color.glColourRGB(0x442244);
				GL11.glBlendFunc(770, 1);
				mc.getTextureManager().bindTexture(symbol);
				GL11.glTranslated(getWidth() / 2D, getHeight() / 2D, 0);
				GL11.glRotated(System.currentTimeMillis() % 36000L / 100D, 0, 0, 1);
				GL11.glTranslated(getWidth() / -2D, getHeight() / -2D, 0);
				GL11.glScaled(getWidth() / 256D, getHeight() / 256D, 0);
				RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
				Color.glColourRGB(0xFFFFFF);
				GL11.glBlendFunc(770, 771);
				GL11.glPopMatrix();
			}
		};
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
		return icon;
	}
	
	@Override
	public String getTitle()
	{
		return BlocksLT.INFUSER_DARK.getLocalizedName();
	}
	
	@Override
	public String getUid()
	{
		return JEIConstans.DARK_INFUSER;
	}
	
	@Override
	public void setRecipe(IRecipeLayout layout, DarkInfuserRecipeWrapper recipe, IIngredients ingredients)
	{
		IGuiItemStackGroup items = layout.getItemStacks();
		
		items.init(0, false, 118, 0);
		items.set(0, recipe.recipe.result);
		
		int inputCount = Math.min(recipe.recipe.components.length, 6);
		
		for(int i = 0; i < inputCount; ++i)
		{
			items.init(2 + i, true, 18 * i, 0);
			items.set(2 + i, recipe.recipe.components[i]);
		}
		
		int start = 2 + inputCount;
		
		ItemStack[] stacks = recipe.recipe.discoveries;
		
		for(int i = 0; i < stacks.length; ++i)
		{
			items.init(start + i, false, i * 18, 18);
			items.set(start + i, stacks[i]);
		}
		
		start += stacks.length;
	}
	
	@Override
	public String getModName()
	{
		return Info.MOD_NAME;
	}
}