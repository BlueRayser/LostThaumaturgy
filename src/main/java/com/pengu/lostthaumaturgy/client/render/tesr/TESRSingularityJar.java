package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileSingularityJar;

public class TESRSingularityJar extends TESR<TileSingularityJar>
{
	public static final TESRSingularityJar INSTANCE = new TESRSingularityJar();
	public final ResourceLocation singularity = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/xp_singularity.png");
	
	@Override
	public void renderBase(TileSingularityJar tile, ItemStack stack, double x, double y, double z, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
		
		for(int i = 0; i < (destroyStage != null ? 2 : 1); ++i)
		{
			TextureAtlasSprite glass = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/warded_glass");
			TextureAtlasSprite jar = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/jar");
			
			if(i == 1)
				glass = jar = DestroyStageTexture.getAsSprite(destroyProgress);
			
			sbr.begin();
			sbr.setBrightness(getBrightnessForRB(tile, sbr.rb));
			sbr.setSprite(jar);
			sbr.setRenderBounds(1 / 4D, 0, 1 / 4D, 3 / 4D, 1 / 8D, 3 / 4D);
			sbr.drawBlock(x, y, z);
			sbr.setRenderBounds(1 / 4D, 7 / 8D, 1 / 4D, 3 / 4D, 1, 3 / 4D);
			sbr.drawBlock(x, y, z);
			sbr.end();
		}
		
		if(stack == null)
		{
			GlStateManager.disableLighting();
			GLRenderState.BLEND.on();
			
			RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			bindTexture(singularity);
			
			float rot = (float) (System.currentTimeMillis() % 3600D) / 10F;
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + .5, y + .5, z + .5);
			GL11.glPushMatrix();
			
			GlStateManager.disableLighting();
			GLRenderState.BLEND.on();
			
			bindTexture(singularity);
			
			float f1 = ActiveRenderInfo.getRotationX();
			float f2 = ActiveRenderInfo.getRotationXZ();
			float f3 = ActiveRenderInfo.getRotationZ();
			float f4 = ActiveRenderInfo.getRotationYZ();
			float f5 = ActiveRenderInfo.getRotationXY();
			float f10 = .4F;
			float f11 = 0;
			float f12 = 0;
			float f13 = 0;
			Tessellator tessellator = Tessellator.getInstance();
			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			
			int bright = getBrightnessForRB(tile, sbr.rb);
			float r = 1F, g = 1, b = 1F, a = 1F;
			
			tessellator.getBuffer().pos(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10).tex(0, 1).color(r, g, b, a).endVertex();
			tessellator.getBuffer().pos(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10).tex(1, 1).color(r, g, b, a).endVertex();
			tessellator.getBuffer().pos(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10).tex(1, 0).color(r, g, b, a).endVertex();
			tessellator.getBuffer().pos(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10).tex(0, 0).color(r, g, b, a).endVertex();
			
			tessellator.draw();
			
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
		}
		
		for(int i = 0; i < (destroyStage != null ? 2 : 1); ++i)
		{
			TextureAtlasSprite glass = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/warded_glass");
			TextureAtlasSprite jar = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/jar");
			
			if(i == 1)
				glass = jar = DestroyStageTexture.getAsSprite(destroyProgress);
			
			sbr.begin();
			sbr.setBrightness(getBrightnessForRB(tile, sbr.rb));
			sbr.setSprite(glass);
			sbr.setRenderBounds(1 / 8D, 1 / 8D, 1 / 8D, 7 / 8D, 7 / 8D, 7 / 8D);
			sbr.drawBlock(x, y, z);
			sbr.end();
		}
		
		rb.renderAlpha = srcAlpha;
	}
}