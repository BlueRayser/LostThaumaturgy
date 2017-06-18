package com.pengu.lostthaumaturgy.client.render.tesr;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.OpnodeLoader;
import com.pengu.hammercore.client.model.simple.OpnodeRender;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.model.ModelCrystal;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileVisCondenser;

public class TESRVisCondenser extends TESR<TileVisCondenser>
{
	public static final TESRVisCondenser INSTANCE = new TESRVisCondenser();
	
	private ModelCrystal model = new ModelCrystal();
	private float bob = 0;
	public final ResourceLocation portal2 = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/portal2.png");
	public final ResourceLocation crystal = new ResourceLocation(LTInfo.MOD_ID, "textures/models/crystal.png");
	
	private void drawDisk(double x, double y, double z, float angle)
	{
		Tessellator tessellator = Tessellator.getInstance();
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y, z + .5);
		GL11.glPushMatrix();
		GL11.glRotatef(angle, 0, 1, 0);
		GL11.glTranslatef(-.3F, 0, -.3F);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 1);
		bindTexture(portal2);
		GL11.glColor4d(1, 1, 1, 1);
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		VertexBuffer vb = tessellator.getBuffer();
		// tessellator.a(1.0, 0.5, 1.0, 1.0);
		vb.pos(0, 0, .6).tex(0.0, 1.0).endVertex();
		vb.pos(0.6, 0.0, 0.6).tex(1.0, 1.0).endVertex();
		vb.pos(0.6, 0.0, 0.0).tex(1.0, 0.0).endVertex();
		vb.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).endVertex();
		vb.pos(0.0, 0.0, 0.0).tex(0.0, 1.0).endVertex();
		vb.pos(0.6, 0.0, 0.0).tex(1.0, 1.0).endVertex();
		vb.pos(0.6, 0.0, 0.6).tex(1.0, 0.0).endVertex();
		vb.pos(0.0, 0.0, 0.6).tex(0.0, 0.0).endVertex();
		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glBlendFunc(770, 771);
	}
	
	@Override
	public void renderBase(TileVisCondenser tile, ItemStack stack, double x, double y, double z, ResourceLocation destroyStage)
	{
		List<int[]> opnode = OpnodeLoader.loadOpnodes(LTInfo.MOD_ID, "tile/condenser");
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		OpnodeRender.renderOpnodes(rb.simpleRenderer, opnode, getBrightnessForRB(tile, rb), true);
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileVisCondenser te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		this.bob = MathHelper.sin(mc.player.ticksExisted / 10F) * .05F + .05F;
		
		if(te.hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)))
		{
			RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
			SimpleBlockRendering sbr = rb.simpleRenderer;
			
			sbr.begin();
			sbr.setRenderBounds(4 / 16D, 4 / 16D, 4 / 16D, 12 / 16D, 12 / 16D, 12 / 16D);
			sbr.setSprite(ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/quicksilver_core"));
			sbr.drawBlock(x, y, z);
			sbr.end();
		}
		
		if(te.degredation > 0)
		{
			float tbob = this.bob;
			if(te.hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)))
				tbob = 0;
			boolean darken = false;
			bindTexture(crystal);
			
			float r = 1, g = 1, b = 1;
			
			if(te.currentType != -1)
			{
				switch(te.currentType)
				{
				case 1:
				{
					int color = 0xFFEE00;
					
					r = ((color >> 16) & 0xFF) / 255F;
					g = ((color >> 8) & 0xFF) / 255F;
					b = ((color >> 0) & 0xFF) / 255F;
					
					break;
				}
				case 2:
				{
					int color = 0x361CFF;
					
					r = ((color >> 16) & 0xFF) / 255F;
					g = ((color >> 8) & 0xFF) / 255F;
					b = ((color >> 0) & 0xFF) / 255F;
					
					break;
				}
				case 3:
				{
					int color = 0x1CFF1C;
					
					r = ((color >> 16) & 0xFF) / 255F;
					g = ((color >> 8) & 0xFF) / 255F;
					b = ((color >> 0) & 0xFF) / 255F;
					
					break;
				}
				case 4:
				{
					int color = 0xFF1C1C;
					
					r = ((color >> 16) & 0xFF) / 255F;
					g = ((color >> 8) & 0xFF) / 255F;
					b = ((color >> 0) & 0xFF) / 255F;
					
					break;
				}
				case 5:
				{
					int color = 0x450B6C;
					darken = true;
					
					r = ((color >> 16) & 0xFF) / 255F;
					g = ((color >> 8) & 0xFF) / 255F;
					b = ((color >> 0) & 0xFF) / 255F;
					
					break;
				}
				default:
				{
					int color = 0xAE00FF;
					
					r = ((color >> 16) & 0xFF) / 255F;
					g = ((color >> 8) & 0xFF) / 255F;
					b = ((color >> 0) & 0xFF) / 255F;
					
					break;
				}
				}
			} else
			{
				int color = 0xAE00FF;
				
				r = ((color >> 16) & 0xFF) / 255F;
				g = ((color >> 8) & 0xFF) / 255F;
				b = ((color >> 0) & 0xFF) / 255F;
			}
			GL11.glEnable(2977);
			GL11.glEnable(3042);
			GL11.glPushMatrix();
			GL11.glEnable(32826);
			GL11.glBlendFunc(770, 771);
			GL11.glColor4f(r * te.degredation / (darken ? 6000 : 3500), g * te.degredation / (darken ? 6000 : 3500), b * te.degredation / (darken ? 6000 : 3500), 1);
			GL11.glTranslated(x + .5, y + tbob + .95, z + .5);
			GL11.glRotatef(te.angle, 0, 1, 0);
			GL11.glPushMatrix();
			GL11.glScaled(.15, .45, .15);
			model.render();
			GL11.glScalef(1, 1, 1);
			GL11.glPopMatrix();
			GL11.glDisable(32826);
			GL11.glPopMatrix();
			GL11.glDisable(3042);
			GL11.glColor4f(1, 1, 1, 1);
			if(te.hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)))
				drawDisk(x, y + 1.1749999523162842 + bob * 6, z, 360 - te.angle);
		}
	}
}