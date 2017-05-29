package com.pengu.lostthaumaturgy.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDuplicator extends ModelBase
{
	ModelRenderer press1, piston1;
	
	public ModelDuplicator()
	{
		textureWidth = 64;
		textureHeight = 32;
		press1 = new ModelRenderer(this, 0, 0);
		press1.addBox(-4.0f, 0.0f, -4.0f, 8, 4, 8);
		press1.setRotationPoint(0.0f, 12.0f, 0.0f);
		press1.setTextureSize(64, 32);
		press1.mirror = true;
		setRotation(press1, 0.0f, 0.0f, 0.0f);
		
		piston1 = new ModelRenderer(this, 0, 12);
		piston1.addBox(-2.0f, 0.0f, -2.0f, 4, 4, 4);
		piston1.setRotationPoint(0.0f, 8.0f, 0.0f);
		piston1.setTextureSize(64, 32);
		piston1.mirror = true;
		setRotation(piston1, 0.0f, 0.0f, 0.0f);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, null);
		press1.render(f5);
		piston1.render(f5);
	}
	
	public void render()
	{
		press1.render(0.0625f);
		piston1.render(0.0625f);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}