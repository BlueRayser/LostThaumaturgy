package com.pengu.lostthaumaturgy.client.render.tesr.monolith;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.client.model.ModelCrystal;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.items.ItemGogglesRevealing;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.monolith.TileCrystalReceptacle;

public class TESRCrystalReceptacle extends TESR<TileCrystalReceptacle>
{
	public static final TESRCrystalReceptacle INSTANCE = new TESRCrystalReceptacle();
	final ModelCrystal crystal = new ModelCrystal();
	final ResourceLocation texture1 = new ResourceLocation(LTInfo.MOD_ID, "textures/models/crystal.png");
	final Map<String, ResourceLocation> textures = new HashMap<>();
	
	{
		textures.put("aqueus", new ResourceLocation(LTInfo.MOD_ID, "textures/misc/runes/aqueous.png"));
		textures.put("earthen", new ResourceLocation(LTInfo.MOD_ID, "textures/misc/runes/earthen.png"));
		textures.put("fiery", new ResourceLocation(LTInfo.MOD_ID, "textures/misc/runes/fiery.png"));
		textures.put("tainted", new ResourceLocation(LTInfo.MOD_ID, "textures/misc/runes/tainted.png"));
		textures.put("vaporous", new ResourceLocation(LTInfo.MOD_ID, "textures/misc/runes/vaporous.png"));
		textures.put("vis", new ResourceLocation(LTInfo.MOD_ID, "textures/misc/runes/vis.png"));
	}
	
	@Override
	public void renderTileEntityAt(TileCrystalReceptacle te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		te.rand.setSeed(te.getPos().toLong() + te.getWorld().getSeed() + te.EXPECTED_CRYSTAL.get());
		
		boolean goggles = ItemGogglesRevealing.getWearing(mc.player) != null;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .01, y + .01, z + .01);
		GL11.glScaled(.98, .98, .98);
		HammerCore.renderProxy.getRenderHelper().renderEndPortalEffect(0, 0, 0, EnumFacing.UP);
		GL11.glPopMatrix();
		
		BlockOreCrystal ore = te.getOre();
		if(te.INSERTED.get())
		{
			GL11.glPushMatrix();
			Color.glColourRGB(ore != null ? ore.getCrystalColor() : 0xFFFFFF);
			GL11.glTranslated(x + .5, y + .8, z + .5);
			GL11.glScaled(.5, .5, .5);
			GL11.glScalef((.15F + .05F), (.5F + .04F), (0.15f + .02F));
			Minecraft.getMinecraft().getTextureManager().bindTexture(texture1);
			crystal.render(null, 0, 0, -.1F, 0, 0, .0625F);
			GL11.glPopMatrix();
			Color.glColourRGB(0xFFFFFF);
		}
		
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(te, sbr.rb));
		
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(!BlocksLT.MONOLITH_OPENER.shouldSideBeRendered(te.getWorld().getBlockState(te.getPos()), te.getWorld(), te.getPos(), f))
				sbr.disableFace(f);
			
			if(f == EnumFacing.UP)
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/crystal_receptacle"));
			else
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/eldritch_block/" + te.rand.nextInt(6)));
		}
		
		sbr.drawBlock(x, y, z);
		
		sbr.end();
		
		if(goggles && ore != null && !te.INSERTED.get())
		{
			GL11.glPushMatrix();
			GL11.glTranslated(x + .25, y + 1.09 - Math.sin(te.rand.nextInt(100000) / 2.666667D + te.getWorld().getTotalWorldTime() / 16D) * .05, z + .25);
			GL11.glScaled(.5, .5, .5);
			GL11.glTranslated(.5, .5, .5);
			GL11.glRotated((te.rand.nextDouble() * 360 + te.getWorld().getTotalWorldTime() % 360L) % 360D + partialTicks, 0, 1, 0);
			GL11.glTranslated(-.5, -.5, -.5);
			GL11.glRotated(90, 1, 0, 0);
			GL11.glScaled(1 / 256D, 1 / 256D, 1 / 256D);
			bindTexture(textures.get(ore.cName));
			RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			GL11.glPopMatrix();
		}
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(f == EnumFacing.UP)
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/crystal_receptacle"));
			else
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/eldritch_block/" + f.ordinal()));
		}
		sbr.drawBlock(0, 0, 0);
		sbr.end();
	}
}