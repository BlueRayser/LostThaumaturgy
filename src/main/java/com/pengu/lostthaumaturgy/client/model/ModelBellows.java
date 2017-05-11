package com.pengu.lostthaumaturgy.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBellows extends ModelBase
{
	public ModelRenderer BottomPlank, MiddlePlank, TopPlank, Bag, Nozzle;
	
	public ModelBellows()
	{
		textureWidth = 128;
		textureHeight = 64;
		
		BottomPlank = new ModelRenderer(this, 0, 0);
		BottomPlank.addBox(-6.0f, 0.0f, -6.0f, 12, 2, 12);
		BottomPlank.setRotationPoint(0.0f, 22.0f, 0.0f);
		BottomPlank.setTextureSize(128, 64);
		BottomPlank.mirror = true;
		setRotation(BottomPlank, 0.0f, 0.0f, 0.0f);
		
		MiddlePlank = new ModelRenderer(this, 0, 0);
		MiddlePlank.addBox(-6.0f, -1.0f, -6.0f, 12, 2, 12);
		MiddlePlank.setRotationPoint(0.0f, 16.0f, 0.0f);
		MiddlePlank.setTextureSize(128, 64);
		MiddlePlank.mirror = true;
		setRotation(MiddlePlank, 0.0f, 0.0f, 0.0f);
		
		TopPlank = new ModelRenderer(this, 0, 0);
		TopPlank.addBox(-6.0f, 0.0f, -6.0f, 12, 2, 12);
		TopPlank.setRotationPoint(0.0f, 8.0f, 0.0f);
		TopPlank.setTextureSize(128, 64);
		TopPlank.mirror = true;
		setRotation(TopPlank, 0.0f, 0.0f, 0.0f);
		
		Bag = new ModelRenderer(this, 48, 0);
		Bag.addBox(-10.0f, -12.03333f, -10.0f, 20, 24, 20);
		Bag.setRotationPoint(0.0f, 16.0f, 0.0f);
		Bag.setTextureSize(64, 32);
		Bag.mirror = true;
		setRotation(Bag, 0.0f, 0.0f, 0.0f);
		
		Nozzle = new ModelRenderer(this, 0, 36);
		Nozzle.addBox(-2.0f, -2.0f, 0.0f, 4, 4, 2);
		Nozzle.setRotationPoint(0.0f, 16.0f, 6.0f);
		Nozzle.setTextureSize(128, 64);
		Nozzle.mirror = true;
		setRotation(Nozzle, 0.0f, 0.0f, 0.0f);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, null);
		BottomPlank.render(f5);
		MiddlePlank.render(f5);
		TopPlank.render(f5);
		Bag.render(f5);
		Nozzle.render(f5);
	}
	
	public void render()
	{
		MiddlePlank.render(0.0625f);
		Nozzle.render(0.0625f);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}