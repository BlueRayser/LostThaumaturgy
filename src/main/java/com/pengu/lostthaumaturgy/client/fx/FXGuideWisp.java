package com.pengu.lostthaumaturgy.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.proxy.ParticleProxy_Client;
import com.pengu.hammercore.client.particle.api.SimpleParticle;

public class FXGuideWisp extends SimpleParticle
{
	boolean source = false;
	int type1;
	int type2;
	int seed = 0;
	double destX = 0.0;
	double destY = 0.0;
	double destZ = 0.0;
	float moteParticleScale;
	
	public FXGuideWisp(World world, double d, double d1, double d2, double x, double y, double z, float f, int t1, int t2, boolean source)
	{
		super(world, d, d1, d2, 0.0, 0.0, 0.0);
		setSize(.2F, .2F);
		particleGravity = 0;
		this.moteParticleScale = particleScale = f;
		particleMaxAge = 1000;
		if(!source)
			particleMaxAge = 100;
		canCollide = true;
		this.source = source;
		this.destX = x;
		this.destY = y;
		this.destZ = z;
		seed = world.rand.nextInt(100000);
		this.type1 = t1;
		this.type2 = t2;
		switch(t1)
		{
		case 0:
		{
			particleRed = 0.75f + world.rand.nextFloat() * 0.25f;
			particleGreen = 0.25f + world.rand.nextFloat() * 0.25f;
			particleBlue = 0.75f + world.rand.nextFloat() * 0.25f;
			break;
		}
		case 1:
		{
			particleRed = 0.4f + world.rand.nextFloat() * 0.3f;
			particleGreen = 0.6f + world.rand.nextFloat() * 0.3f;
			particleBlue = 0.2f;
			break;
		}
		case 2:
		{
			particleRed = 0.2f;
			particleGreen = 0.2f;
			particleBlue = 0.7f + world.rand.nextFloat() * 0.3f;
			break;
		}
		case 3:
		{
			particleRed = 0.2f;
			particleGreen = 0.7f + world.rand.nextFloat() * 0.3f;
			particleBlue = 0.2f;
			break;
		}
		case 4:
		{
			particleRed = 0.7f + world.rand.nextFloat() * 0.3f;
			particleGreen = 0.2f;
			particleBlue = 0.2f;
			break;
		}
		case 5:
		{
			particleRed = world.rand.nextFloat() * 0.1f;
			particleGreen = world.rand.nextFloat() * 0.1f;
			particleBlue = world.rand.nextFloat() * 0.1f;
		}
		}
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float f, float f1, float f2, float f3, float f4, float f5)
	{
		EntityPlayer entityIn = Minecraft.getMinecraft().player;
		ActiveRenderInfo.updateRenderInfo(entityIn, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2);
		
		Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * f;
		Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * f;
		Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * f;
		Particle.cameraViewDir = entityIn.getLook(f);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(516, 0.003921569F);
		
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buf = tessellator.getBuffer();
		
		Entity renderentity = Minecraft.getMinecraft().getRenderViewEntity();
		int visibleDistance = 100;
		if(renderentity.getDistance(posX, posY, posZ) > (double) visibleDistance)
			return;
		float scale = (float) Math.sqrt((particleAge + seed) / 5F);
		particleScale = this.moteParticleScale * scale;
		GL11.glPushMatrix();
		GL11.glDepthMask((boolean) false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(FXWisp.p_large);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 0.75f);
		float f10 = 0.5f * particleScale;
		float f11 = (float) (prevPosX + (posX - prevPosX) * (double) f - interpPosX);
		float f12 = (float) (prevPosY + (posY - prevPosY) * (double) f - interpPosY);
		float f13 = (float) (prevPosZ + (posZ - prevPosZ) * (double) f - interpPosZ);
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		buf.pos((double) (f11 - f1 * f10 - f4 * f10), (double) (f12 - f2 * f10), (double) (f13 - f3 * f10 - f5 * f10)).tex(0.0, 1.0).color(particleRed, particleGravity, particleBlue, source ? .5F : .25F).endVertex();
		buf.pos((double) (f11 - f1 * f10 + f4 * f10), (double) (f12 + f2 * f10), (double) (f13 - f3 * f10 + f5 * f10)).tex(1.0, 1.0).color(particleRed, particleGravity, particleBlue, source ? .5F : .25F).endVertex();
		buf.pos((double) (f11 + f1 * f10 + f4 * f10), (double) (f12 + f2 * f10), (double) (f13 + f3 * f10 + f5 * f10)).tex(1.0, 0.0).color(particleRed, particleGravity, particleBlue, source ? .5F : .25F).endVertex();
		buf.pos((double) (f11 + f1 * f10 - f4 * f10), (double) (f12 - f2 * f10), (double) (f13 + f3 * f10 - f5 * f10)).tex(0.0, 0.0).color(particleRed, particleGravity, particleBlue, source ? .5F : .25F).endVertex();
		Tessellator.getInstance().draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
		GL11.glBlendFunc(770, 771);
	}
	
	@Override
	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(particleAge++ >= particleMaxAge)
			setExpired();
		if(getDistance(destX, destY, destZ) < .75)
		{
			for(int a = 0; a < 30; ++a)
			{
				FXWisp ef = new FXWisp(world, posX, posY, posZ, posX + (double) (world.rand.nextFloat() - world.rand.nextFloat()), posY + (double) (world.rand.nextFloat() - world.rand.nextFloat()), posZ + (double) (world.rand.nextFloat() - world.rand.nextFloat()), 0.5f, this.type1);
				ef.shrink = true;
				ParticleProxy_Client.queueParticleSpawn(ef);
			}
			world.playSound(posX, posY, posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, .5F, world.rand.nextFloat() * .4F + .8F, true);
			setExpired();
		}
		motionX = (this.destX - posX) * 0.019999999552965164;
		motionY = (this.destY - posY) * 0.019999999552965164;
		motionZ = (this.destZ - posZ) * 0.019999999552965164;
		move(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863;
		motionY *= 0.9800000190734863;
		motionZ *= 0.9800000190734863;
		if(onGround)
		{
			motionX *= 0.699999988079071;
			motionZ *= 0.699999988079071;
		}
		if(source)
		{
			FXWisp ef = new FXWisp(world, posX + (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.1f), posY + (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.1f), posZ + (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.1f), 0.5f, this.type2);
			ef.shrink = true;
			ParticleProxy_Client.queueParticleSpawn(ef);
		}
	}
	
	public double getDistance(double x, double y, double z)
	{
		double d0 = this.posX - x;
		double d1 = this.posY - y;
		double d2 = this.posZ - z;
		return (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	}
}