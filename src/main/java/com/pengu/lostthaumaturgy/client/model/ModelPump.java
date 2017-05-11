package com.pengu.lostthaumaturgy.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPump extends ModelBase
{
	public final ModelRenderer Front;
	public final ModelRenderer MoveBase;
	public final ModelRenderer MoveFrill;
	public final ModelRenderer Center;
	public final ModelRenderer Back;
	
	public ModelPump()
	{
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.Front = new ModelRenderer(this, 0, 0);
		this.Front.addBox(0.0f, 0.0f, 0.0f, 12, 6, 12);
		this.Front.setRotationPoint(-6.0f, 18.0f, -6.0f);
		this.Front.setTextureSize(128, 64);
		this.Front.mirror = true;
		this.setRotation(this.Front, 0.0f, 0.0f, 0.0f);
		this.MoveBase = new ModelRenderer(this, 0, 18);
		this.MoveBase.addBox(0.0f, 0.0f, 0.0f, 12, 2, 12);
		this.MoveBase.setRotationPoint(-6.0f, 8.0f, -6.0f);
		this.MoveBase.setTextureSize(128, 64);
		this.MoveBase.mirror = true;
		this.setRotation(this.MoveBase, 0.0f, 0.0f, 0.0f);
		this.MoveFrill = new ModelRenderer(this, 0, 32);
		this.MoveFrill.addBox(0.0f, 0.0f, 0.0f, 10, 4, 10);
		this.MoveFrill.setRotationPoint(-5.0f, 10.0f, -5.0f);
		this.MoveFrill.setTextureSize(128, 64);
		this.MoveFrill.mirror = true;
		this.setRotation(this.MoveFrill, 0.0f, 0.0f, 0.0f);
		this.Center = new ModelRenderer(this, 48, 0);
		this.Center.addBox(0.0f, 0.0f, 0.0f, 16, 4, 16);
		this.Center.setRotationPoint(-8.0f, 14.0f, -8.0f);
		this.Center.setTextureSize(128, 64);
		this.Center.mirror = true;
		this.setRotation(this.Center, 0.0f, 0.0f, 0.0f);
		this.Back = new ModelRenderer(this, 0, 46);
		this.Back.addBox(0.0f, 0.0f, 0.0f, 6, 6, 6);
		this.Back.setRotationPoint(-3.0f, 8.0f, -3.0f);
		this.Back.setTextureSize(128, 64);
		this.Back.mirror = true;
		this.setRotation(this.Back, 0.0f, 0.0f, 0.0f);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		this.Front.render(f5);
		this.MoveBase.render(f5);
		this.MoveFrill.render(f5);
		this.Center.render(f5);
		this.Back.render(f5);
	}
	
	public void render()
	{
		this.Front.render(0.0625f);
		this.Center.render(0.0625f);
		this.Back.render(0.0625f);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
	}
}