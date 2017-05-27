package com.pengu.lostthaumaturgy.client.model;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;

public class ModelTinyPlayer extends ModelPlayer
{
	public ModelTinyPlayer()
	{
		super(0.0F, false);
		isChild = true;
	}
	
	public void renderAll(float progress)
	{
		this.bipedRightArm.rotateAngleX = progress;
		float scale = 0.0625F;
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
		bipedHead.render(scale);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
		bipedBody.render(scale);
		bipedRightArm.render(scale);
		bipedLeftArm.render(scale);
		bipedRightLeg.render(scale);
		bipedLeftLeg.render(scale);
		bipedHeadwear.render(scale);
		GlStateManager.popMatrix();
	}
}