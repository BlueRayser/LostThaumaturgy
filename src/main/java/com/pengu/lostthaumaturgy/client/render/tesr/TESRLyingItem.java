package com.pengu.lostthaumaturgy.client.render.tesr;

import java.util.Random;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.core.tile.TileLyingItem;

public class TESRLyingItem extends TESR<TileLyingItem>
{
	public static final TESRLyingItem INSTANCE = new TESRLyingItem();
	private static final Random rand = new Random();
	
	@Override
	public void renderTileEntityAt(TileLyingItem te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		ItemStack stack = te.lying.get();
		
		double oos = 1 / 6D;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + oos, y, z + oos);
		
		rand.setSeed(te.getPos().toLong() + te.lying.get().getItem().getRegistryName().toString().hashCode() - te.lying.get().getItemDamage());
		
		GL11.glTranslated(rand.nextFloat() * (1 - oos * 2), 0, rand.nextFloat() * (1 - oos * 2));
		
		GL11.glTranslated(1 / 16D, 0, 1 / 16D);
		GL11.glRotated(rand.nextDouble() * 360D, 0, 1, 0);
		GL11.glTranslated(-1 / 16D, 0, -1 / 16D);
		
		GL11.glRotated(90, 1, 0, 0);
		
		mc.getRenderItem().renderItem(stack, TransformType.GROUND);
		
		GL11.glPopMatrix();
	}
}