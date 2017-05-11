package com.pengu.lostthaumaturgy.client.render.tesr;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import com.google.common.base.Predicate;
import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileVisFilter;
import com.pengu.lostthaumaturgy.tile.TileVisTank;

public class TESRVisFilter extends TESR<TileVisFilter>
{
	public static final TESRVisFilter INSTANCE = new TESRVisFilter();
	
	@Override
	public void renderItem(ItemStack item)
	{
		destroyProgress = 0;
		render(0, 0, 0, null);
	}
	
	@Override
	public void renderTileEntityAt(TileVisFilter te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		render(x, y, z, te);
	}
	
	private void render(double x, double y, double z, Predicate<EnumFacing> connections)
	{
		TextureAtlasSprite vis_conduit = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_conduit");
		TextureAtlasSprite side_connected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_filter_side_connected");
		TextureAtlasSprite side_disconnected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_filter_side_disconnected");
		TextureAtlasSprite top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_filter_top");
		TextureAtlasSprite bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/vis_filter_bottom");
		
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		
		GLRenderState blend = GLRenderState.BLEND;
		for(int i = 0; i < (destroyProgress > 0F ? 2 : 1); ++i)
		{
			if(i == 1)
			{
				TextureAtlasSprite destroy = DestroyStageTexture.getAsSprite(destroyProgress);
				vis_conduit = side_connected = side_disconnected = top = bottom = destroy;
			}
			
			blend.captureState();
			blend.on();
			
			GlStateManager.disableLighting();
			
			Tessellator tess = Tessellator.getInstance();
			
			int bright = getBrightnessForRB(null, rb);
			
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			
			rb.renderFromInside = false;
			
			rb.setRenderBounds(2 / 16D, 0, 2 / 16D, 14 / 16D, 3 / 16D, 14 / 16D);
			
			rb.renderFaceXNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceXPos(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceYNeg(x, y, z, bottom, 1, 1, 1, bright);
			rb.renderFaceYPos(x, y, z, bottom, 1, 1, 1, bright);
			rb.renderFaceZNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceZPos(x, y, z, side_disconnected, 1, 1, 1, bright);
			
			rb.setRenderBounds(2 / 16D, 13 / 16D, 2 / 16D, 14 / 16D, 1, 14 / 16D);
			
			rb.renderFaceXNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceXPos(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceYNeg(x, y, z, bottom, 1, 1, 1, bright);
			rb.renderFaceYPos(x, y, z, top, 1, 1, 1, bright);
			rb.renderFaceZNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceZPos(x, y, z, side_disconnected, 1, 1, 1, bright);
			
			rb.setRenderBounds(3 / 16D, 3 / 16D, 3 / 16D, 13 / 16D, 13 / 16D, 13 / 16D);
			
			if(connections != null)
			{
				rb.renderFaceXNeg(x, y, z, connections.apply(EnumFacing.WEST) ? side_connected : side_disconnected, 1, 1, 1, bright);
				rb.renderFaceXPos(x, y, z, connections.apply(EnumFacing.EAST) ? side_connected : side_disconnected, 1, 1, 1, bright);
				rb.renderFaceZNeg(x, y, z, connections.apply(EnumFacing.NORTH) ? side_connected : side_disconnected, 1, 1, 1, bright);
				rb.renderFaceZPos(x, y, z, connections.apply(EnumFacing.SOUTH) ? side_connected : side_disconnected, 1, 1, 1, bright);
			}else
			{
				rb.renderFaceXNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
				rb.renderFaceXPos(x, y, z, side_disconnected, 1, 1, 1, bright);
				rb.renderFaceZNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
				rb.renderFaceZPos(x, y, z, side_connected, 1, 1, 1, bright);
			}
			
			if(connections != null)
				TESRConduit.drawPipeConnection(x, y, z, vis_conduit, 1, 1, 1, bright, 3.1, connections);
			
			tess.draw();
		}
	}
}