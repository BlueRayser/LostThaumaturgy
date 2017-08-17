package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileWandConstructor;

public class TESRWandConstructor extends TESR<TileWandConstructor>
{
	public static final TESRWandConstructor INSTANCE = new TESRWandConstructor();
	
	@Override
	public void renderTileEntityAt(TileWandConstructor te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		renderModel(te, x, y, z);
		
		rb.renderAlpha = srcAlpha;
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		renderModel(null, 0, 0, 0);
	}
	
	public void renderModel(TileWandConstructor tile, double x, double y, double z)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		int bright = getBrightnessForRB(tile, rb);
		
		GlStateManager.disableLighting();
		
		Tessellator tess = Tessellator.getInstance();
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		TextureAtlasSprite side = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/wand_constructor/side");
		TextureAtlasSprite bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/wand_constructor/bottom");
		TextureAtlasSprite top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/wand_constructor/top");
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		rb.renderFromInside = false;
		rb.setRenderBounds(4 / 16D, 0, 4 / 16D, 12 / 16D, 2 / 16D, 12 / 16D);
		
		rb.renderFaceXNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, bottom, 1, 1, 1, bright);
		rb.renderFaceYPos(x, y, z, bottom, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, side, 1, 1, 1, bright);
		
		rb.setRenderBounds(6 / 16D, 2 / 16D, 6 / 16D, 10 / 16D, 9 / 16D, 10 / 16D);
		
		rb.renderFaceXNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, side, 1, 1, 1, bright);
		
		rb.setRenderBounds(0, 9 / 16D, 0, 1, 11 / 16D, 1);
		
		rb.renderFaceXNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, top, 1, 1, 1, bright);
		rb.renderFaceYPos(x, y, z, top, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, side, 1, 1, 1, bright);
		
		rb.setRenderBounds(0, 8 / 16D, 0, 4 / 16D, 9 / 16D, 4 / 16D);
		rb.renderFaceXNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, top, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, side, 1, 1, 1, bright);
		rb.setRenderBounds(0, 8 / 16D, 12 / 16D, 4 / 16D, 9 / 16D, 1);
		rb.renderFaceXNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, top, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, side, 1, 1, 1, bright);
		rb.setRenderBounds(12 / 16D, 8 / 16D, 0, 1, 9 / 16D, 4 / 16D);
		rb.renderFaceXNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, top, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, side, 1, 1, 1, bright);
		rb.setRenderBounds(12 / 16D, 8 / 16D, 12 / 16D, 1, 9 / 16D, 1);
		rb.renderFaceXNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, top, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, side, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, side, 1, 1, 1, bright);
		
		tess.draw();
	}
}