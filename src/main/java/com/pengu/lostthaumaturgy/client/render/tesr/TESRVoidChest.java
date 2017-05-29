package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.HammerCore;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.tile.TileVoidChest;

public class TESRVoidChest extends TESR<TileVoidChest>
{
	public static final TESRVoidChest INSTANCE = new TESRVoidChest();
	
	@Override
	public void renderTileEntityAt(TileVoidChest te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(x + .01, y + .01, z + .01);
		GL11.glScaled(.98, .98, .98);
		HammerCore.renderProxy.getRenderHelper().renderEndPortalEffect(0, 0, 0, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH);
		GL11.glPopMatrix();
	}
}