package com.pengu.lostthaumaturgy.client.render.tesr;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.OpnodeLoader;
import com.pengu.hammercore.client.model.simple.OpnodeRender;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.tile.TileDarknessGenerator;

public class TESRDarknessGenerator extends TESR<TileDarknessGenerator>
{
	public static final TESRDarknessGenerator INSTANCE = new TESRDarknessGenerator();
	
	@Override
	public void renderBase(TileDarknessGenerator tile, ItemStack stack, double x, double y, double z, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(Info.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		List<int[]> opnode = OpnodeLoader.loadOpnodes(Info.MOD_ID, "tile/darkness_generator");
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		OpnodeRender.renderOpnodes(rb.simpleRenderer, opnode, getBrightnessForRB(tile, rb), true);
		GL11.glPopMatrix();
		
		rb.renderAlpha = srcAlpha;
	}
}