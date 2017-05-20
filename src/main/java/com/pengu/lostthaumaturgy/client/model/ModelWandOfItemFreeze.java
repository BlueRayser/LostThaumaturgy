package com.pengu.lostthaumaturgy.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWandOfItemFreeze extends ModelBase
{
	public ModelRenderer wandBase;
	public ModelRenderer branch1, branch2, branch3, branch4;
	public ModelRenderer off1, off2, off3, off4;
	public ModelRenderer conn;
	public ModelRenderer in1, in2, in3, in4;
	
	public ModelWandOfItemFreeze()
	{
		textureWidth = 64;
		textureHeight = 32;
		off4 = new ModelRenderer(this, 50, 0);
		off4.setRotationPoint(-3.4F, 9.4F, -1.0F);
		off4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 2, 0.0F);
		setRotateAngle(off4, 0.0F, 0.0F, -0.5235987755982988F);
		in4 = new ModelRenderer(this, 36, 4);
		in4.setRotationPoint(-3.2F, 9.28F, -1.0F);
		in4.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		setRotateAngle(in4, 0.0F, 0.0F, -0.5235987755982988F);
		branch1 = new ModelRenderer(this, 8, 0);
		branch1.setRotationPoint(-1.0F, 11.5F, -2.0F);
		branch1.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
		off3 = new ModelRenderer(this, 44, 0);
		off3.setRotationPoint(2.54F, 8.9F, -1.0F);
		off3.addBox(0.0F, 0.0F, 0.0F, 1, 3, 2, 0.0F);
		setRotateAngle(off3, 0.0F, 0.0F, 0.5235987755982988F);
		branch3 = new ModelRenderer(this, 20, 0);
		branch3.setRotationPoint(1.0F, 11.5F, -1.0F);
		branch3.addBox(0.0F, 0.0F, 0.0F, 1, 2, 2, 0.0F);
		branch4 = new ModelRenderer(this, 26, 0);
		branch4.setRotationPoint(-2.0F, 11.5F, -1.0F);
		branch4.addBox(0.0F, 0.0F, 0.0F, 1, 2, 2, 0.0F);
		off1 = new ModelRenderer(this, 32, 0);
		off1.setRotationPoint(-1.0F, 9.4F, -3.4F);
		off1.addBox(0.0F, 0.0F, 0.0F, 2, 3, 1, 0.0F);
		setRotateAngle(off1, 0.5235987755982988F, 0.0F, 0.0F);
		in1 = new ModelRenderer(this, 54, 3);
		in1.setRotationPoint(-1.0F, 8.24F, 1.4F);
		in1.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		setRotateAngle(in1, -0.5235987755982988F, 0.0F, 0.0F);
		in3 = new ModelRenderer(this, 28, 4);
		in3.setRotationPoint(1.5F, 8.3F, -1.0F);
		in3.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		setRotateAngle(in3, 0.0F, 0.0F, 0.5235987755982988F);
		branch2 = new ModelRenderer(this, 14, 0);
		branch2.setRotationPoint(-1.0F, 11.5F, 1.0F);
		branch2.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
		in2 = new ModelRenderer(this, 20, 4);
		in2.setRotationPoint(-1.0F, 9.17F, -3.0F);
		in2.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		setRotateAngle(in2, 0.5235987755982988F, 0.0F, 0.0F);
		off2 = new ModelRenderer(this, 38, 0);
		off2.setRotationPoint(-1.0F, 8.9F, 2.54F);
		off2.addBox(0.0F, 0.0F, 0.0F, 2, 3, 1, 0.0F);
		setRotateAngle(off2, -0.5235987755982988F, 0.0F, 0.0F);
		wandBase = new ModelRenderer(this, 0, 0);
		wandBase.setRotationPoint(-1.0F, 12.0F, -1.0F);
		wandBase.addBox(0.0F, 0.0F, 0.0F, 2, 12, 2, 0.0F);
		conn = new ModelRenderer(this, 8, 3);
		conn.setRotationPoint(-1.5F, 12.01F, -1.5F);
		conn.addBox(0.0F, 0.0F, 0.0F, 3, 2, 3, 0.0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		off4.render(f5);
		in4.render(f5);
		branch1.render(f5);
		off3.render(f5);
		branch3.render(f5);
		branch4.render(f5);
		off1.render(f5);
		in1.render(f5);
		in3.render(f5);
		branch2.render(f5);
		in2.render(f5);
		off2.render(f5);
		wandBase.render(f5);
		conn.render(f5);
	}
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
