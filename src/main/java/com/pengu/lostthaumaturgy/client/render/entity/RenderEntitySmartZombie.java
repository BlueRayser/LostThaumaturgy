package com.pengu.lostthaumaturgy.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.entity.EntitySmartZombie;

public class RenderEntitySmartZombie extends RenderZombie
{
	public static final Factory FACTORY = new Factory();
	private static final ResourceLocation SMART_ZOMBIE_TEXTURES = new ResourceLocation(LTInfo.MOD_ID, "textures/entity/smart_zombie.png");
	
	public RenderEntitySmartZombie(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityZombie entity)
	{
		return SMART_ZOMBIE_TEXTURES;
	}
	
	public static class Factory implements IRenderFactory<EntitySmartZombie>
	{
		@Override
		public Render<? super EntitySmartZombie> createRenderFor(RenderManager manager)
		{
			return new RenderEntitySmartZombie(manager);
		}
	}
}