package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.BlockCrystallizer;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.client.model.ModelCrystal;
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
		double x = 0, y = 0, z = 0;
		
		drawBlock(x, y, z, getBrightnessForRB(null, RenderBlocks.forMod(LTInfo.MOD_ID)));
		
		float count = item.hashCode() / 360F;
		float bob = 0;
		float angleS = 45;
		float angleI = 90;
		
		angleS += (count % 360);
		bob = count / 5F * .15F + .15F;
		
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
		
		super.renderItem(item);
	}
	
	@Override
	public void renderTileEntityAt(TileCrystallizer te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		drawBlock(x, y, z, getBrightnessForRB(te, RenderBlocks.forMod(LTInfo.MOD_ID)));
		
		float count = (te.crystalTime.get() / te.maxTime) * 360F;
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
		SimpleBlockRendering sr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
		
		sr.begin();
		sr.setSidedSprites(ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crystallizer/bottom"), ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crystallizer/top"), ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crystallizer/side"));
		sr.setBrightness(bright);
		sr.setRenderBounds(BlockCrystallizer.CRYSTALLIZER_AABB);
		sr.rb.renderFromInside = false;
		sr.drawBlock(x, y, z);
		sr.setRenderBounds(BlockCrystallizer.CRYSTALLIZER_AABB.shrink(.01));
		sr.setSprite(ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/crystallizer/inner"));
		sr.enableFaces();
		sr.disableFace(EnumFacing.UP);
		sr.bounds[1] = .1 / 16;
		sr.rb.renderFromInside = true;
		sr.drawBlock(x, y, z);
		sr.end();
	}
}