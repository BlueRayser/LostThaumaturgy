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
import com.pengu.lostthaumaturgy.client.model.ModelWandOfItemFreeze;
import com.pengu.lostthaumaturgy.core.Info;

public class RenderItemWandOfItemFreeze implements IItemRender
{
	private final ModelWandOfItemFreeze model = new ModelWandOfItemFreeze();
	private final ResourceLocation//
	        texture = new ResourceLocation(Info.MOD_ID, "textures/models/wand_item_freeze.png"), //
	        texture1 = new ResourceLocation(Info.MOD_ID, "textures/models/crystal.png");
	private final ModelCrystal crystal = new ModelCrystal();
	
	@Override
	public void renderItem(ItemStack stack)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(-.3, 1.8, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glRotated(180, 0, 0, 1);
		GL11.glScaled(1.5, 1.5, 1.5);
		GL11.glRotated(45, 1, 0, 1);
		model.render(null, 0, 0, -.1F, 0, 0, .0625F);
		
		GlStateManager.disableLighting();
		
		int r = (int) (Math.cos(System.currentTimeMillis() / 100D) * 12 + 220);
		Color.glColourRGB(MathHelper.multiplyColor(0x5349FF, Color.packARGB(r, r, r, 255)));
		GL11.glTranslated(0, .2, 0);
		GL11.glScaled(.5, .5, .5);
		GL11.glScalef((.15F + .05F), (.5F + .04F), (0.15f + .02F));
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture1);
		crystal.render(null, 0, 0, -.1F, 0, 0, .0625F);
		GL11.glPopMatrix();
		Color.glColourRGB(0xFFFFFF);
	}
}