package com.pengu.lostthaumaturgy.client.render.tesr.monolith;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.monolith.TileMonolith;

public class TESRMonolith extends TESR<TileMonolith>
{
	public static final TESRMonolith INSTANCE = new TESRMonolith();
	
	@Override
	public void renderTileEntityAt(TileMonolith te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		boolean up = te.getWorld().getBlockState(te.getPos().up()).getBlock() == BlocksLT.MONOLITH;
		boolean down = te.getWorld().getBlockState(te.getPos().down()).getBlock() == BlocksLT.MONOLITH;
		
		TextureAtlasSprite side = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/side_" + (up && !down ? "u" : !up && down ? "d" : "ud"));
		TextureAtlasSprite yspr = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/bottom");
		
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y + te.getYOffset(0), z);
		
		GL11.glPushMatrix();
		if(up && !down)
		{
			GL11.glTranslated(.01, .01, .01);
			GL11.glScaled(.98, 1.1, .98);
		} else if(!up && down)
		{
			GL11.glTranslated(.01, -.11, .01);
			GL11.glScaled(.98, 1.1, .98);
		} else
		{
			GL11.glTranslated(.01, .01, .01);
			GL11.glScaled(.98, .98, .98);
		}
		HammerCore.renderProxy.getRenderHelper().renderEndPortalEffect(0, 0, 0, EnumFacing.VALUES);
		GL11.glPopMatrix();
		
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(te, sbr.rb));
		
		sbr.setSidedSprites(yspr, yspr, side);
		sbr.setRenderBounds(0, 0, 0, 1, 1, 1);
		if(up)
			sbr.disableFace(EnumFacing.UP);
		if(down)
			sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, 0, 0);
		
		sbr.end();
		
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		
		TextureAtlasSprite side = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/side_d");
		TextureAtlasSprite yspr = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/bottom");
		
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
		sbr.setSidedSprites(yspr, yspr, side);
		sbr.setRenderBounds(0, 0, 0, 1, 1, 1);
		sbr.drawBlock(0, 0, 0);
		sbr.end();
	}
}