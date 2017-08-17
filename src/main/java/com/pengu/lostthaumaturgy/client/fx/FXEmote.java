package com.pengu.lostthaumaturgy.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.lostthaumaturgy.emote.EmoteData;
import com.pengu.lostthaumaturgy.emote.EmoteManager;

public class FXEmote extends SimpleParticle
{
	protected final EmoteData data;
	
	public FXEmote(EmoteData data)
	{
		super(Minecraft.getMinecraft().world, data.getX(), data.getY(), data.getZ());
		this.data = data;
		
		particleRed = data.getRed();
		particleGreen = data.getGreen();
		particleBlue = data.getBlue();
		particleAlpha = data.getAlpha();
		particleMaxAge = data.getFadeIn() + data.getStay() + data.getFadeOut();
		particleScale = data.getScale();
		particleGravity = 0;
		particleAge = 0;
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		EntityPlayer entityIn = Minecraft.getMinecraft().player;
		ActiveRenderInfo.updateRenderInfo(entityIn, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2);
		
		Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
		Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
		Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
		Particle.cameraViewDir = entityIn.getLook(partialTicks);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GLRenderState.BLEND.captureState();
		GLRenderState.BLEND.on();
		GL11.glBlendFunc(770, 771);
		
		double strength = .2 * particleScale;
		
		if(particleAge <= data.getFadeIn())
		{
			particleAlpha = ((float) particleAge / (float) data.getFadeIn()) * data.getAlpha();
			GL11.glTranslated(0, ((float) particleAge / (float) data.getFadeIn()) * strength, 0);
		} else if(particleAge < data.getFadeIn() + data.getStay())
		{
			particleAlpha = data.getAlpha();
			GL11.glTranslated(0, strength, 0);
		} else if(particleAge <= particleMaxAge)
		{
			particleAlpha = (1 - ((float) (particleAge - data.getFadeIn() - data.getStay()) / (float) data.getFadeOut())) * data.getAlpha();
			GL11.glTranslated(0, strength + ((float) (particleAge - data.getFadeIn() - data.getStay()) / (float) data.getFadeOut()) * strength, 0);
		}
		
		/** Bind to the emote texture */
		Minecraft.getMinecraft().getTextureManager().bindTexture(EmoteManager.getEmoteLocation(data.getId()));
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		
		buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		
		float f = (float) this.particleTextureIndexX / 16.0F;
		float f1 = f + 0.0624375F;
		float f2 = (float) this.particleTextureIndexY / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.1F * this.particleScale;
		
		f = 0;
		f1 = 1;
		f2 = 0;
		f3 = 1;
		
		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
		int i = 255;
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] { new Vec3d((double) (-rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((double) (-rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double) (rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double) (rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (rotationYZ * f4 - rotationXZ * f4)) };
		
		if(this.particleAngle != 0.0F)
		{
			float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);
			
			for(int l = 0; l < 4; ++l)
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
		}
		
		buffer.pos((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		
		Tessellator.getInstance().draw();
		
		GLRenderState.BLEND.reset();
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
		GL11.glBlendFunc(770, 771);
	}
}