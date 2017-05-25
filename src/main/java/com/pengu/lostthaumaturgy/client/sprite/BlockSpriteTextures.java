package com.pengu.lostthaumaturgy.client.sprite;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.mrdimka.hammercore.vec.Cuboid6;
import com.mrdimka.hammercore.vec.Vector3;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.sprite.SpriteTexture.BlockSpriteTexture;

public class BlockSpriteTextures
{
	public static BlockSpriteTexture[] textures = new BlockSpriteTexture[6];
	public static double[] bounds = new double[6];
	public static boolean[] faces = new boolean[6];
	public static int[] rgb = new int[6];
	public static int[] bright = new int[6];
	
	public static void setColor(EnumFacing side, int rgb)
	{
		BlockSpriteTextures.rgb[side.ordinal()] = rgb;
	}
	
	public static void setBrightness(int bright)
	{
		Arrays.fill(BlockSpriteTextures.bright, bright);
	}
	
	public static void setBrightness(EnumFacing side, int bright)
	{
		BlockSpriteTextures.bright[side.ordinal()] = bright;
	}
	
	public static void disableFace(EnumFacing side)
	{
		faces[side.ordinal()] = false;
	}
	
	public static void enableFace(EnumFacing side)
	{
		faces[side.ordinal()] = true;
	}
	
	public static void enableFaces()
	{
		Arrays.fill(faces, true);
	}
	
	public static void disableFaces()
	{
		Arrays.fill(faces, false);
	}
	
	public static void setSidedSprites(TextureAtlasSprite bottom, TextureAtlasSprite top, TextureAtlasSprite side)
	{
		setSpriteForSide(EnumFacing.DOWN, bottom);
		setSpriteForSide(EnumFacing.UP, top);
		setSpriteForSide(EnumFacing.EAST, side);
		setSpriteForSide(EnumFacing.WEST, side);
		setSpriteForSide(EnumFacing.SOUTH, side);
		setSpriteForSide(EnumFacing.NORTH, side);
	}
	
	public static void setSprite(TextureAtlasSprite sprite)
	{
		for(EnumFacing f : EnumFacing.VALUES)
			setSpriteForSide(f, sprite);
	}
	
	public static void setSpriteForSide(EnumFacing side, TextureAtlasSprite sprite)
	{
		BlockSpriteTexture t = textures[side.ordinal()];
		if(t == null)
			textures[side.ordinal()] = t = new BlockSpriteTexture();
		t.setSprite(sprite);
	}
	
	private static TextureAtlasSprite getSpriteForSide(EnumFacing side)
	{
		BlockSpriteTexture t = textures[side.ordinal()];
		if(t == null)
			textures[side.ordinal()] = t = new BlockSpriteTexture();
		return t.getSprite();
	}
	
	public static void setRenderBounds(Cuboid6 cube)
	{
		setRenderBounds(cube.min, cube.max);
	}
	
	public static void setRenderBounds(Vector3 min, Vector3 max)
	{
		setRenderBounds(min.x, min.y, min.z, max.x, max.y, max.z);
	}
	
	public static void setRenderBounds(Vec3d min, Vec3d max)
	{
		setRenderBounds(min.xCoord, min.yCoord, min.zCoord, max.xCoord, max.yCoord, max.zCoord);
	}
	
	public static void setRenderBounds(AxisAlignedBB aabb)
	{
		setRenderBounds(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
	}
	
	public static void setRenderBounds(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		setRenderBounds(state.getBlock().getBoundingBox(state, world, pos));
	}
	
	public static void setRenderBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		bounds[0] = minX;
		bounds[1] = minY;
		bounds[2] = minZ;
		bounds[3] = maxX;
		bounds[4] = maxY;
		bounds[5] = maxZ;
	}
	
	public static void begin()
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();
		
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		enableFaces();
		
		Arrays.fill(rgb, 0xFFFFFF);
		Arrays.fill(bright, 255);
		
		setRenderBounds(Block.FULL_BLOCK_AABB);
		Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		RenderBlocks.forMod(LTInfo.MOD_ID).renderFromInside = false;
	}
	
	public static void drawBlock(double x, double y, double z)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		
		rb.setRenderBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
		
		for(EnumFacing f : EnumFacing.VALUES)
			if(faces[f.ordinal()])
			{
				int rgb = BlockSpriteTextures.rgb[f.ordinal()];
				int bright = BlockSpriteTextures.bright[f.ordinal()];
				
				float r = (rgb >> 16 & 0xFF) / 255F;
				float g = (rgb >> 8 & 0xFF) / 255F;
				float b = (rgb >> 0 & 0xFF) / 255F;
				
				if(f == EnumFacing.DOWN)
					rb.renderFaceYNeg(x, y, z, getSpriteForSide(f), r, g, b, bright);
				if(f == EnumFacing.UP)
					rb.renderFaceYPos(x, y, z, getSpriteForSide(f), r, g, b, bright);
				if(f == EnumFacing.NORTH)
					rb.renderFaceZNeg(x, y, z, getSpriteForSide(f), r, g, b, bright);
				if(f == EnumFacing.SOUTH)
					rb.renderFaceZPos(x, y, z, getSpriteForSide(f), r, g, b, bright);
				if(f == EnumFacing.WEST)
					rb.renderFaceXNeg(x, y, z, getSpriteForSide(f), r, g, b, bright);
				if(f == EnumFacing.EAST)
					rb.renderFaceXPos(x, y, z, getSpriteForSide(f), r, g, b, bright);
			}
		
		enableFaces();
	}
	
	public static void end()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator.getInstance().draw();
		
		GLRenderState.BLEND.reset();
	}
}