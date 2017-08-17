package com.pengu.lostthaumaturgy.client.render.tesr.monolith;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.monolith.TileExtraRoom;

public class TESRMonolithExtraRoom extends TESR<TileExtraRoom>
{
	public static final TESRMonolithExtraRoom INSTANCE = new TESRMonolithExtraRoom();
	
	@Override
	public void renderTileEntityAt(TileExtraRoom te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(x + .01, y + .01, z + .01);
		GL11.glScaled(.98, .98, .98);
		HammerCore.renderProxy.getRenderHelper().renderEndPortalEffect(0, 0, 0, EnumFacing.VALUES[te.orientation.get()].getOpposite());
		GL11.glPopMatrix();
		
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(!BlocksLT.MONOLITH_OPENER.shouldSideBeRendered(te.getWorld().getBlockState(te.getPos()), te.getWorld(), te.getPos(), f))
				sbr.disableFace(f);
			
			if(f.getOpposite().ordinal() == te.orientation.get())
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/extra_room"));
			else
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/eldritch_block/" + f.ordinal()));
		}
		sbr.drawBlock(x, y, z);
		sbr.end();
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(f.getOpposite() == EnumFacing.NORTH)
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/monolith/extra_room"));
			else
				sbr.setSpriteForSide(f, ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/eldritch_block/" + f.ordinal()));
		}
		sbr.drawBlock(0, 0, 0);
		sbr.end();
	}
}