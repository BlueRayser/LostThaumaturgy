package com.pengu.lostthaumaturgy.client.render.entity;

import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import com.pengu.lostthaumaturgy.client.render.entity.layer.LayerThaumSlimeGel;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.entity.EntityThaumSlime;

public class RenderEntityThaumSlime extends RenderLiving<EntityThaumSlime>
{
	public static final Factory FACTORY = new Factory();
	private static final ResourceLocation TSLIME_TEXTURES = new ResourceLocation(Info.MOD_ID, "textures/entity/thaum_slime.png");
	private static final ResourceLocation TAINTED_SLIME_TEXTURES = new ResourceLocation(Info.MOD_ID, "textures/entity/tainted_slime.png");
	
	public RenderEntityThaumSlime(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelSlime(16), .25F);
		addLayer(new LayerThaumSlimeGel(this));
	}
	
	@Override
	public void doRender(EntityThaumSlime entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.shadowSize = 0.25F * (float) entity.getSlimeSize();
		bindTexture(getEntityTexture(entity));
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected void preRenderCallback(EntityThaumSlime entitylivingbaseIn, float partialTickTime)
	{
		int size = entitylivingbaseIn.getSlimeSize();
		GlStateManager.scale(0.5F * size, 0.5F * size, 0.5F * size);
		GlStateManager.scale(1.5, 1.5, 1.5);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityThaumSlime entity)
	{
		return entity.isTainted() ? TAINTED_SLIME_TEXTURES : TSLIME_TEXTURES;
	}
	
	public static class Factory implements IRenderFactory<EntityThaumSlime>
	{
		@Override
		public Render<? super EntityThaumSlime> createRenderFor(RenderManager manager)
		{
			return new RenderEntityThaumSlime(manager);
		}
	}
}