package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;
import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.ConnectionManager;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;
import com.pengu.lostthaumaturgy.block.def.BlockTraceableRendered;
import com.pengu.lostthaumaturgy.block.wood.silverwood.BlockSilverwoodConduit;
import com.pengu.lostthaumaturgy.client.render.shared.LiquidVisRenderer;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileConduit;

public class TESRConduit<T extends TileConduit> extends TESR<T> implements Predicate<EnumFacing>
{
	public static final TESRConduit INSTANCE = new TESRConduit();
	
	@Override
	public void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		BlockTraceableRendered br = WorldUtil.cast(te.getWorld().getBlockState(te.getPos()).getBlock(), BlockTraceableRendered.class);
		String sprite = LTInfo.MOD_ID + ":blocks/vis_conduit";
		if(br instanceof BlockSilverwoodConduit)
			sprite = br.getParticleSprite(getWorld(), null);
		
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		renderConduitBase(te, te, sprite, x, y, z);
		renderConduitVis(te, x, y, z);
		rb.renderAlpha = srcAlpha;
	}
	
	@Override
	public boolean canRenderFromNbt()
	{
		return true;
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		ItemBlock ib = WorldUtil.cast(item.getItem(), ItemBlock.class);
		BlockTraceableRendered br = WorldUtil.cast(ib.getBlock(), BlockTraceableRendered.class);
		String sprite = LTInfo.MOD_ID + ":blocks/vis_conduit";
		if(br instanceof BlockSilverwoodConduit)
			sprite = br.getParticleSprite(getWorld(), null);
		
		renderConduitBase(null, this, sprite, 0, 0, 0);
		super.renderItem(item);
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
		
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		
		int bright = getBrightnessForRB(null, rb);
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		float displayPure = nbt.getFloat("PureVis");
		float displayTaint = nbt.getFloat("TaintedVis");
		float maxVis = 4;
		
		float w4 = 0.25f;
		float w6 = 0.375f;
		float wq2 = 0.38025f;
		float w1 = 0.0025f;
		
		float b = 0;
		
		if(displayPure + displayTaint >= .05F)
		{
			b = Math.min(1.0f, displayTaint / (displayTaint + displayPure));
			float total = Math.min(displayPure + displayTaint, maxVis - 0.1F);
			float hfill = (1.0f - wq2 * 2.0f) * (total / maxVis);
			int c = 20 + (int) (b * 210.0f);
			
			for(EnumFacing f : EnumFacing.VALUES)
			{
				if(!apply(f))
					continue;
				
				if(f == EnumFacing.UP)
					rb.setRenderBounds(wq2 + hfill, (6 + wq2 + hfill) / 16D, wq2 + hfill, 1 - wq2 - hfill, 1 - w1 / 16D, 1 - wq2 - hfill);
				if(f == EnumFacing.DOWN)
					rb.setRenderBounds(wq2 + hfill, (w1 + wq2 + hfill) / 16D, wq2 + hfill, 1 - wq2 - hfill, (6 + w1) / 16D, 1 - wq2 - hfill);
				if(f == EnumFacing.EAST)
					rb.setRenderBounds(10 / 16D, (6 + w1) / 16D, (6 + w1) / 16D, 1, wq2 + hfill, (10 - w1) / 16D);
				if(f == EnumFacing.WEST)
					rb.setRenderBounds(0, (6 + w1) / 16D, (6 + w1) / 16D, (6 - w1) / 16D, wq2 + hfill, (10 - w1) / 16D);
				if(f == EnumFacing.SOUTH)
					rb.setRenderBounds((6 + w1) / 16D, (6 + w1) / 16D, (10 - w1) / 16D, (10 - w1) / 16D, wq2 + hfill, 1);
				if(f == EnumFacing.NORTH)
					rb.setRenderBounds((6 + w1) / 16D, (6 + w1) / 16D, 0, (10 - w1) / 16D, wq2 + hfill, (6 - w1) / 16D);
				
				rb.renderFaceXNeg(x, y, z, vis, c, c, c, bright);
				rb.renderFaceXPos(x, y, z, vis, c, c, c, bright);
				rb.renderFaceYNeg(x, y, z, vis, c, c, c, bright);
				rb.renderFaceYPos(x, y, z, vis, c, c, c, bright);
				rb.renderFaceZNeg(x, y, z, vis, c, c, c, bright);
				rb.renderFaceZPos(x, y, z, vis, c, c, c, bright);
			}
			
			rb.setRenderBounds(wq2, wq2, wq2, 1.0f - wq2, wq2 + hfill, 1.0f - wq2);
			
			rb.renderFaceXNeg(x, y, z, vis, c, c, c, bright);
			rb.renderFaceXPos(x, y, z, vis, c, c, c, bright);
			rb.renderFaceYNeg(x, y, z, vis, c, c, c, bright);
			rb.renderFaceYPos(x, y, z, vis, c, c, c, bright);
			rb.renderFaceZNeg(x, y, z, vis, c, c, c, bright);
			rb.renderFaceZPos(x, y, z, vis, c, c, c, bright);
		}
		
		LiquidVisRenderer.finishDrawWithShaders(tess, 1 - b);
		
		blend.reset();
	}
	
	public void renderConduitBase(TileConduit te, Predicate<EnumFacing> conduit, String conduitSprite, double x, double y, double z)
	{
		TextureAtlasSprite destroy = te == null ? null : DestroyStageTexture.getAsSprite(destroyProgress);
		
		TextureAtlasSprite sprites[] = null;
		if(destroy != null)
			sprites = new TextureAtlasSprite[] { destroy, ClientProxy.getSprite(conduitSprite) };
		else
			sprites = new TextureAtlasSprite[] { ClientProxy.getSprite(conduitSprite) };
		
		GLRenderState blend = GLRenderState.BLEND;
		
		for(TextureAtlasSprite sprite : sprites)
		{
			blend.captureState();
			blend.on();
			
			GlStateManager.disableLighting();
			
			Tessellator tess = Tessellator.getInstance();
			
			RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
			
			int bright = te != null ? rb.setLighting(te.getWorld(), te.getPos()) : rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
			
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			
			boolean renderCore = true;
			
			int matches = 0;
			boolean areOpposite = false;
			EnumFacing lastMatch = null;
			
			for(EnumFacing facing : EnumFacing.VALUES)
				if(conduit.apply(facing))
				{
					if(matches == 1)
						areOpposite = lastMatch.getAxis() == facing.getAxis();
					
					lastMatch = facing;
					matches++;
					
					switch(facing)
					{
					case UP:
					{
						rb.setRenderBounds(6 / 16D, 10 / 16D, 6 / 16D, 10 / 16D, 1, 10 / 16D);
						
						rb.renderFromInside = true;
						
						rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
						
						rb.renderFromInside = false;
						
						rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
					}
					break;
					
					case DOWN:
					{
						rb.setRenderBounds(6 / 16D, 0, 6 / 16D, 10 / 16D, 6 / 16D, 10 / 16D);
						
						rb.renderFromInside = true;
						
						rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
						
						rb.renderFromInside = false;
						
						rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
					}
					break;
					
					case EAST:
					{
						rb.setRenderBounds(10 / 16D, 6 / 16D, 6 / 16D, 1, 10 / 16D, 10 / 16D);
						
						rb.renderFromInside = true;
						
						rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
						
						rb.renderFromInside = false;
						
						rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
					}
					break;
					
					case WEST:
					{
						rb.setRenderBounds(0, 6 / 16D, 6 / 16D, 6 / 16D, 10 / 16D, 10 / 16D);
						
						rb.renderFromInside = true;
						
						rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
						
						rb.renderFromInside = false;
						
						rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
					}
					break;
					
					case SOUTH:
					{
						rb.setRenderBounds(6 / 16D, 6 / 16D, 10 / 16D, 10 / 16D, 10 / 16D, 1);
						
						rb.renderFromInside = true;
						
						rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
						
						rb.renderFromInside = false;
						
						rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
					}
					break;
					
					case NORTH:
					{
						rb.setRenderBounds(6 / 16D, 6 / 16D, 0, 10 / 16D, 10 / 16D, 6 / 16D);
						
						rb.renderFromInside = true;
						
						rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
						
						rb.renderFromInside = false;
						
						rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
						rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
					}
					break;
					}
				}
			
			if(matches == 2 && areOpposite)
				renderCore = false;
			
			renderCore = true;
			
			if(renderCore)
			{
				rb.setRenderBounds(6 / 16D, 6 / 16D, 6 / 16D, 10 / 16D, 10 / 16D, 10 / 16D);
				
				rb.renderFromInside = true;
				
				if(!conduit.apply(EnumFacing.WEST))
					rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.EAST))
					rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.DOWN))
					rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.UP))
					rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.NORTH))
					rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.SOUTH))
					rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
				
				rb.renderFromInside = false;
				
				if(!conduit.apply(EnumFacing.WEST))
					rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.EAST))
					rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.DOWN))
					rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.UP))
					rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.NORTH))
					rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
				if(!conduit.apply(EnumFacing.SOUTH))
					rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
			} else
			{
				Axis renderAxis = lastMatch.getAxis();
				
			}
			
			tess.draw();
			
			blend.reset();
		}
	}
	
	public void renderConduitVis(TileConduit tc, double x, double y, double z)
	{
		BlockPos pos = tc.getPos();
		World world = tc.getWorld();
		
		TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
		
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		GlStateManager.disableLighting();
		
		Tessellator tess = Tessellator.getInstance();
		
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		
		int bright = rb.setLighting(tc.getWorld(), tc.getPos());
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		float w4 = 0.25f;
		float w6 = 0.375f;
		float wq2 = 0.38125f;
		float w1 = 0.0625f;
		
		float b = 1;
		if(tc.displayPure + tc.displayTaint >= .05F)
		{
			b = Math.min(1.0f, tc.displayTaint / (tc.displayTaint + tc.displayPure));
			float total = Math.min(tc.displayPure + tc.displayTaint, tc.maxVis - 0.1F);
			float hfill = (1.0f - wq2 * 2.0f) * (total / tc.maxVis);
			int c = 20 + (int) (b * 210.0f);
			
			for(EnumFacing f : EnumFacing.VALUES)
			{
				IConnection ic = ConnectionManager.getConnection(world, pos, f);
				if(ic == null || !ic.getConnectable(f.getOpposite()))
					continue;
				
				TileVisUser user = WorldUtil.cast(ic, TileVisUser.class);
				TileConduit cond = WorldUtil.cast(ic, TileConduit.class);
				
				if(cond != null && cond.displayPure + cond.displayTaint < .05F)
					continue;
				
				if(user != null && user.getSuction(user.getPos()) < tc.getSuction(null))
					continue;
				
				if(cond == null || Math.abs(tc.getSuction(null) - ic.getSuction(pos)) == 1 || (user != null && (user.getSuction(user.getPos()) >= tc.getSuction(null) || user.isVisSource())))
				{
					if(f == EnumFacing.UP)
						rb.setRenderBounds(wq2 + hfill, (6 + wq2 + hfill) / 16D, wq2 + hfill, 1 - wq2 - hfill, 1 - w1 / 16D, 1 - wq2 - hfill);
					if(f == EnumFacing.DOWN)
						rb.setRenderBounds(wq2 + hfill, (w1 + wq2 + hfill) / 16D, wq2 + hfill, 1 - wq2 - hfill, (6 + w1) / 16D, 1 - wq2 - hfill);
					if(f == EnumFacing.EAST)
						rb.setRenderBounds(10 / 16D, (6 + w1) / 16D, (6 + w1) / 16D, 1, wq2 + hfill, (10 - w1) / 16D);
					if(f == EnumFacing.WEST)
						rb.setRenderBounds(0, (6 + w1) / 16D, (6 + w1) / 16D, (6 - w1) / 16D, wq2 + hfill, (10 - w1) / 16D);
					if(f == EnumFacing.SOUTH)
						rb.setRenderBounds((6 + w1) / 16D, (6 + w1) / 16D, (10 - w1) / 16D, (10 - w1) / 16D, wq2 + hfill, 1);
					if(f == EnumFacing.NORTH)
						rb.setRenderBounds((6 + w1) / 16D, (6 + w1) / 16D, 0, (10 - w1) / 16D, wq2 + hfill, (6 - w1) / 16D);
					
					rb.renderFaceXNeg(x, y, z, vis, c, c, c, bright);
					rb.renderFaceXPos(x, y, z, vis, c, c, c, bright);
					rb.renderFaceYNeg(x, y, z, vis, c, c, c, bright);
					rb.renderFaceYPos(x, y, z, vis, c, c, c, bright);
					rb.renderFaceZNeg(x, y, z, vis, c, c, c, bright);
					rb.renderFaceZPos(x, y, z, vis, c, c, c, bright);
				}
			}
			
			rb.setRenderBounds(wq2, wq2, wq2, 1.0f - wq2, wq2 + hfill, 1.0f - wq2);
			
			rb.renderFaceXNeg(x, y, z, vis, c, c, c, bright);
			rb.renderFaceXPos(x, y, z, vis, c, c, c, bright);
			rb.renderFaceYNeg(x, y, z, vis, c, c, c, bright);
			rb.renderFaceYPos(x, y, z, vis, c, c, c, bright);
			rb.renderFaceZNeg(x, y, z, vis, c, c, c, bright);
			rb.renderFaceZPos(x, y, z, vis, c, c, c, bright);
		}
		
		LiquidVisRenderer.finishDrawWithShaders(tess, 1 - b);
		
		blend.reset();
	}
	
	@Override
	public boolean apply(EnumFacing input)
	{
		return input.getAxis() == Axis.Y;
	}
	
	public static void drawPipeConnection(double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue, int bright, double length, Predicate<EnumFacing> facings)
	{
		float w4 = 0.25f;
		float w6 = 0.375f;
		float wq2 = 0.38125f;
		
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		
		boolean wasRenderingFromInside = rb.renderFromInside;
		
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(!facings.apply(f))
				continue;
			
			rb.renderFromInside = false;
			
			double off = length;
			
			if(f == EnumFacing.DOWN)
				rb.setRenderBounds(6 / 16D, 0, 6 / 16D, 10 / 16D, (6 - off) / 16D, 10 / 16D);
			if(f == EnumFacing.UP)
				rb.setRenderBounds(6 / 16D, (10 + off) / 16, 6 / 16D, 10 / 16D, 1, 10 / 16D);
			if(f == EnumFacing.EAST)
				rb.setRenderBounds((10 + off) / 16, 6 / 16D, 6 / 16D, 1, 10 / 16D, 10 / 16D);
			if(f == EnumFacing.WEST)
				rb.setRenderBounds(0, 6 / 16D, 6 / 16D, (6 - off) / 16D, 10 / 16D, 10 / 16D);
			if(f == EnumFacing.SOUTH)
				rb.setRenderBounds(6 / 16D, 6 / 16D, (10 + off) / 16D, 10 / 16D, 10 / 16D, 1);
			if(f == EnumFacing.NORTH)
				rb.setRenderBounds(6 / 16D, 6 / 16D, 0, 10 / 16D, 10 / 16D, (6 - off) / 16D);
			
			rb.renderFromInside = true;
			
			if(f.getAxis() != Axis.X)
			{
				rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
			}
			
			rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
			rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
			
			if(f.getAxis() != Axis.Z)
			{
				rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
			}
			
			rb.renderFromInside = false;
			
			if(f.getAxis() != Axis.X)
			{
				rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
			}
			
			rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
			rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
			
			if(f.getAxis() != Axis.Z)
			{
				rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
			}
		}
		
		rb.renderFromInside = wasRenderingFromInside;
	}
}