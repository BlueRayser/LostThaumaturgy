package com.pengu.lostthaumaturgy.client.render.tesr;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileAdvancedVisValve;
import com.pengu.lostthaumaturgy.tile.TileConduit;
import com.pengu.lostthaumaturgy.tile.TileVisValve;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class TESRAdvancedVisValve extends TESRConduit<TileAdvancedVisValve>
{
	public static final TESRAdvancedVisValve INSTANCE = new TESRAdvancedVisValve();
	
	@Override
	public void renderItem(ItemStack item)
	{
		super.renderItem(item);
		NBTTagCompound itemNBT = getNBTFromItemStack(item);
		if(itemNBT == null) renderValve(false, 0, 0, 0, 0);
	}
	
	@Override
	public void renderTileEntityAt(TileAdvancedVisValve te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		renderValve(true, te.setting, x, y, z);
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
		
		TextureAtlasSprite sprite = open == 0 ? ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/advanced_vis_valve_off") : open == 2 ? ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/advanced_vis_valve_taint") : ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/advanced_vis_valve_vis");
		
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