package com.pengu.lostthaumaturgy.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import com.pengu.lostthaumaturgy.entity.EntityCustomSplashPotion;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class RenderCustomSplashPotion extends RenderSnowball<EntityCustomSplashPotion>
{
	public static final Factory FACTORY = new Factory();
	
	public RenderCustomSplashPotion(RenderManager renderManagerIn)
	{
		super(renderManagerIn, ItemsLT.CUSTOM_POTION, Minecraft.getMinecraft().getRenderItem());
	}
	
	@Override
	public ItemStack getStackToRender(EntityCustomSplashPotion entityIn)
	{
		ItemStack stack = super.getStackToRender(entityIn);
		stack.setItemDamage(entityIn.getType());
		return stack;
	}
	
	public static final class Factory implements IRenderFactory<EntityCustomSplashPotion>
	{
		@Override
		public Render<? super EntityCustomSplashPotion> createRenderFor(RenderManager manager)
		{
			return new RenderCustomSplashPotion(manager);
		}
	}
}