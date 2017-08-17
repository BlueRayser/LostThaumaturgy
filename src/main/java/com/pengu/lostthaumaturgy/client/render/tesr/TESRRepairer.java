package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.model.ModelGear;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileRepairer;

public class TESRRepairer extends TESR<TileRepairer>
{
	public static final TESRRepairer INSTANCE = new TESRRepairer();
	public static final ResourceLocation gear = new ResourceLocation(LTInfo.MOD_ID, "textures/models/gear.png");
	private ModelGear model = new ModelGear();
	
	@Override
	public void renderBase(TileRepairer tile, ItemStack stack, double x, double y, double z, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		int count = tile != null ? tile.ticksExisted : 0;
		int angle = 0;
		if(tile != null && tile.worked)
			angle = count % 360;
		bindTexture(gear);
		GL11.glEnable(2977);
		GL11.glEnable(3042);
		GL11.glPushMatrix();
		GL11.glEnable(32826);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glTranslated(x + .5, y + .5, z + .5);
		
		GL11.glPushMatrix();
		GL11.glScaled(.43, .94, .94);
		GL11.glRotated(angle, 1, 0, 0);
		model.render();
		GL11.glScalef(1, 1, 1);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glScaled(.94, .94, .43);
		GL11.glRotatef(90, 0, 1, 0);
		GL11.glRotatef(angle, 1, 0, 0);
		model.render();
		GL11.glScalef(1, 1, 1);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glScaled(.94, .43, .94);
		GL11.glRotatef(90, 0, 0, 1);
		GL11.glRotatef(angle, 1, 0, 0);
		model.render();
		GL11.glScalef(1, 1, 1);
		GL11.glPopMatrix();
		
		GL11.glDisable(32826);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
		sbr.rb.renderAlpha = 1;
		
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(tile, sbr.rb));
		TextureAtlasSprite stex = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/repairer_side");
		TextureAtlasSprite ttex = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/repairer_top");
		for(int i = 0; i < (destroyProgress > 0 ? 2 : 1); ++i)
		{
			if(i == 1)
				stex = ttex = DestroyStageTexture.getAsSprite(destroyProgress);
			
			sbr.setSprite(stex);
			sbr.setSpriteForSide(EnumFacing.UP, ttex);
			sbr.setSpriteForSide(EnumFacing.DOWN, ttex);
			
			sbr.setRenderBounds(0, 0, 0, 7 / 16D, 7 / 16D, 7 / 16D);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(9 / 16D, 0, 0, 1, 7 / 16D, 7 / 16D);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(0, 0, 9 / 16D, 7 / 16D, 7 / 16D, 1);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(9 / 16D, 0, 9 / 16D, 1, 7 / 16D, 1);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(0, 9 / 16D, 0, 7 / 16D, 1, 7 / 16D);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(9 / 16D, 9 / 16D, 0, 1, 1, 7 / 16D);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(0, 9 / 16D, 9 / 16D, 7 / 16D, 1, 1);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(9 / 16D, 9 / 16D, 9 / 16D, 1, 1, 1);
			sbr.drawBlock(x, y, z);
		}
		sbr.end();
		
		rb.renderAlpha = srcAlpha;
	}
}