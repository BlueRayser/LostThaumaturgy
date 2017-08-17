package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileInfuser;

public class TESRInfuser extends TESR<TileInfuser>
{
	public static final TESRInfuser INSTANCE = new TESRInfuser();
	
	@Override
	public void renderItem(ItemStack item)
	{
		double rotation = 0;
		boolean active = false;
		
		NBTTagCompound nbt = getNBTFromItemStack(item);
		
		if(nbt != null && nbt.getFloat("CookCost") > 0 && nbt.getFloat("CookTime") > 0)
		{
			rotation = nbt.getFloat("CookTime") / nbt.getFloat("CookCost") * 360F;
			active = true;
		}
		
		renderModel(null, 0, 0, 0, rotation, active);
	}
	
	@Override
	public void renderTileEntityAt(TileInfuser te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		double rotation = te.angle;
		boolean active = te.getCookProgressScaled(8) > 0F;
		if(!active)
			rotation = 0;
		
		renderModel(te, x, y, z, rotation, active);
		
		if(active && te.canSpawnParticle && te.sucked > 0F && te.getWorld().rand.nextFloat() < te.sucked)
		{
			te.canSpawnParticle = false;
			float xx = (float) te.getPos().getX() + 0.5f - (te.getWorld().rand.nextFloat() - te.getWorld().rand.nextFloat()) * 0.35f;
			float yy2 = (float) te.getPos().getY() + 0.9475f;
			float zz = (float) te.getPos().getZ() + 0.5f - (te.getWorld().rand.nextFloat() - te.getWorld().rand.nextFloat()) * 0.35f;
			FXWisp ef = new FXWisp(te.getWorld(), xx, yy2, zz, xx, yy2 + te.getWorld().rand.nextFloat(), zz, 0.5f, te.getWorld().rand.nextInt(5));
			Minecraft.getMinecraft().effectRenderer.addEffect(ef);
		}
		
		destroyProgress = 0;
		rb.renderAlpha = srcAlpha;
	}
	
	protected ResourceLocation disk = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/infuser_symbol.png");
	
	private void renderModel(TileInfuser tile, double x, double y, double z, double angle, boolean active)
	{
		SimpleBlockRendering sbr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
		int bright = getBrightnessForRB(tile, sbr.rb);
		
		Tessellator tess = Tessellator.getInstance();
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		sbr.begin();
		sbr.setBrightness(bright);
		
		TextureAtlasSprite side_disconnected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser_side_disconnected");
		TextureAtlasSprite bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser_bottom");
		TextureAtlasSprite top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser_top");
		TextureAtlasSprite side_connected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser_side_connected");
		
		for(int i = 0; i < (destroyProgress > 0 ? 2 : 1); ++i)
		{
			sbr.rb.renderFromInside = false;
			sbr.setRenderBounds(0, 0, 0, 1, 15D / 16D, 1);
			
			if(i == 1)
				sbr.setSprite(DestroyStageTexture.getAsSprite(destroyProgress));
			else
				for(EnumFacing f : EnumFacing.VALUES)
				{
					if(f == EnumFacing.UP)
						sbr.setSpriteForSide(f, top);
					else if(f == EnumFacing.DOWN)
						sbr.setSpriteForSide(f, bottom);
					else if(tile != null)
						sbr.setSpriteForSide(f, tile.isConnected(f) ? side_connected : side_disconnected);
					else
						sbr.setSpriteForSide(f, side_disconnected);
				}
			
			sbr.drawBlock(x, y, z);
			
			if(i == 0)
				sbr.setSidedSprites(bottom, top, side_disconnected);
			
			sbr.setRenderBounds(0, 15 / 16D, 0, 3 / 16D, 1, 3 / 16D);
			sbr.disableFace(EnumFacing.DOWN);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(0, 15 / 16D, 13 / 16D, 3 / 16D, 1, 1);
			sbr.disableFace(EnumFacing.DOWN);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(13 / 16D, 15 / 16D, 0, 1, 1, 3 / 16D);
			sbr.disableFace(EnumFacing.DOWN);
			sbr.drawBlock(x, y, z);
			
			sbr.setRenderBounds(13 / 16D, 15 / 16D, 13 / 16D, 1, 1, 1);
			sbr.disableFace(EnumFacing.DOWN);
			sbr.drawBlock(x, y, z);
		}
		
		sbr.end();
		
		double activeRed = 1;
		double activeGreen = .5;
		double activeBlue = 1;
		
		if(tile != null && tile.getWorld().isBlockIndirectlyGettingPowered(tile.getPos()) > 0)
		{
			activeGreen = .2;
			activeBlue = .2;
			activeRed = .7;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y + 15.1 / 16, z);
		GL11.glRotated(90, 1, 0, 0);
		
		GL11.glDepthMask(false);
		
		GL11.glScaled(.85, .85, .85);
		
		GL11.glTranslated(.59, .59, 0);
		GL11.glRotated(angle, 0, 0, 1);
		GL11.glTranslated(-.5, -.5, 0);
		
		GL11.glScaled(1 / 256D, 1 / 256D, 1 / 256D);
		
		if(active)
			GL11.glBlendFunc(770, 1);
		else
			GL11.glBlendFunc(770, 771);
		
		if(active)
			GL11.glColor4d(activeRed, activeGreen, activeBlue, 1);
		else
			GL11.glColor4f(0, 0, 0, 1);
		
		bindTexture(disk);
		RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
		GL11.glPopMatrix();
		
		GL11.glDepthMask(true);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1, 1, 1, 1);
		
		blend.reset();
		
		
	}
}