package com.pengu.lostthaumaturgy.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import org.lwjgl.opengl.GL11;

public class ModelGenCore extends ModelBase
{
	private ModelRenderer cube, outer;
	
	public ModelGenCore()
	{
		outer = new ModelRenderer(this, "glass");
		outer.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
		
		cube = new ModelRenderer(this, "cube");
		cube.setTextureOffset(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
	}
	
	public void render(float f, float f1, float f2, float f3, float f4, float f5)
	{
		GL11.glPushMatrix();
		GL11.glScalef(0.8f, 0.8f, 0.8f);
		GL11.glTranslatef(0.0f, -1.0f, 0.0f);
		GL11.glRotatef(f1, 0.0f, 1.0f, 0.0f);
		GL11.glRotatef(60.0f, 0.7071f, 0.0f, 0.7071f);
		outer.render(f5);
		float f6 = 0.75f;
		GL11.glScalef(f6, f6, f6);
		GL11.glRotatef(60.0f, 0.7071f, 0.0f, 0.7071f);
		GL11.glRotatef(f1, 0.0f, 1.0f, 0.0f);
		outer.render(f5);
		GL11.glScalef(f6, f6, f6);
		GL11.glRotatef(60.0f, 0.7071f, 0.0f, 0.7071f);
		GL11.glRotatef(f1, 0.0f, 1.0f, 0.0f);
		cube.render(f5);
		GL11.glPopMatrix();
	}
}