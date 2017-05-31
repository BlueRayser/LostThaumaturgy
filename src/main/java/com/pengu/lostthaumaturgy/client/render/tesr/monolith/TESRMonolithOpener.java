package com.pengu.lostthaumaturgy.client.render.tesr.monolith;

import net.minecraft.client.renderer.GlStateManager;
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
import com.pengu.lostthaumaturgy.tile.monolith.TileMonolithOpener;

public class TESRMonolithOpener extends TESR<TileMonolithOpener>
{
	public static final TESRMonolithOpener INSTANCE = new TESRMonolithOpener();
	
	@Override
	public void renderItem(ItemStack item)
	{
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(f == EnumFacing.UP)
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/bottom"));
			else
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/eldritch_block/" + f.ordinal()));
		}
		sbr.drawBlock(0, 0, 0);
		sbr.end();
	}
	
	@Override
	public void renderTileEntityAt(TileMonolithOpener te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		te.rand.setSeed(te.getPos().toLong() + te.getWorld().getSeed());
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .01, y + .01, z + .01);
		GL11.glScaled(.98, .98, .98);
		HammerCore.renderProxy.getRenderHelper().renderEndPortalEffect(0, 0, 0, EnumFacing.UP);
		GL11.glPopMatrix();
		
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(!BlocksLT.MONOLITH_OPENER.shouldSideBeRendered(te.getWorld().getBlockState(te.getPos()), te.getWorld(), te.getPos(), f))
				sbr.disableFace(f);
			
			if(f == EnumFacing.UP)
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/bottom"));
			else
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/eldritch_block/" + te.rand.nextInt(6)));
		}
		sbr.drawBlock(x, y, z);
		sbr.end();
	}
}