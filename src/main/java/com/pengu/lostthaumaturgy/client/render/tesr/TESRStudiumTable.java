package com.pengu.lostthaumaturgy.client.render.tesr;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileStudiumTable;

public class TESRStudiumTable extends TESR<TileStudiumTable>
{
	public static final TESRStudiumTable INSTANCE = new TESRStudiumTable();
	protected ResourceLocation disk = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/studium_symbol.png");
	
	@Override
	public void renderTileEntityAt(TileStudiumTable te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		renderModel(te, x, y, z);
		renderDisk(x, y, z, te.researchProgress * 360F, te.researchProgress > 0);
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + 14 / 16D, z + .5);
		GL11.glRotated(te.researchProgress * -360F, 0, 1, 0);
		Minecraft.getMinecraft().getRenderItem().renderItem(te.inventory.getStackInSlot(0), TransformType.GROUND);
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		renderModel(null, 0, 0, 0);
		renderDisk(0, 0, 0, 0, false);
	}
	
	public void renderModel(TileStudiumTable tile, double x, double y, double z)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		int bright = getBrightnessForRB(tile, rb);
		
		GlStateManager.disableLighting();
		
		Tessellator tess = Tessellator.getInstance();
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		TextureAtlasSprite side = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/studium_table/side");
		TextureAtlasSprite bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/studium_table/bottom");
		TextureAtlasSprite top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/studium_table/top");
		
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
	
	public void renderDisk(double x, double y, double z, double rotation, boolean active)
	{
		active = true;
		
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		double activeRed = 1 - Math.sin(rotation / 90);
		double activeGreen = .35;
		double activeBlue = 1;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y + 11.1 / 16, z);
		GL11.glRotated(90, 1, 0, 0);
		
		GL11.glDepthMask(false);
		
		GL11.glScaled(.85, .85, .85);
		
		GL11.glTranslated(.59, .59, 0);
		GL11.glRotated(rotation, 0, 0, 1);
		GL11.glTranslated(-.5, -.5, 0);
		
		GL11.glScaled(1 / 256D, 1 / 256D, 1 / 256D);
		
		if(active) GL11.glBlendFunc(770, 1);
		else GL11.glBlendFunc(770, 771);
		
		if(active) GL11.glColor4d(activeRed, activeGreen, activeBlue, 1);
		else GL11.glColor4f(0, 0, 0, 1);
		
		bindTexture(disk);
		RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
		GL11.glPopMatrix();
		
		GL11.glDepthMask(true);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1, 1, 1, 1);
		
		blend.reset();
	}
}