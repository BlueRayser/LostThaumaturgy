package com.pengu.lostthaumaturgy.client.render.entity.layer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

import com.pengu.lostthaumaturgy.client.render.entity.RenderEntityThaumSlime;
import com.pengu.lostthaumaturgy.entity.EntityThaumSlime;

public class LayerThaumSlimeGel implements LayerRenderer<EntityThaumSlime>
{
	private final RenderEntityThaumSlime slimeRenderer;
	private final ModelBase slimeModel = new ModelSlime(0);
	
	public LayerThaumSlimeGel(RenderEntityThaumSlime slimeRendererIn)
	{
		this.slimeRenderer = slimeRendererIn;
	}
	
	public void doRenderLayer(EntityThaumSlime entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if(!entitylivingbaseIn.isInvisible())
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableNormalize();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
			this.slimeModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
		}
	}
	
	public boolean shouldCombineTextures()
	{
		return true;
	}
}