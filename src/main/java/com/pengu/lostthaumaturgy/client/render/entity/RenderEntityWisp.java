package com.pengu.lostthaumaturgy.client.render.entity;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import org.lwjgl.opengl.GL11;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.entity.EntityWisp;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class RenderEntityWisp extends Render<EntityWisp>
{
	public static final Factory FACTORY = new Factory();
	
	int particleAge = 0;
	int moteHalfLife = 10;
	
	private static final ResourceLocation p_large = new ResourceLocation(LTInfo.MOD_ID, "textures/particle/p_large.png");
	private static final ResourceLocation wisp = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/wisp.png");
	
	public RenderEntityWisp(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}
	
	@Override
	public void doRender(EntityWisp entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		y += .5;
		
		particleAge = (int) ((System.currentTimeMillis() + entity.hashCode()) % 800L);
		float agescale = particleAge / 400F;
		if(agescale > 1F)
			agescale = 2F - agescale;
		
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		
		bindTexture(p_large);
		
		float f1 = ActiveRenderInfo.getRotationX();
		float f2 = ActiveRenderInfo.getRotationXZ();
		float f3 = ActiveRenderInfo.getRotationZ();
		float f4 = ActiveRenderInfo.getRotationYZ();
		float f5 = ActiveRenderInfo.getRotationXY();
		float f10 = agescale * 0.6f;
		float f11 = (float) x;
		float f12 = (float) y;
		float f13 = (float) z;
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		
		int bright = 240;
		float r, g, b, a = .5F;
		
		if(((EntityWisp) entity).getType() != 5)
		{
			r = .6F;
			g = 0F;
			b = .75F;
		} else
		{
			r = .4F;
			g = 0F;
			b = .5F;
		}
		
		tessellator.getBuffer().pos(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10).tex(0, 1).color(r, g, b, a).endVertex();
		tessellator.getBuffer().pos(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10).tex(1, 1).color(r, g, b, a).endVertex();
		tessellator.getBuffer().pos(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10).tex(1, 0).color(r, g, b, a).endVertex();
		tessellator.getBuffer().pos(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10).tex(0, 0).color(r, g, b, a).endVertex();
		
		tessellator.draw();
		
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
		
		bindTexture(wisp);
		
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		
		int i = 234 + ((EntityWisp) entity).getType();
		int size = ClientProxy.getTexSize(wisp.toString(), 16);
		float size16 = size * 16;
		float float_sizeMinus0_01 = (float) size - 0.01f;
		float float_texNudge = 1.0f / ((float) size * (float) size * 2.0f);
		float float_reciprocal = 1.0f / (float) size;
		f1 = ActiveRenderInfo.getRotationX();
		f2 = ActiveRenderInfo.getRotationXZ();
		f3 = ActiveRenderInfo.getRotationZ();
		f4 = ActiveRenderInfo.getRotationYZ();
		f5 = ActiveRenderInfo.getRotationXY();
		float x0 = ((float) (i % 16 * size) + 0.0f) / size16;
		float x1 = ((float) (i % 16 * size) + float_sizeMinus0_01) / size16;
		float x2 = ((float) (i / 16 * size) + 0.0f) / size16;
		float x3 = ((float) (i / 16 * size) + float_sizeMinus0_01) / size16;
		f10 = 0.15f;
		f11 = (float) x;
		f12 = (float) y;
		f13 = (float) z;
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tessellator.getBuffer().pos(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10).tex(x1, x3).endVertex();
		tessellator.getBuffer().pos(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10).tex(x1, x2).endVertex();
		tessellator.getBuffer().pos(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10).tex(x0, x2).endVertex();
		tessellator.getBuffer().pos(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10).tex(x0, x3).endVertex();
		tessellator.draw();
		GL11.glDisable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glPopMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityWisp entity)
	{
		return p_large;
	}
	
	public static final class Factory implements IRenderFactory<EntityWisp>
	{
		@Override
		public Render<? super EntityWisp> createRenderFor(RenderManager manager)
		{
			return new RenderEntityWisp(manager);
		}
	}
}