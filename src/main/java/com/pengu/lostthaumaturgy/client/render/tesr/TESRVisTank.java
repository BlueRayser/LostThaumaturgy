package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileVisTank;

public class TESRVisTank extends TESR<TileVisTank>
{
	public static final TESRVisTank INSTANCE = new TESRVisTank();
	
	protected TextureAtlasSprite bottom, side, side_to_bottom, side_to_top, side_to_top_bottom, top;
	
	protected void setupTextures()
	{
		bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_tank/bottom");
		side = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_tank/side");
		side_to_bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_tank/side_to_bottom");
		side_to_top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_tank/side_to_top");
		side_to_top_bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_tank/side_to_top_bottom");
		top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_tank/top");
	}
	
	@Override
	public void renderTileEntityAt(TileVisTank te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
		TextureAtlasSprite vis_conduit = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_conduit");
		
		setupTextures();
		
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		
		GLRenderState blend = GLRenderState.BLEND;
		for(int i = 0; i < (destroyStage != null && destroyProgress > 0F ? 2 : 1); ++i)
		{
			if(i == 1)
			{
				TextureAtlasSprite destroy = DestroyStageTexture.getAsSprite(destroyProgress);
				bottom = side = side_to_bottom = side_to_top = vis_conduit = side_to_top_bottom = top = destroy;
			}
			
			blend.captureState();
			blend.on();
			
			GlStateManager.disableLighting();
			
			Tessellator tess = Tessellator.getInstance();
			
			int bright = getBrightnessForRB(te, rb);
			
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			
			rb.renderFromInside = false;
			
			rb.setRenderBounds(1 / 16D, 0, 1 / 16D, 15 / 16D, 1, 15 / 16D);
			
			if(te == null)
			{
				rb.renderFaceXNeg(x, y, z, side, 1, 1, 1, bright);
				rb.renderFaceXPos(x, y, z, side, 1, 1, 1, bright);
				rb.renderFaceYNeg(x, y, z, bottom, 1, 1, 1, bright);
				rb.renderFaceYPos(x, y, z, top, 1, 1, 1, bright);
				rb.renderFaceZNeg(x, y, z, side, 1, 1, 1, bright);
				rb.renderFaceZPos(x, y, z, side, 1, 1, 1, bright);
			} else
			{
				IBlockState a = te.getWorld().getBlockState(te.getPos());
				IBlockState b = te.getWorld().getBlockState(te.getPos().up());
				IBlockState c = te.getWorld().getBlockState(te.getPos().down());
				
				boolean up = a.equals(b);
				boolean down = a.equals(c);
				
				TileVisTank topTank = WorldUtil.cast(te.getWorld().getTileEntity(te.getPos().up()), TileVisTank.class);
				
				TextureAtlasSprite sprite = side;
				
				if(up)
					sprite = side_to_top;
				if(down)
					sprite = side_to_bottom;
				if(up && down)
					sprite = side_to_top_bottom;
				
				rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
				
				if(!down)
					rb.renderFaceYNeg(x, y, z, bottom, 1, 1, 1, bright);
				if(!up)
					rb.renderFaceYPos(x, y, z, top, 1, 1, 1, bright);
				
				rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
				
				float wx = 0;
				float w1 = 1.0625F / 16F;
				
				if(te.pureVis + te.taintedVis > .1F)
				{
					float hfill = (te.pureVis + te.taintedVis) / te.getMaxVis();
					float d = Math.min(1F, te.taintedVis / (te.taintedVis + te.pureVis));
					int f = 20 + (int) (d * 210);
					
					boolean filled = te.pureVis + te.taintedVis >= te.getMaxVis() * .9F;
					boolean topFill = topTank != null && topTank.pureVis + topTank.taintedVis > .1F;
					
					rb.setRenderBounds(wx + w1, wx + (down ? 0 : .003F), wx + w1, 1F - 0.003f - w1, wx + hfill - (up ? 0 : .003F), 1F - wx - w1);
					
					if(!filled || !up || !topFill)
						rb.renderFaceYPos(x, y, z, vis, f, f, f, bright);
					rb.renderFaceXNeg(x, y, z, vis, f, f, f, bright);
					rb.renderFaceXPos(x, y, z, vis, f, f, f, bright);
					rb.renderFaceYNeg(x, y, z, vis, f, f, f, bright);
					rb.renderFaceZNeg(x, y, z, vis, f, f, f, bright);
					rb.renderFaceZPos(x, y, z, vis, f, f, f, bright);
				}
			}
			
			if(te != null)
				TESRConduit.drawPipeConnection(x, y, z, vis_conduit, 1, 1, 1, bright, 5D, te);
			
			tess.draw();
		}
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		renderTileEntityAt(null, 0, 0, 0, 0, null);
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
		
		TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
		
		Tessellator tess = Tessellator.getInstance();
		
		RenderBlocks rb = RenderBlocks.getInstance();
		
		int bright = rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		float pureVis = nbt.getFloat("PureVis");
		float taintedVis = nbt.getFloat("TaintedVis");
		float maxVis = nbt.getFloat("MaxVis");
		
		float wx = 0;
		float w1 = 2.0625F / 16F;
		
		if(pureVis + taintedVis > .1F)
		{
			float hfill = (1F - wx * 2F) * ((pureVis + taintedVis) / maxVis);
			float d = Math.min(1F, taintedVis / (taintedVis + pureVis));
			int f = 20 + (int) (d * 210);
			
			boolean filled = pureVis + taintedVis >= maxVis;
			
			rb.setRenderBounds(wx + w1, wx + 0.003f, wx + w1, 1F - 0.003f - w1, wx + hfill - 0.003f, 1F - wx - w1);
			
			rb.renderFaceYPos(x, y, z, vis, f, f, f, bright);
			rb.renderFaceXNeg(x, y, z, vis, f, f, f, bright);
			rb.renderFaceXPos(x, y, z, vis, f, f, f, bright);
			rb.renderFaceYNeg(x, y, z, vis, f, f, f, bright);
			rb.renderFaceZNeg(x, y, z, vis, f, f, f, bright);
			rb.renderFaceZPos(x, y, z, vis, f, f, f, bright);
		}
		
		tess.draw();
		
		blend.reset();
	}
}