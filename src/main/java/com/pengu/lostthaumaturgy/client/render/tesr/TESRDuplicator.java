package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;
import com.pengu.lostthaumaturgy.client.model.ModelDuplicator;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.tile.TileDuplicator;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class TESRDuplicator extends TESR<TileDuplicator>
{
	public static final TESRDuplicator INSTANCE = new TESRDuplicator();
	private ModelDuplicator model = new ModelDuplicator();
	public final ResourceLocation texture = new ResourceLocation(Info.MOD_ID, "textures/models/duplicator.png");
	
	@Override
	public void renderBase(TileDuplicator tile, ItemStack stack, double x, double y, double z, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(Info.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		float dist = .04372F;
		
		if(tile != null)
		{
			dist = .1875F - tile.press.get();
			
			if(!tile.forbid && tile.getWorld().rand.nextFloat() < tile.duplicatorCopyTime / tile.currentItemCopyCost)
			{
				float xx = tile.getPos().getX() + .5F - (tile.getWorld().rand.nextFloat() - tile.getWorld().rand.nextFloat()) * .7F;
				float yy = tile.getPos().getY() + .5F - (tile.getWorld().rand.nextFloat() - tile.getWorld().rand.nextFloat()) * .7F;
				float zz = tile.getPos().getZ() + .5F - (tile.getWorld().rand.nextFloat() - tile.getWorld().rand.nextFloat()) * .7F;
				
				ParticleProxy_Client.queueParticleSpawn(new FXWisp(tile.getWorld(), tile.getPos().getX() + .5F, tile.getPos().getY() + .5F, tile.getPos().getZ() + .5F, xx, yy, zz, 1F, tile.getWorld().rand.nextInt(5)));
				
				tile.forbid = true;
			}
		}
		
		bindTexture(texture);
		
		GL11.glEnable(2977);
		GL11.glEnable(3042);
		GL11.glPushMatrix();
		GL11.glEnable(32826);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glTranslated(x + .5D, y - .5D, z + .5D);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, (-dist), 0);
		model.render();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glRotatef(180, 0, 0, 1);
		GL11.glTranslatef(0, (-2 - dist), 0);
		model.render();
		GL11.glPopMatrix();
		GL11.glDisable(32826);
		GL11.glPopMatrix();
		GL11.glDisable(3042);
		GL11.glColor4f(1, 1, 1, 1);
		
		if(tile != null && tile.orientation.get() < 0)
			return;
		
		EnumFacing front = tile != null ? EnumFacing.VALUES[tile.orientation.get()] : EnumFacing.SOUTH;
		
		SimpleBlockRendering sbr = rb.simpleRenderer;
		
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(tile, sbr.rb));
		sbr.setSprite(ClientProxy.getSprite(Info.MOD_ID + ":blocks/duplicator/side"));
		sbr.setSpriteForSide(front, ClientProxy.getSprite(Info.MOD_ID + ":blocks/duplicator/front"));
		sbr.setSpriteForSide(EnumFacing.UP, ClientProxy.getSprite(Info.MOD_ID + ":blocks/duplicator/y"));
		sbr.setSpriteForSide(EnumFacing.DOWN, ClientProxy.getSprite(Info.MOD_ID + ":blocks/duplicator/y"));
		sbr.drawBlock(x, y, z);
		sbr.rb.renderFromInside = true;
		sbr.setSprite(ClientProxy.getSprite(Info.MOD_ID + ":blocks/duplicator/inner"));
		sbr.setRenderBounds(.01, 1 / 8D, .01, .99, 7 / 8D, .99);
		sbr.disableFace(front);
		sbr.drawBlock(x, y, z);
		sbr.end();
		
		rb.renderAlpha = srcAlpha;
	}
}