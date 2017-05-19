package com.pengu.lostthaumaturgy.intr.jei.infuser;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.InfuserRecipe;
import com.pengu.lostthaumaturgy.client.render.shared.LiquidVisRenderer;
import com.pengu.lostthaumaturgy.custom.research.ResearchPredicate;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;

public class InfuserRecipeWrapper implements IRecipeWrapper
{
	public final InfuserRecipe recipe;
	
	public InfuserRecipeWrapper(InfuserRecipe recipe)
    {
		this.recipe = recipe;
    }
	
	private static final ResourceLocation symbol = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/infuser_symbol.png");
	
	@Override
	public void drawInfo(Minecraft mc, int arg1, int arg2, int arg3, int arg4)
	{
		for(int i = 0; i < recipe.discoveries.length; ++i)
			RenderUtil.drawTexturedModalRect(i * 18, 18, 0, 37, 18, 18);
		
		GLRenderState.BLEND.on();
		
		GL11.glPushMatrix();
		GL11.glTranslated(113, 0, 0);
		
		Color.glColourRGB(0xFF88FF);
		
		GL11.glBlendFunc(770, 1);
		
		mc.getTextureManager().bindTexture(symbol);
		
		GL11.glTranslated(8, 8, 0);
		GL11.glRotated((System.currentTimeMillis() + recipe.hashCode() * 3) % 3600L / 10D, 0, 0, 1);
		GL11.glTranslated(-8, -8, 0);
		
		GL11.glScaled(16 / 256D, 16 / 256D, 0);
		RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
		GL11.glPopMatrix();
		
		Color.glColourRGB(0xFFFFFF);
		
		GL11.glBlendFunc(770, 771);
		
		LiquidVisRenderer.renderIntoGui(109, 19, 45 * Math.min(1, recipe.cost / 500F), 16, 1);
		
		GL11.glPushMatrix();
		GL11.glTranslated(156.5, 20, 0);
		GL11.glScaled(.8, .8, 1);
		if(recipe.depletedShards > 0) mc.fontRenderer.drawString("0-" + recipe.depletedShards, 0, 0, 0xFFFFFF, true);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslated(131.5, 24, 0);
		GL11.glScaled(.8, .8, 1);
		FontRenderer f = mc.fontRenderer;
		String s = recipe.cost + " Vis";
		f.drawString(s, -f.getStringWidth(s) / 2, 0, 0xFFFFFF, true);
		GL11.glPopMatrix();
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