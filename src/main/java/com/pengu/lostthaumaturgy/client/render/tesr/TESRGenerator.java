package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.model.ModelGenCore;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileGenerator;

public class TESRGenerator extends TESR<TileGenerator>
{
	public static final TESRGenerator INSTANCE = new TESRGenerator();
	public final ModelGenCore model = new ModelGenCore();
	public final ResourceLocation texture = new ResourceLocation(LTInfo.MOD_ID, "textures/models/thaum_generator.png");
	
	@Override
	public void renderTileEntityAt(TileGenerator te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		renderBase(te, null, x, y, z, destroyStage, alpha);
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		renderBase(null, item, 0, 0, 0, null, 1);
	}
	
	public void renderBase(TileGenerator tile, ItemStack stack, double x, double y, double z, ResourceLocation destroyStage, float partialTicks)
	{
		RenderBlocks rb = RenderBlocks.forMod(LTInfo.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = partialTicks;
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		float time = (float) Math.abs(tile != null ? tile.rotation : stack != null ? Minecraft.getMinecraft().world.getTotalWorldTime() % 360L : 0) % 360F;
		float f2 = time + (tile != null ? tile.getWorld().isBlockIndirectlyGettingPowered(tile.getPos()) > 0 ? 0 : partialTicks : partialTicks);
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + 1.3, z + .5);
		float f3 = MathHelper.floor(f2 * .2F) / 2F + .5F;
		f3 = f3 * f3 + f3;
		bindTexture(texture);
		model.render(0, f2, f3 * .01F, 0, 0, .0625F);
		if(destroyStage != null)
		{
			bindTexture(destroyStage);
			model.render(0, f2, f3 * .01F, 0, 0, .0625F);
		}
		GL11.glPopMatrix();
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
		
		sbr.begin();
		
		TextureAtlasSprite warded = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/warded_glass");
		TextureAtlasSprite generator = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/generator");
		
		for(int i = 0; i < (destroyStage == null ? 1 : 2); ++i)
		{
			if(i == 1)
				warded = generator = DestroyStageTexture.getAsSprite(destroyProgress);
			
			sbr.setBrightness(getBrightnessForRB(tile, sbr.rb));
			sbr.setSprite(warded);
			sbr.setRenderBounds(1 / 8D, 1 / 8D, 1 / 8D, 7 / 8D, 7 / 8D, 7 / 8D);
			sbr.drawBlock(x, y, z);
			sbr.setSprite(generator);
			sbr.setRenderBounds(.25, 0, .25, .75, 1 / 8D, .75);
			sbr.drawBlock(x, y, z);
			sbr.setRenderBounds(.25, 7 / 8D, .25, .75, 1, .75);
			sbr.drawBlock(x, y, z);
			sbr.setRenderBounds(.25, .25, 0, .75, .75, 1 / 8D);
			sbr.drawBlock(x, y, z);
			sbr.setRenderBounds(.25, .25, 7 / 8D, .75, .75, 1);
			sbr.drawBlock(x, y, z);
			sbr.setRenderBounds(0, .25, .25, 1 / 8D, .75, .75);
			sbr.drawBlock(x, y, z);
			sbr.setRenderBounds(7 / 8D, .25, .25, 1, .75, .75);
			sbr.drawBlock(x, y, z);
		}
		sbr.end();
		
		rb.renderAlpha = srcAlpha;
	}
}