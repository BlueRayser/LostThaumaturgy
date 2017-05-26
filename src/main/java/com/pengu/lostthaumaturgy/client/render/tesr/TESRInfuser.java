package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;
import com.pengu.lostthaumaturgy.client.model.ModelInfuser;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileInfuser;

public class TESRInfuser extends TESR<TileInfuser>
{
	public static final TESRInfuser INSTANCE = new TESRInfuser();
	private final ModelInfuser model = new ModelInfuser();
	
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
	public void renderTileEntityAt(TileInfuser te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
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
			te.canSpawnParticle = false; // ensure that if player pauses the
										 // game we don't spawn thousands of
										 // particles
			
			float xx = (float) te.getPos().getX() + 0.5f - (te.getWorld().rand.nextFloat() - te.getWorld().rand.nextFloat()) * 0.35f;
			float yy2 = (float) te.getPos().getY() + 0.9475f;
			float zz = (float) te.getPos().getZ() + 0.5f - (te.getWorld().rand.nextFloat() - te.getWorld().rand.nextFloat()) * 0.35f;
			FXWisp ef = new FXWisp(te.getWorld(), xx, yy2, zz, xx, yy2 + te.getWorld().rand.nextFloat(), zz, 0.5f, /* te
																													 * .
																													 * i
																													 * (
																													 * )
																													 * ==
																													 * 2
																													 * ?
																													 * 5
																													 * --
																													 * dark
																													 * infuser */te.getWorld().rand.nextInt(5));
			Minecraft.getMinecraft().effectRenderer.addEffect(ef);
		}
	}
	
	protected ResourceLocation pillars = new ResourceLocation(LTInfo.MOD_ID, "textures/models/infuser.png");
	protected ResourceLocation disk = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/infuser_symbol.png");
	
	private void renderModel(TileInfuser tile, double x, double y, double z, double angle, boolean active)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		int bright = getBrightnessForRB(tile, rb);
		
		GlStateManager.disableLighting();
		
		Tessellator tess = Tessellator.getInstance();
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		TextureAtlasSprite side_disconnected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser_side_disconnected");
		TextureAtlasSprite bottom = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser_bottom");
		TextureAtlasSprite top = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser_top");
		TextureAtlasSprite side_connected = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/infuser_side_connected");
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		rb.renderFromInside = false;
		rb.setRenderBounds(0, 0, 0, 1, 15D / 16D, 1);
		
		if(tile != null)
		{
			rb.renderFaceXNeg(x, y, z, tile.isConnected(EnumFacing.WEST) ? side_connected : side_disconnected, 1, 1, 1, bright);
			rb.renderFaceXPos(x, y, z, tile.isConnected(EnumFacing.EAST) ? side_connected : side_disconnected, 1, 1, 1, bright);
			rb.renderFaceZNeg(x, y, z, tile.isConnected(EnumFacing.NORTH) ? side_connected : side_disconnected, 1, 1, 1, bright);
			rb.renderFaceZPos(x, y, z, tile.isConnected(EnumFacing.SOUTH) ? side_connected : side_disconnected, 1, 1, 1, bright);
		} else
		{
			rb.renderFaceXNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceXPos(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceZNeg(x, y, z, side_disconnected, 1, 1, 1, bright);
			rb.renderFaceZPos(x, y, z, side_disconnected, 1, 1, 1, bright);
		}
		
		rb.renderFaceYPos(x, y, z, top, 1, 1, 1, bright);
		rb.renderFaceYNeg(x, y, z, bottom, 1, 1, 1, bright);
		
		tess.draw();
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + 1.5, z + .5);
		bindTexture(pillars);
		GL11.glPushMatrix();
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();
		GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
		model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, false);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
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