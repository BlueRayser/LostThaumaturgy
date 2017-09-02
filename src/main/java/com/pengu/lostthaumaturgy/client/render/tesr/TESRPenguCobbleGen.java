package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.shader.ShaderProgram;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.lostthaumaturgy.client.model.ModelTinyPlayer;
import com.pengu.lostthaumaturgy.client.render.shared.LiquidVisRenderer;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.tile.TilePenguCobbleGen;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class TESRPenguCobbleGen extends TESR<TilePenguCobbleGen>
{
	public static final TESRPenguCobbleGen INSTANCE = new TESRPenguCobbleGen();
	
	public ModelTinyPlayer player = new ModelTinyPlayer();
	
	@Override
	public void renderBase(TilePenguCobbleGen tile, ItemStack stack, double x, double y, double z, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(Info.MOD_ID);
		float srcAlpha = rb.renderAlpha;
		rb.renderAlpha = alpha;
		
		SimpleBlockRendering sbr = rb.simpleRenderer;
		
		sbr.begin();
		sbr.setBrightness(getBrightnessForRB(tile, sbr.rb));
		
		sbr.setSprite(ClientProxy.getSprite(Info.MOD_ID + ":blocks/pengu_cobble_gen"));
		
		sbr.setRenderBounds(0, 0, 0, 1, 1 / 8D, 1);
		sbr.drawBlock(x, y, z);
		
		sbr.setRenderBounds(0, 7 / 8D, 0, 1, 1, 1);
		sbr.drawBlock(x, y, z);
		
		sbr.setSprite(ClientProxy.getSprite("minecraft:blocks/glass"));
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		
		sbr.setRenderBounds(1 / 16D, 1 / 8D, 1 / 16D, 15 / 16D, 7 / 8D, 15 / 16D);
		sbr.drawBlock(x, y, z);
		
		sbr.end();
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + 1.175, z + .83);
		
		GL11.glScaled(.7, .7, .7);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
		
		ClientProxy.bindPenguSkin();
		
		float rotate = (float) (-85 - Math.sin(tile != null ? tile.ticksExisted / 4F : 0) * 30);
		
		ItemStack pickaxeItem = tile != null ? tile.pickaxe.get() : ItemStack.EMPTY;
		
		float a = rotate / 360F;
		player.renderAll(a * 6.23F);
		
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .63, y + .3 - rotate * .003, z + .63);
		GL11.glScaled(.2, .2, .2);
		GL11.glRotated(-rotate + 180, 1, 0, 0);
		GL11.glTranslated(0, .125, 0);
		mc.getRenderItem().renderItem(pickaxeItem, TransformType.THIRD_PERSON_RIGHT_HAND);
		GL11.glPopMatrix();
		
		if(tile != null && tile.toConsume != tile.maxConsume)
		{
			GL11.glPushMatrix();
			
			GL11.glTranslated(x + .25, y + 1 / 8D, z + .075);
			GL11.glScaled(.5, .5 * ((tile.maxConsume - tile.toConsume) / tile.maxConsume), .5);
			
			alpha = rb.renderAlpha;
			sbr.begin();
			float progress = 1 - tile.cooldownTimer / (float) tile.cooldownTimerMax;
			if(tile.timer != -1)
				progress = 1;
			
			ItemStack gen = tile.generated.get();
			sbr.setRenderBounds(0, 0, 0, 1, 1, 1);
			sbr.setBrightness(getBrightnessForRB(tile, rb));
			sbr.setSprite(mc.getRenderItem().getItemModelMesher().getParticleIcon(gen.getItem(), gen.getItemDamage()));
			rb.renderAlpha = progress;
			sbr.drawBlock(0, 0, 0);
			sbr.end();
			sbr.begin();
			sbr.setRenderBounds(0, 0, 0, 1, 1, 1);
			sbr.setBrightness(getBrightnessForRB(tile, rb));
			rb.renderAlpha = 1 - progress;
			sbr.setSprite(ClientProxy.getSprite(Info.MOD_ID + ":blocks/fluid_vis"));
			sbr.drawBlock(0, 0, 0);
			
			if(LiquidVisRenderer.useShaders() && LiquidVisRenderer.visShader == null)
				LiquidVisRenderer.reloadShader();
			if(LiquidVisRenderer.useShaders() && LiquidVisRenderer.visShader != null)
			{
				LiquidVisRenderer.operation.red = 200 / 255F;
				LiquidVisRenderer.operation.green = 0;
				LiquidVisRenderer.operation.blue = 1;
				LiquidVisRenderer.operation.setResolution(200);
				LiquidVisRenderer.visShader.freeBindShader();
				
				int loc = LiquidVisRenderer.visShader.getUniformLoc("alpha");
				ARBShaderObjects.glUniform1fARB(loc, (1 - progress) * .5F);
			}
			
			GLRenderState.BLEND.on();
			Tessellator.getInstance().draw();
			
			if(LiquidVisRenderer.useShaders())
				ShaderProgram.unbindShader();
			
			GLRenderState.BLEND.reset();
			rb.renderAlpha = alpha;
			
			GL11.glPopMatrix();
		}
		
		rb.renderAlpha = srcAlpha;
	}
	
	@Override
	public void renderFromNBT(NBTTagCompound nbt, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		
	}
	
	@Override
	public void renderTileEntityAt(TilePenguCobbleGen te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		
	}
}