package com.pengu.lostthaumaturgy.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGear extends ModelBase
{
	ModelRenderer Shape1, Shape2, Shape3;
	
	public ModelGear()
	{
		textureWidth = 64;
		textureHeight = 32;
		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(-2.0f, -6.0f, -6.0f, 4, 12, 12);
		Shape1.setRotationPoint(0.0f, 0.0f, 0.0f);
		Shape1.setTextureSize(64, 32);
		Shape1.mirror = true;
		
		setRotation(Shape1, 0.0f, 0.0f, 0.0f);
		Shape2 = new ModelRenderer(this, 0, 0);
		Shape2.addBox(-2.0f, -6.0f, -6.0f, 4, 12, 12);
		Shape2.setRotationPoint(0.0f, 0.0f, 0.0f);
		Shape2.setTextureSize(64, 32);
		Shape2.mirror = true;
		
		setRotation(Shape2, 0.5235988f, 0.0f, 0.0f);
		Shape3 = new ModelRenderer(this, 0, 0);
		Shape3.addBox(-2.0f, -6.0f, -6.0f, 4, 12, 12);
		Shape3.setRotationPoint(0.0f, 0.0f, 0.0f);
		Shape3.setTextureSize(64, 32);
		Shape3.mirror = true;
		setRotation(Shape3, -0.5235988f, 0.0f, 0.0f);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, null);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
	}
	
	public void render()
	{
		Shape1.render(0.0625f);
		Shape2.render(0.0625f);
		Shape3.render(0.0625f);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}