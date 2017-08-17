package com.pengu.lostthaumaturgy.client.render.seal;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.common.EnumRotation;
import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.tile.TileSeal;

public class TESRSeal extends TESR<TileSeal>
{
	public static final TESRSeal INSTANCE = new TESRSeal();
	
	@Override
	public void translateFromOrientation(double x, double y, double z, EnumFacing facing)
	{
		int orientation = facing.ordinal();
		
		if(orientation == 0)
		{
			GL11.glTranslated(x + .5, y + 1, z + .5);
			GL11.glRotated(180, 1, 0, 0);
		} else if(orientation == 1)
		{
			GL11.glTranslated(x + .5, y, z + .5);
		} else if(orientation == 2)
		{
			GL11.glTranslated(x + .5, y + .5, z + 1);
			GL11.glRotatef(-90, 1, 0, 0);
		} else if(orientation == 3)
		{
			GL11.glTranslated(x + .5, y + .5, z);
			GL11.glRotatef(90, 1, 0, 0);
		} else if(orientation == 4)
		{
			GL11.glTranslated(x + 1, y + .5, z + .5);
			GL11.glRotatef(90, 0, 0, 1);
		} else if(orientation == 5)
		{
			GL11.glTranslated(x, y + .5, z + .5);
			GL11.glRotatef(-90, 0, 0, 1);
		}
	}
	
	@Override
	public void renderTileEntityAt(TileSeal te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		if(te.getLocation().getBlock() != BlocksLT.SEAL)
			return;
		
		GL11.glPushMatrix();
		translateFromOrientation(x, y, z, te.getLocation().getState().getValue(EnumRotation.EFACING));
		GL11.glRotated(90, 1, 0, 0);
		GL11.glTranslated(0, -2 / 16D, 0);
		mc.getRenderItem().renderItem(te.stack.get(), TransformType.GROUND);
		for(int i = 0; i < 3; ++i)
		{
			ItemSealSymbol ss = te.getSymbol(i);
			if(ss != null)
				ss.renderSymbol(te, 0, 0, 0, partialTicks, i);
		}
		GL11.glPopMatrix();
	}
}