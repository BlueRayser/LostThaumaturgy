package com.pengu.lostthaumaturgy.client.render.seal;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.client.TextureAtlasSpriteFull;
import com.pengu.lostthaumaturgy.tile.TileSeal;

@SideOnly(Side.CLIENT)
public class LTSealRenders
{
	public static void renderStandart(TileSeal seal, double x, double y, double z, float partialTicks, int index)
	{
		ItemSealSymbol symb = seal.getSymbol(index);
		int color = symb.getColorMultiplier(seal, index);
		boolean rotates = symb.doesRotate(seal, index);
		ResourceLocation tex = symb.getTexture(seal, index);
		GlStateManager.enableTexture2D();
		SimpleBlockRendering sbr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
		sbr.rb.renderFromInside = false;
		sbr.rb.renderAlpha = 1;
		sbr.begin();
		sbr.setRenderBounds(0, 0, 0, 1, 1, 1);
		sbr.setBrightness(sbr.rb.setLighting(seal.getWorld(), seal.getPos()));
		Arrays.fill(sbr.rgb, color);
		sbr.disableFaces();
		sbr.enableFace(EnumFacing.NORTH);
		sbr.setSprite(TextureAtlasSpriteFull.sprite);
		sbr.drawBlock(0, 0, 0);
		
		int mult = index == 2 ? -1 : 1;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x - (8 / 16D), y - (6 / 16D), z - (.01 * (index + 1)));
		GL11.glTranslated(.5, .5, .5);
		GL11.glRotated(index * 120 * mult, 0, 0, 1);
		if(rotates)
			GL11.glRotated(((seal.ticksExisted + partialTicks) % 360D) * mult, 0, 0, 1);
		GL11.glTranslated(-.5, -.5, -.5);
		Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
		Tessellator.getInstance().draw();
		GL11.glPopMatrix();
	}
	
	public static void renderNone(TileSeal seal, double x, double y, double z, float partialTicks)
	{
		
	}
}