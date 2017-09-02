package com.pengu.lostthaumaturgy.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import org.lwjgl.opengl.GL11;

import com.pengu.lostthaumaturgy.client.model.ModelTrunk;
import com.pengu.lostthaumaturgy.core.entity.EntityTravelingTrunk;
import com.pengu.lostthaumaturgy.core.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class RenderTravelingTrunk extends RenderLiving<EntityTravelingTrunk>
{
	public static final Factory FACTORY = new Factory();
	
	private final ModelTrunk trunkModel;
	
	protected RenderTravelingTrunk(RenderManager renderManager, ModelTrunk model)
	{
		super(renderManager, model, .01F);
		trunkModel = model;
	}
	
	protected void adjustTrunk(EntityTravelingTrunk entity, float f)
	{
		int i = 2;
		float f1 = (entity.field_767_b + (entity.field_768_a - entity.field_767_b) * f) / (i * .5F + 1);
		float f2 = 1 / (f1 + 1);
		float f3 = i;
		f1 = (float) (f1 / 1.5);
		f2 = (float) (f2 / 1.4);
		f3 = (float) (entity.hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONTAINED_EMPTINESS)) ? (f3 / 1.4) : (f3 / 1.5));
		GL11.glScalef((f2 * f3), (.5F / f2 * f3), (f2 * f3));
		GL11.glTranslatef(-.45F, .45F, -.45F);
		f1 = 1 - entity.lidrot;
		f1 = 1 - f1 * f1 * f1;
		trunkModel.chestLid.rotateAngleX = -f1 * (float) Math.PI / 2F;
	}
	
	@Override
	protected void preRenderCallback(EntityTravelingTrunk entitylivingbaseIn, float partialTickTime)
	{
		adjustTrunk(entitylivingbaseIn, partialTickTime);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityTravelingTrunk entity)
	{
		return entity.texture;
	}
	
	public static class Factory implements IRenderFactory<EntityTravelingTrunk>
	{
		@Override
		public Render<? super EntityTravelingTrunk> createRenderFor(RenderManager manager)
		{
			return new RenderTravelingTrunk(manager, new ModelTrunk());
		}
	}
}