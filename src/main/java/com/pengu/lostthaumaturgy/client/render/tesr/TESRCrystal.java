package com.pengu.lostthaumaturgy.client.render.tesr;

import java.util.Random;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.client.model.ModelCrystal;
import com.pengu.lostthaumaturgy.tile.TileCrystalOre;

public class TESRCrystal extends TESR<TileCrystalOre>
{
	public static final TESRCrystal INSTANCE = new TESRCrystal();
	private ModelCrystal model = new ModelCrystal();
	private final Random rand = new Random();
	private final ResourceLocation crystal = new ResourceLocation(LTInfo.MOD_ID, "textures/models/crystal.png");
	
	@Override
	public void renderItem(ItemStack item)
	{
		ItemBlock ib = WorldUtil.cast(item.getItem(), ItemBlock.class);
		if(ib == null)
			return;
		BlockOreCrystal block = WorldUtil.cast(ib.block, BlockOreCrystal.class);
		if(block == null)
			return;
		
		float red = ((block.getCrystalColor() >> 16) & 0xFF) / 255F;
		float green = ((block.getCrystalColor() >> 8) & 0xFF) / 255F;
		float blue = ((block.getCrystalColor() >> 0) & 0xFF) / 255F;
		
		rand.setSeed(item.hashCode());
		
		mc.getTextureManager().bindTexture(crystal);
		
		GL11.glPushMatrix();
		GL11.glTranslated(-.25, .05, -.25);
		GL11.glScaled(1.5, 1.5, 1.5);
		drawCrystal(1, 0, 0, 0, (rand.nextFloat() - rand.nextFloat()) * 5.0f, (rand.nextFloat() - rand.nextFloat()) * 5F, 1, rand, red, green, blue);
		for(int a = 1; a < Math.min(item.getCount(), 5); ++a)
		{
			int angle1 = rand.nextInt(45) + 90 * a;
			int angle2 = 15 + rand.nextInt(15);
			drawCrystal(1, 0, 0, 0, angle1, angle2, 1, rand, red, green, blue);
		}
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileCrystalOre te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		BlockOreCrystal block = WorldUtil.cast(te.getWorld().getBlockState(te.getPos()).getBlock(), BlockOreCrystal.class);
		if(block == null)
			return;
		
		float red = ((block.getCrystalColor() >> 16) & 0xFF) / 255F;
		float green = ((block.getCrystalColor() >> 8) & 0xFF) / 255F;
		float blue = ((block.getCrystalColor() >> 0) & 0xFF) / 255F;
		
		rand.setSeed(te.getWorld().getSeed() + (te.getWorld().provider.getDimension() + 2) + te.getPos().toLong() + block.getCrystalColor());
		
		mc.getTextureManager().bindTexture(crystal);
		
		drawCrystal(te.orientation, (float) x, (float) y, (float) z, (rand.nextFloat() - rand.nextFloat()) * 5.0f, (rand.nextFloat() - rand.nextFloat()) * 5.0f, 1, rand, red, green, blue);
		for(int a = 1; a < te.crystals; ++a)
		{
			int angle1 = rand.nextInt(45) + 90 * a;
			int angle2 = 15 + rand.nextInt(15);
			drawCrystal(te.orientation, (float) x, (float) y, (float) z, angle1, angle2, 1, rand, red, green, blue);
		}
		
		rand.setSeed(te.getWorld().getSeed() + (te.getWorld().provider.getDimension() + 2) + te.getPos().toLong() + block.getCrystalColor());
		
		if(destroyStage != null)
		{
			mc.getTextureManager().bindTexture(destroyStage);
			drawCrystal(te.orientation, (float) x, (float) y, (float) z, (rand.nextFloat() - rand.nextFloat()) * 5.0f, (rand.nextFloat() - rand.nextFloat()) * 5.0f, 1, rand, red, green, blue);
			for(int a = 1; a < te.crystals; ++a)
			{
				int angle1 = rand.nextInt(45) + 90 * a;
				int angle2 = 15 + rand.nextInt(15);
				drawCrystal(te.orientation, (float) x, (float) y, (float) z, angle1, angle2, 1, rand, red, green, blue);
			}
		}
	}
	
	private void translateFromOrientation(float x, float y, float z, int orientation)
	{
		if(orientation == 0)
		{
			GL11.glTranslatef((x + 0.5f), (y + 1.3f), (z + 0.5f));
			GL11.glRotatef(180, 1, 0, 0);
		} else if(orientation == 1)
		{
			GL11.glTranslatef((x + 0.5f), (y - 0.3f), (z + 0.5f));
		} else if(orientation == 2)
		{
			GL11.glTranslatef((x + 0.5f), (y + 0.5f), (z + 1.3f));
			GL11.glRotatef(-90, 1, 0, 0);
		} else if(orientation == 3)
		{
			GL11.glTranslatef((x + 0.5f), (y + 0.5f), (z - 0.3f));
			GL11.glRotatef(90, 1, 0, 0);
		} else if(orientation == 4)
		{
			GL11.glTranslatef((x + 1.3f), (y + 0.5f), (z + 0.5f));
			GL11.glRotatef(90, 0, 0, 1);
		} else if(orientation == 5)
		{
			GL11.glTranslatef((x - 0.3f), (y + 0.5f), (z + 0.5f));
			GL11.glRotatef(-90, 0, 0, 1);
		}
	}
	
	private void drawCrystal(int ori, float x, float y, float z, float a1, float a2, float shade, Random rand, float red, float green, float blue)
	{
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		GLRenderState norm = GLRenderState.NORMALIZE;
		norm.captureState();
		
		GL11.glPushMatrix();
		norm.on();
		GL11.glEnable(32826);
		GL11.glBlendFunc(770, 771);
		Tessellator tessellator = Tessellator.getInstance();
		
		this.translateFromOrientation(x, y, z, ori);
		
		GL11.glPushMatrix();
		GL11.glRotatef(a1, 0, 1, 0);
		GL11.glRotatef(a2, 1, 0, 0);
		GL11.glPushMatrix();
		GL11.glColor4f(shade * red, shade * green, shade * blue, 1);
		GL11.glScalef((.15F + rand.nextFloat() * .075F), (.5F + rand.nextFloat() * .1F), (0.15f + rand.nextFloat() * .05F));
		this.model.render();
		GL11.glScalef(1, 1, 1);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glDisable(32826);
		GL11.glPopMatrix();
		blend.reset();
		norm.reset();
		GL11.glColor4f(1, 1, 1, 1);
	}
}