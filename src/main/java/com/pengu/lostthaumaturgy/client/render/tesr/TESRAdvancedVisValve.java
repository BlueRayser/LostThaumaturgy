package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.tile.TileAdvancedVisValve;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class TESRAdvancedVisValve extends TESRConduit<TileAdvancedVisValve>
{
	public static final TESRAdvancedVisValve INSTANCE = new TESRAdvancedVisValve();
	
	@Override
	public void renderItem(ItemStack item)
	{
		super.renderItem(item);
		NBTTagCompound itemNBT = getNBTFromItemStack(item);
		if(itemNBT == null)
			renderValve(false, 0, 0, 0, 0);
	}
	
	@Override
	public void renderTileEntityAt(TileAdvancedVisValve te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage, alpha);
		
		RenderBlocks rb = RenderBlocks.forMod(Info.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		GlStateManager.disableLighting();
		TextureAtlasSprite sprite = te.setting == 0 ? ClientProxy.getSprite(Info.MOD_ID + ":blocks/advanced_vis_valve_off") : te.setting == 2 ? ClientProxy.getSprite(Info.MOD_ID + ":blocks/advanced_vis_valve_taint") : ClientProxy.getSprite(Info.MOD_ID + ":blocks/advanced_vis_valve_vis");
		Tessellator tess = Tessellator.getInstance();
		int bright = getBrightnessForRB(te, rb);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		rb.setRenderBounds(4 / 16D, 4 / 16D, 4 / 16D, 12 / 16D, 12 / 16D, 12 / 16D);
		rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
		tess.draw();
		blend.reset();
		
		rb.renderAlpha = srcAlpha;
	}
	
	@Override
	public void renderFromNBT(NBTTagCompound nbt, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		super.renderFromNBT(nbt, x, y, z, partialTicks, destroyStage);
		renderValve(false, nbt.getInteger("Setting"), x, y, z);
	}
	
	private void renderValve(boolean isTileRendering, int open, double x, double y, double z)
	{
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		GlStateManager.disableLighting();
		
		TextureAtlasSprite sprite = open == 0 ? ClientProxy.getSprite(Info.MOD_ID + ":blocks/advanced_vis_valve_off") : open == 2 ? ClientProxy.getSprite(Info.MOD_ID + ":blocks/advanced_vis_valve_taint") : ClientProxy.getSprite(Info.MOD_ID + ":blocks/advanced_vis_valve_vis");
		
		Tessellator tess = Tessellator.getInstance();
		
		RenderBlocks rb = RenderBlocks.forMod(Info.MOD_ID);
		
		int bright = rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		rb.setRenderBounds(4 / 16D, 4 / 16D, 4 / 16D, 12 / 16D, 12 / 16D, 12 / 16D);
		
		rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
		rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
		
		tess.draw();
		
		blend.reset();
	}
}