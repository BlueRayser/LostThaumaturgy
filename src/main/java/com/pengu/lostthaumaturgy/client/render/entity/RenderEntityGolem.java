package com.pengu.lostthaumaturgy.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import org.lwjgl.opengl.GL11;

import com.pengu.lostthaumaturgy.client.model.ModelGolem;
import com.pengu.lostthaumaturgy.entity.EntityGolemBase;

public class RenderEntityGolem extends RenderLiving<EntityGolemBase>
{
	public static final Factory FACTORY = new Factory();
	
	public RenderEntityGolem(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelGolem(true), .25F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityGolemBase entity)
	{
		String tex = entity.getTexture();
		return new ResourceLocation(tex != null ? tex : "missing");
	}
	
	@Override
	protected void preRenderCallback(EntityGolemBase e, float partialTickTime)
	{
		if(e.hurtTime > 0)
			GL11.glColor4f(.9F, .5F, .5F, .4F);
		if(e.healing > 0)
		{
			float h1 = (float) e.healing / 10.0f;
			float h2 = (float) e.healing / 5.0f;
			GL11.glColor4f((float) (0.5f + h1), (float) (0.9f + h2), (float) (0.5f + h1), (float) 0.4f);
		}
	}
	
	@Override
	protected void renderLivingAt(EntityGolemBase e, double x, double y, double z)
	{
		super.renderLivingAt(e, x, y, z);
		GlStateManager.translate(0, -.85, 0);
	}
	
	public static class Factory implements IRenderFactory<EntityGolemBase>
	{
		@Override
		public Render<? super EntityGolemBase> createRenderFor(RenderManager manager)
		{
			return new RenderEntityGolem(manager);
		}
	}
}