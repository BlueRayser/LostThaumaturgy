package com.pengu.lostthaumaturgy.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCrystal extends ModelBase
{
	ModelRenderer Crystal;
	
	public ModelCrystal()
	{
		textureWidth = 64;
		textureHeight = 32;
		Crystal = new ModelRenderer(this, 0, 0);
		Crystal.addBox(-16.0f, -16.0f, 0.0f, 16, 16, 16);
		Crystal.setRotationPoint(0.0f, 32.0f, 0.0f);
		Crystal.setTextureSize(64, 32);
		Crystal.mirror = true;
		setRotation(Crystal, 0.7071f, 0.0f, 0.7071f);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, null);
		Crystal.render(f5);
	}
	
	public void render()
	{
		Crystal.render(0.0625f);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}