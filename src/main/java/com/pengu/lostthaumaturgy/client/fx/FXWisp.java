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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.lostthaumaturgy.LTInfo;

@SideOnly(Side.CLIENT)
public class FXWisp extends SimpleParticle
{
	public boolean shrink = false;
	float moteParticleScale;
	int moteHalfLife;
	public boolean tinkle = false;
	public int blendmode = 1;
	
	public FXWisp(World world, double d, double d1, double d2, float partialTicks, float rotationX, float rotationZ)
	{
		this(world, d, d1, d2, 1.0f, partialTicks, rotationX, rotationZ);
	}
	
	public FXWisp(World world, double d, double d1, double d2, float partialTicks, float rotationX, float rotationZ, float rotationYZ)
	{
		super(world, d, d1, d2, 0.0, 0.0, 0.0);
		if(rotationX == 0F)
			rotationX = 1F;
		this.particleRed = rotationX;
		this.particleGreen = rotationZ;
		this.particleBlue = rotationYZ;
		this.particleGravity = 0.0f;
		motionX = 0.0;
		motionY = 0.0;
		motionZ = 0.0;
		particleScale *= partialTicks;
		this.moteParticleScale = particleScale;
		particleMaxAge = (int) (36.0 / (Math.random() * 0.3 + 0.7));
		this.moteHalfLife = particleMaxAge / 2;
		// motionY = false;
	}
	
	public FXWisp(World world, double d, double d1, double d2, float partialTicks, int type)
	{
		this(world, d, d1, d2, partialTicks, 0.0f, 0.0f, 0.0f);
		switch(type)
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
			particleRed = 0.5f + world.rand.nextFloat() * 0.3f;
			particleGreen = 0.5f + world.rand.nextFloat() * 0.3f;
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
			this.blendmode = 771;
			particleRed = world.rand.nextFloat() * 0.1f;
			particleGreen = world.rand.nextFloat() * 0.1f;
			particleBlue = world.rand.nextFloat() * 0.1f;
			break;
		}
		case 6:
		{
			particleRed = 0.8f + world.rand.nextFloat() * 0.2f;
			particleGreen = 0.8f + world.rand.nextFloat() * 0.2f;
			particleBlue = 0.8f + world.rand.nextFloat() * 0.2f;
			break;
		}
		case 7:
		{
			float rr = world.rand.nextFloat();
			particleRed = 0.2f + rr * 0.3f;
			particleGreen = 0.2f + rr * 0.3f;
			particleBlue = 0.7f + world.rand.nextFloat() * 0.3f;
		}
		}
	}
	
	public FXWisp(World world, double x, double y, double z, double tx, double ty, double tz, float partialTicks, int type)
	{
		this(world, x, y, z, partialTicks, type);
		double dx = tx - this.posX;
		double dy2 = ty - this.posY;
		double dz = tz - this.posZ;
		this.motionX = dx / (double) particleMaxAge;
		this.motionY = dy2 / (double) particleMaxAge;
		this.motionZ = dz / (double) particleMaxAge;
	}
	
	public FXWisp setColor(int color)
	{
		particleRed = ((color >> 16) & 255) / 255F;
		particleGreen = ((color >> 8) & 255) / 255F;
		particleBlue = ((color >> 0) & 255) / 255F;
		
		return this;
	}
	
	static final ResourceLocation p_large = new ResourceLocation(LTInfo.MOD_ID, "textures/particle/p_large.png");
	
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
		GlStateManager.alphaFunc(516, 0.003921569F);
		
		float agescale = 0.0f;
		if(this.shrink)
		{
			agescale = ((float) particleMaxAge - (float) particleAge) / (float) particleMaxAge;
		} else
		{
			agescale = (float) particleAge / (float) this.moteHalfLife;
			if(agescale > 1.0f)
				agescale = 2.0f - agescale;
		}
		particleScale = this.moteParticleScale * agescale;
		
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GLRenderState.BLEND.captureState();
		GLRenderState.BLEND.on();
		GL11.glBlendFunc(770, blendmode);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(p_large);
		
		VertexBuffer buffer = Tessellator.getInstance().getBuffer();
		
		buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		particleAlpha = 0.5F;
		
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
	
	@Override
	public void onUpdate()
	{
		Entity renderentity = Minecraft.getMinecraft().getRenderViewEntity();
		double visibleDistance = 50;
		
		if(Minecraft.getMinecraft().gameSettings.particleSetting > 0)
			visibleDistance = 25;
		if(Minecraft.getMinecraft().gameSettings.particleSetting > 1)
			visibleDistance = 5;
		
		if(renderentity.getDistance(posX, posY, posZ) > visibleDistance)
		{
			setExpired();
			return;
		}
		
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		if(particleAge == 0 && tinkle && rand.nextInt(3) == 0)
			world.playSound(posX, posY, posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, .003F, .7F * ((rand.nextFloat() - rand.nextFloat()) * 0.6f + 2.0f), false);
		
		if(particleAge++ >= particleMaxAge)
			setExpired();
		
		motionY -= 0.04 * particleGravity;
		
		move(motionX, motionY, motionZ);
		
		motionX *= 0.9800000190734863;
		motionY *= 0.9800000190734863;
		motionZ *= 0.9800000190734863;
		
		if(onGround)
		{
			motionX *= 0.699999988079071;
			motionZ *= 0.699999988079071;
		}
	}
	
	public void setGravity(float val)
	{
		particleGravity = val;
	}
}