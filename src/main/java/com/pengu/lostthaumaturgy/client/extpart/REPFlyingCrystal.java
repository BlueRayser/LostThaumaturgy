package com.pengu.lostthaumaturgy.client.extpart;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.ParticleRenderer;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.model.ModelCrystal;

public class REPFlyingCrystal extends ParticleRenderer<EPFlyingCrystal>
{
	private final ResourceLocation texture = new ResourceLocation(LTInfo.MOD_ID, "textures/models/crystal.png");
	private final ModelCrystal crystal = new ModelCrystal();
	
	@Override
	public void doRender(EPFlyingCrystal particle, double x, double y, double z, float partialTicks)
	{
//		System.out.println(particle.motionX.get() + " " + particle.motionY.get() + " " + particle.motionZ.get() + " " + particle.posX.get() + ' ' + particle.posY.get() + " " + particle.posZ.get());
		
		GL11.glPushMatrix();
		GL11.glTranslated(x - .3, y + 1.8, z);
		
		GlStateManager.disableLighting();
		
		int r = (int) (Math.cos(System.currentTimeMillis() / 100D) * 12 + 220);
		Color.glColourRGB(MathHelper.multiplyColor(0x5349FF, Color.packARGB(r, r, r, 255)));
		GL11.glTranslated(0, .2, 0);
		GL11.glScaled(1.5, 1.5, 1.5);
		GL11.glScalef((.15F + .05F), (.5F + .04F), (0.15f + .02F));
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		crystal.render(null, 0, 0, -.1F, 0, 0, .0625F);
		
		Color.glColourRGB(0xFFFFFF);
		GL11.glPopMatrix();
	}
}