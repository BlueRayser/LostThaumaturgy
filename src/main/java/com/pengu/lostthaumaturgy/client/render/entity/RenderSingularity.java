package com.pengu.lostthaumaturgy.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import com.pengu.lostthaumaturgy.entity.EntityCustomSplashPotion;
import com.pengu.lostthaumaturgy.entity.EntitySingularity;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class RenderSingularity extends RenderSnowball<EntitySingularity>
{
	public static final Factory FACTORY = new Factory();
	
	public RenderSingularity(RenderManager renderManagerIn)
	{
		super(renderManagerIn, ItemsLT.SINGULARITY, Minecraft.getMinecraft().getRenderItem());
	}
	
	@Override
	public void doRender(EntitySingularity entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if(entity.fuse > 0)
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	public static final class Factory implements IRenderFactory<EntitySingularity>
	{
		@Override
		public Render<? super EntitySingularity> createRenderFor(RenderManager manager)
		{
			return new RenderSingularity(manager);
		}
	}
}