package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.BlockCrystallizer;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.client.model.ModelCrystal;
import com.pengu.lostthaumaturgy.client.sprite.BlockSpriteTextures;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileCrystallizer;

public class TESRCrystallizer extends TESR<TileCrystallizer>
{
	public static final TESRCrystallizer INSTANCE = new TESRCrystallizer();
	private ModelCrystal model = new ModelCrystal();
	private final ResourceLocation crystal = new ResourceLocation(LTInfo.MOD_ID, "textures/models/crystal.png");
	
	private void drawCrystal(double x, double y, double z, float a1, float a2, float b)
	{
		GLRenderState.NORMALIZE.on();
		GLRenderState.BLEND.on();
		GL11.glPushMatrix();
		GL11.glEnable(32826);
		GL11.glBlendFunc(770, 771);
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(a1, 0, 1, 0);
		GL11.glRotatef(a2, 1, 0, 0);
		GL11.glPushMatrix();
		GL11.glScalef(.15F, .45F, .15F);
		model.render();
		GL11.glScalef(1, 1, 1);
		GL11.glPopMatrix();
		GL11.glDisable(32826);
		GL11.glPopMatrix();
		GLRenderState.BLEND.off();
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		drawBlock(0, 0, 0, getBrightnessForRB(null, RenderBlocks.forMod(LTInfo.MOD_ID)));
		super.renderItem(item);
	}
	
	@Override
	public void renderTileEntityAt(TileCrystallizer te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		drawBlock(x, y, z, getBrightnessForRB(te, RenderBlocks.forMod(LTInfo.MOD_ID)));
		
		float slowdown = 4;
		float count = (float) (Minecraft.getMinecraft().world.getTotalWorldTime() % ((long)(360L * slowdown)) / slowdown);
		float bob = 0;
		float angleS = 45;
		float angleI = 90;
		
		if(te.hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL)))
		{
			angleS = 36.0f;
			angleI = 72.0f;
		}
		
		if(te.isCooking())
		{
			angleS += (count % 360);
			bob = count / 5F * .15F + .15F;
		}
		
		bindTexture(crystal);
		
		Color.glColourRGB(((BlockOreCrystal) BlocksLT.CRYSTAL_ORE_VIS).getCrystalColor());
		drawCrystal(x + .5, y + .25, z + .5, angleS, 0, 1 - bob);
		
		Color.glColourRGB(((BlockOreCrystal) BlocksLT.CRYSTAL_ORE_VAPOROUS).getCrystalColor());
		drawCrystal(x + .5, y + .25, z + .5, angleS, 25, 1 - bob);
		
		Color.glColourRGB(((BlockOreCrystal) BlocksLT.CRYSTAL_ORE_AQUEOUS).getCrystalColor());
		drawCrystal(x + .5, y + .25, z + .5, angleS += angleI, 25, 1 - bob);
		
		Color.glColourRGB(((BlockOreCrystal) BlocksLT.CRYSTAL_ORE_EARTHEN).getCrystalColor());
		drawCrystal(x + .5, y + .25, z + .5, angleS += angleI, 25, 1 - bob);
		
		Color.glColourRGB(((BlockOreCrystal) BlocksLT.CRYSTAL_ORE_FIERY).getCrystalColor());
		drawCrystal(x + .5, y + .25, z + .5, angleS += angleI, 25, 1 - bob);
		
		if(te.hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL)))
		{
			Color.glColourRGB(((BlockOreCrystal) BlocksLT.CRYSTAL_ORE_TAINTED).getCrystalColor());
			drawCrystal(x + .5, y + .25, z + .5, angleS += angleI, 25, .4F - bob);
		}
	}
	
	public void drawBlock(double x, double y, double z, int bright)
	{
		BlockSpriteTextures.begin();
		BlockSpriteTextures.setSidedSprites(ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crystallizer/bottom"), ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crystallizer/top"), ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crystallizer/side"));
		BlockSpriteTextures.setBrightness(bright);
		BlockSpriteTextures.setRenderBounds(BlockCrystallizer.CRYSTALLIZER_AABB);
		BlockSpriteTextures.drawBlock(x, y, z);
		BlockSpriteTextures.setSprite(ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crystallizer/inner"));
		BlockSpriteTextures.enableFaces();
		BlockSpriteTextures.disableFace(EnumFacing.UP);
		BlockSpriteTextures.bounds[1] = .1 / 16;
		RenderBlocks.forMod(LTInfo.MOD_ID).renderFromInside = true;
		BlockSpriteTextures.drawBlock(x, y, z);
		BlockSpriteTextures.end();
	}
}