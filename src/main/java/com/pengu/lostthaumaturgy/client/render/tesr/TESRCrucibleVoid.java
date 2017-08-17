package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.render.shared.LiquidVisRenderer;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileCrucibleVoid;

public class TESRCrucibleVoid extends TESR<TileCrucibleVoid>
{
	public static final TESRCrucibleVoid INSTANCE = new TESRCrucibleVoid();
	
	@Override
	public void renderTileEntityAt(TileCrucibleVoid te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		try
		{
			GLRenderState blend = GLRenderState.BLEND;
			
			String sub = te != null && te.emitsPower() ? "on" : "off";
			TextureAtlasSprite side_disconnected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_side_disconnected_" + sub);
			TextureAtlasSprite bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_bottom");
			TextureAtlasSprite top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_top");
			TextureAtlasSprite spill = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucible_spill");
			TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
			TextureAtlasSprite base = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_inner");
			TextureAtlasSprite side_connected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_side_connected_" + sub);
			
			for(int i = 0; i < (destroyStage != null && destroyProgress > 0F ? 2 : 1); ++i)
			{
				if(i == 1)
				{
					TextureAtlasSprite destroy = DestroyStageTexture.getAsSprite(destroyProgress);
					side_disconnected = side_connected = bottom = base = destroy;
				}
				
				blend.captureState();
				blend.on();
				
				GlStateManager.disableLighting();
				
				Tessellator tess = Tessellator.getInstance();
				
				int bright = te != null ? rb.setLighting(te.getWorld(), te.getPos()) : rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
				
				boolean isConnectable = te != null;
				
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
				
				rb.setRenderBounds(0, 0, 0, 1, 1, 1);
				rb.renderFromInside = false;
				
				if(isConnectable)
				{
					rb.renderFaceXNeg(x, y, z, te.isConnected(EnumFacing.WEST) ? side_connected : side_disconnected, 1, 1, 1, bright);
					rb.renderFaceXPos(x, y, z, te.isConnected(EnumFacing.EAST) ? side_connected : side_disconnected, 1, 1, 1, bright);
					rb.renderFaceZNeg(x, y, z, te.isConnected(EnumFacing.NORTH) ? side_connected : side_disconnected, 1, 1, 1, bright);
					rb.renderFaceZPos(x, y, z, te.isConnected(EnumFacing.SOUTH) ? side_connected : side_disconnected, 1, 1, 1, bright);
				} else
				{
					rb.renderFaceXNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
					rb.renderFaceXPos(x, y, z, side_disconnected, 1, 1, 1, bright);
					rb.renderFaceZNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
					rb.renderFaceZPos(x, y, z, side_disconnected, 1, 1, 1, bright);
				}
				
				rb.renderFaceYPos(x, y, z, top, 1, 1, 1, bright);
				rb.renderFaceYNeg(x, y, z, bottom, 1, 1, 1, bright);
				
				rb.renderFromInside = true;
				
				double tp = 2 / 16D;
				double fp = 14 / 16D;
				rb.setRenderBounds(tp, 0, tp, fp, 1, fp);
				
				rb.renderFaceXNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
				rb.renderFaceXPos(x, y, z, side_disconnected, 1, 1, 1, bright);
				rb.renderFaceZNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
				rb.renderFaceZPos(x, y, z, side_disconnected, 1, 1, 1, bright);
				
				rb.renderFromInside = false;
				
				rb.setRenderBounds(0, 4 / 16D, 0, 1, 4 / 16D, 1);
				rb.renderFaceYPos(x, y, z, base, 1, 1, 1, bright);
				rb.renderFaceYNeg(x, y, z, base, 1, 1, 1, bright);
				
				tess.draw();
				
				if(te != null)
				{
					float b = LiquidVisRenderer.getVisSaturation(te.taintedVis, te.pureVis);
					int rgb = 20 + (int) (b * 210);
					
					float tvis = te.pureVis + te.taintedVis;
					float h2 = Math.min(tvis, te.maxVis);
					float level = .735F * (h2 / te.maxVis);
					if(te.maxVis < tvis)
						level = (float) (level + 0.26 / 16D);
					
					if(level > .001F)
					{
						tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
						
						double s0 = .015D / 16D;
						double s1 = 1 - s0;
						
						rb.setRenderBounds(s0, 4D / 16D, s0, s1, 4D / 16D + level, s1);
						
						rb.renderFaceYPos(x, y, z, vis, rgb, rgb, rgb, bright);
						
						if(tvis > te.maxVis && !LiquidVisRenderer.useShaders())
						{
							double off1 = -.001D / 16D;
							double off2 = 1 - off1;
							rb.setRenderBounds(off1, 0, off1, off2, 1 + .05 / 16D, off2);
							
							rb.renderFaceXNeg(x, y, z, spill, rgb, rgb, rgb, bright);
							rb.renderFaceXPos(x, y, z, spill, rgb, rgb, rgb, bright);
							rb.renderFaceZNeg(x, y, z, spill, rgb, rgb, rgb, bright);
							rb.renderFaceZPos(x, y, z, spill, rgb, rgb, rgb, bright);
						}
						
						if(DestroyStageTexture.getAsSprite(destroyProgress) == vis)
							tess.draw();
						else
							LiquidVisRenderer.finishDrawWithShaders(tess, 1 - b);
					}
				}
				
				blend.reset();
			}
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		
		rb.renderAlpha = srcAlpha;
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		renderTileEntityAt(null, 0, 0, 0, 0, null, 1);
		super.renderItem(item);
	}
	
	@Override
	public boolean canRenderFromNbt()
	{
		return true;
	}
	
	@Override
	public void renderFromNBT(NBTTagCompound nbt, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		GlStateManager.disableLighting();
		
		float pureVis = nbt.getFloat("PureVis");
		float taintedVis = nbt.getFloat("TaintedVis");
		float maxVis = nbt.getFloat("MaxVis");
		
		TextureAtlasSprite side_disconnected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_side_disconnected_" + ((pureVis + taintedVis) >= maxVis * .9F ? "on" : "off"));
		TextureAtlasSprite bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_bottom");
		TextureAtlasSprite top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_top");
		TextureAtlasSprite spill = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucible_spill");
		TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
		TextureAtlasSprite base = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crucibles/void/crucible_inner");
		
		Tessellator tess = Tessellator.getInstance();
		
		RenderBlocks rb = RenderBlocks.getInstance();
		
		int bright = rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		float b = LiquidVisRenderer.getVisSaturation(taintedVis, pureVis);
		int rgb = 20 + (int) (b * 210);
		
		float tvis = pureVis + taintedVis;
		float h2 = Math.min(tvis, maxVis);
		float level = .735F * (h2 / maxVis);
		if(maxVis < tvis)
			level = (float) (level + 0.26 / 16D);
		
		if(level > 0)
		{
			double s0 = .015D / 16D;
			double s1 = 1 - s0;
			
			rb.setRenderBounds(s0, 4D / 16D, s0, s1, 4D / 16D + level, s1);
			
			rb.renderFaceYPos(0, 0, 0, vis, rgb, rgb, rgb, bright);
		}
		
		if(tvis > maxVis && !LiquidVisRenderer.useShaders())
		{
			double off1 = -.001D / 16D;
			double off2 = 1 - off1;
			rb.setRenderBounds(off1, 0, off1, off2, 1 + .05 / 16D, off2);
			
			rb.renderFaceXNeg(0, 0, 0, spill, rgb, rgb, rgb, bright);
			rb.renderFaceXPos(0, 0, 0, spill, rgb, rgb, rgb, bright);
			rb.renderFaceZNeg(0, 0, 0, spill, rgb, rgb, rgb, bright);
			rb.renderFaceZPos(0, 0, 0, spill, rgb, rgb, rgb, bright);
		}
		
		LiquidVisRenderer.finishDrawWithShaders(tess, 1 - b);
		
		blend.reset();
	}
	
}