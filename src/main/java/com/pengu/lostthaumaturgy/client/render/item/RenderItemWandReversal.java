package com.pengu.lostthaumaturgy.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.IItemRender;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.client.model.ModelCrystal;
import com.pengu.lostthaumaturgy.client.model.ModelWandReversal;
import com.pengu.lostthaumaturgy.core.Info;

public class RenderItemWandReversal implements IItemRender
{
	private final ModelWandReversal model = new ModelWandReversal();
	private final ResourceLocation//
	        texture = new ResourceLocation(Info.MOD_ID, "textures/models/wand_reversal.png"), //
	        texture1 = new ResourceLocation(Info.MOD_ID, "textures/models/crystal.png");
	private final ModelCrystal crystal = new ModelCrystal();
	
	@Override
	public void renderItem(ItemStack stack)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(.6, .6, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glRotated(180, 0, 0, 1);
		GL11.glScaled(2, 2, 2);
		GL11.glRotated(45, 1, 0, 1);
		GL11.glTranslated(0, 0, .12);
		model.render(null, 0, 0, -.1F, 0, 0, .0625F);
		
		GlStateManager.disableLighting();
		
		int r = (int) (Math.cos(System.currentTimeMillis() / 100D) * 12 + 220);
		Color.glColourRGB(MathHelper.multiplyColor(0xAA00FF, Color.packARGB(r, r, r, 255)));
		GL11.glTranslated(.025, -.45, .025);
		GL11.glScaled(.25, .3, .25);
		GL11.glScalef((.15F + .05F), (.5F + .04F), (0.15f + .02F));
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture1);
		
		GL11.glPushMatrix();
		GL11.glRotated((System.currentTimeMillis() + stack.hashCode()) % 36000L / 100D, 0, 1, 0);
		crystal.render(null, 0, 0, -.1F, 0, 0, .0625F);
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();
		Color.glColourRGB(0xFFFFFF);
	}
}