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
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TilePressurizedConduit;

public class TESRPressurizedConduit extends TESRConduit<TilePressurizedConduit>
{
	public static final TESRPressurizedConduit INSTANCE = new TESRPressurizedConduit();
	
	@Override
	public void renderItem(ItemStack item)
	{
		super.renderItem(item);
		NBTTagCompound itemNBT = getNBTFromItemStack(item);
		if(itemNBT == null)
			renderValve(false, false, 0, 0, 0);
	}
	
	@Override
	public void renderTileEntityAt(TilePressurizedConduit te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage, alpha);
		
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		GlStateManager.disableLighting();
		
		TextureAtlasSprite sprite = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/pressurized_conduit_" + (te.getSuction(null) > 0F ? "on" : "off"));
		
		Tessellator tess = Tessellator.getInstance();
		
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		
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
	}
	
	@Override
	public void renderFromNBT(NBTTagCompound nbt, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		super.renderFromNBT(nbt, x, y, z, partialTicks, destroyStage);
		renderValve(false, false, x, y, z);
	}
	
	private void renderValve(boolean isTileRendering, boolean on, double x, double y, double z)
	{
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		GlStateManager.disableLighting();
		
		TextureAtlasSprite sprite = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/pressurized_conduit_" + (on ? "on" : "off"));
		
		Tessellator tess = Tessellator.getInstance();
		
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		
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