package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.render.shared.LiquidVisRenderer;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileFuser;

public class TESRFuser extends TESR<TileFuser>
{
	public static final TESRFuser INSTANCE = new TESRFuser();
	
	@Override
	public void renderTileEntityAt(TileFuser te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		TileFuser main = te.gui;
		if(main == null)
			return;
		
		Vec3i vec = te.getPos().subtract(main.getPos());
		
		SimpleBlockRendering sbr = rb.simpleRenderer;
		
		rb.renderFromInside = false;
		
		int index = vec.getX() + vec.getZ() * 2 + 1;
		EnumFacing face = EnumFacing.getFacingFromVector(vec.getX(), 0, vec.getZ());
		
		if(vec.getX() == 1 && vec.getZ() == 0)
			face = EnumFacing.NORTH;
		
		if(vec.getX() == 0 && vec.getZ() == 1)
			face = EnumFacing.SOUTH;
		
		if(vec.getX() == 0 && vec.getZ() == 0)
			face = EnumFacing.WEST;
		
		if(vec.getX() == 1 && vec.getZ() == 1)
			face = EnumFacing.EAST;
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		Color.glColourRGB(0xFFFFFF);
		Tessellator tess = Tessellator.getInstance();
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		rb.setRenderBounds(.02, .02, .02, .98, .98, .98);
		int b = getBrightnessForRB(te, sbr.rb);
		TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
		rb.renderFaceXPos(x, y, z, vis, 1F, 1F, 1F, b);
		rb.renderFaceXNeg(x, y, z, vis, 1F, 1F, 1F, b);
		rb.renderFaceZPos(x, y, z, vis, 1F, 1F, 1F, b);
		rb.renderFaceZNeg(x, y, z, vis, 1F, 1F, 1F, b);
		rb.renderFaceYPos(x, y, z, vis, 1F, 1F, 1F, b);
		LiquidVisRenderer.alpha = 1;
		LiquidVisRenderer.resolution = 300;
		LiquidVisRenderer.finishDrawWithShaders(tess, 1);
		
		sbr.begin();
		for(int i = 0; i < (destroyProgress > 0F ? 2 : 1); ++i)
		{
			TextureAtlasSprite d = DestroyStageTexture.getAsSprite(destroyProgress);
			
			TextureAtlasSprite //
			s1 = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser/side1"), //
			s2 = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser/side2"), //
			top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser/top" + index), //
			down = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser/down" + index);
			
			if(i == 1)
				s1 = s2 = top = down = d;
			
			sbr.setBrightness(getBrightnessForRB(te, sbr.rb));
			sbr.disableFaces();
			sbr.enableFace(EnumFacing.UP);
			sbr.enableFace(EnumFacing.DOWN);
			sbr.enableFace(face);
			sbr.enableFace(face.rotateY());
			
			sbr.setSpriteForSide(face, s1);
			sbr.setSpriteForSide(face.rotateY(), s2);
			sbr.setSpriteForSide(EnumFacing.UP, top);
			sbr.setSpriteForSide(EnumFacing.DOWN, down);
			
			sbr.drawBlock(x, y, z);
		}
		sbr.end();
		
		if(main == te)
		{
			GL11.glPushMatrix();
			
			double sin = Math.sin(te.ticksExisted / 8D);
			double cos = Math.cos(te.ticksExisted / 8D);
			
			ItemStack wand = te.inventory.getStackInSlot(9);
			GL11.glTranslated(x, y, z);
			GL11.glTranslated(1.2, 1.2 + cos * .03, 1);
			GL11.glRotated(45 + sin * 5 - 2.5, 0, 0, 1);
			GL11.glScaled(1.4, 1.4, 1.4);
			mc.getRenderItem().renderItem(wand, TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + .535, y + 1, z + .635);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(0), TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1, y + 1, z + .635);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(1), TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1.465, y + 1, z + .635);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(2), TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + .535, y + 1, z + 1.105);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(3), TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1, y + 1, z + 1.105);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(4), TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1.465, y + 1, z + 1.105);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(5), TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + .535, y + 1, z + 1.555);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(6), TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1, y + 1, z + 1.555);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(7), TransformType.GROUND);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1.465, y + 1, z + 1.555);
			GL11.glScaled(.9, .9, .9);
			GL11.glRotatef(-90, 1, 0, 0);
			mc.getRenderItem().renderItem(te.inventory.getStackInSlot(8), TransformType.GROUND);
			GL11.glPopMatrix();
		}
		
		rb.renderAlpha = srcAlpha;
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		GL11.glPushMatrix();
		GL11.glScaled(.5, .5, .5);
		for(int x = 0; x < 2; ++x)
			for(int z = 0; z < 2; ++z)
			{
				SimpleBlockRendering sbr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
				
				sbr.rb.renderFromInside = false;
				
				int index = x + z * 2 + 1;
				EnumFacing face = EnumFacing.getFacingFromVector(x, 0, z);
				
				if(x == 1 && z == 0)
					face = EnumFacing.NORTH;
				
				if(x == 0 && z == 1)
					face = EnumFacing.SOUTH;
				
				if(x == 0 && z == 0)
					face = EnumFacing.WEST;
				
				if(x == 1 && z == 1)
					face = EnumFacing.EAST;
				
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				
				Color.glColourRGB(0xFFFFFF);
				Tessellator tess = Tessellator.getInstance();
				tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
				sbr.rb.setRenderBounds(.02, .02, .02, .98, .98, .98);
				int b = getBrightnessForRB(null, sbr.rb);
				TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
				sbr.rb.renderFaceXPos(x, 0, z, vis, 1F, 1F, 1F, b);
				sbr.rb.renderFaceXNeg(x, 0, z, vis, 1F, 1F, 1F, b);
				sbr.rb.renderFaceZPos(x, 0, z, vis, 1F, 1F, 1F, b);
				sbr.rb.renderFaceZNeg(x, 0, z, vis, 1F, 1F, 1F, b);
				sbr.rb.renderFaceYPos(x, 0, z, vis, 1F, 1F, 1F, b);
				LiquidVisRenderer.alpha = 1;
				LiquidVisRenderer.resolution = 300;
				LiquidVisRenderer.finishDrawWithShaders(tess, 1);
				
				sbr.begin();
				for(int i = 0; i < (destroyProgress > 0F ? 2 : 1); ++i)
				{
					TextureAtlasSprite d = DestroyStageTexture.getAsSprite(destroyProgress);
					
					TextureAtlasSprite //
					s1 = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser/side1"), //
					s2 = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser/side2"), //
					top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser/top" + index), //
					down = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser/down" + index);
					
					if(i == 1)
						s1 = s2 = top = down = d;
					
					sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
					sbr.disableFaces();
					sbr.enableFace(EnumFacing.UP);
					sbr.enableFace(EnumFacing.DOWN);
					sbr.enableFace(face);
					sbr.enableFace(face.rotateY());
					
					sbr.setSpriteForSide(face, s1);
					sbr.setSpriteForSide(face.rotateY(), s2);
					sbr.setSpriteForSide(EnumFacing.UP, top);
					sbr.setSpriteForSide(EnumFacing.DOWN, down);
					
					sbr.drawBlock(x, 0, z);
				}
				sbr.end();
			}
		GL11.glPopMatrix();
	}
}