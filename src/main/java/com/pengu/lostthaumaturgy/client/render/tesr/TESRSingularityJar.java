package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileSingularityJar;

public class TESRSingularityJar extends TESR<TileSingularityJar>
{
	public static final TESRSingularityJar INSTANCE = new TESRSingularityJar();
	public final ResourceLocation singularity = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/xp_singularity.png");
	
	public void renderBase(TileSingularityJar tile, ItemStack stack, double x, double y, double z, ResourceLocation destroyStage)
	{
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
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + .2, y + .2, z + .2);
			GlStateManager.enableRescaleNormal();
			
			GL11.glScaled(.5, .5, .5);
			GL11.glTranslated(.5, .5, .5);
			GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
			GlStateManager.rotate((float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX, 1, 0, 0);
			
			GL11.glPushMatrix();
			
			GL11.glRotated(System.currentTimeMillis() % 3600L / 10D, 0, 0, 1);
			GL11.glTranslated(-.5, -.5, -.5);
			
			GL11.glScaled(1 / 256D, 1 / 256D, 1 / 256D);
			RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			GL11.glPopMatrix();
			
			GlStateManager.disableRescaleNormal();
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
	}
}