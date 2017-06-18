package com.pengu.lostthaumaturgy.intr.jei.fuser;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;

public class FuserRecipeWrapper implements IRecipeWrapper
{
	public final IFuserRecipe recipe;
	
	public FuserRecipeWrapper(IFuserRecipe recipe)
	{
		this.recipe = recipe;
	}
	
	@Override
	public void drawInfo(Minecraft mc, int arg1, int arg2, int arg3, int arg4)
	{
		String v = recipe.getVisUsage() + " V";
		String t = recipe.getTaintUsage() + " T";
		
		GL11.glPushMatrix();
		double maxLen = 24;
		int w = mc.fontRenderer.getStringWidth(v);
		GL11.glTranslated(117.5, 62 + (w > maxLen ? 1D - maxLen / w : 0), 0);
		if(w > maxLen)
			GL11.glScaled(maxLen / w, maxLen / w, 1);
		RenderUtil.drawTextRGBA(mc.fontRenderer, v, 0, 0, 238, 204, 238, 255);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		w = mc.fontRenderer.getStringWidth(t);
		GL11.glTranslated(117.5, mc.fontRenderer.FONT_HEIGHT + 62 + (w > maxLen ? 1D - maxLen / w : 0), 0);
		if(w > maxLen)
			GL11.glScaled(maxLen / w, maxLen / w, 1);
		RenderUtil.drawTextRGBA(mc.fontRenderer, t, 0, 0, 153, 119, 153, 255);
		GL11.glPopMatrix();
	}
	
	@Override
	public void getIngredients(IIngredients ing)
	{
		ing.setInputLists(ItemStack.class, recipe.bakeInputItems());
		ing.setOutput(ItemStack.class, recipe.getOutput());
	}
	
	@Override
	public List<String> getTooltipStrings(int arg0, int arg1)
	{
		return null;
	}
	
	@Override
	public boolean handleClick(Minecraft arg0, int arg1, int arg2, int arg3)
	{
		return false;
	}
}