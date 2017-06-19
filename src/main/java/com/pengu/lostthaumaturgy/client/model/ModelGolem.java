package com.pengu.lostthaumaturgy.client.model;

import java.awt.Color;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import org.lwjgl.opengl.GL11;

import com.pengu.lostthaumaturgy.entity.EntityGolemBase;

public class ModelGolem extends ModelBase
{
	public ModelRenderer golemHead;
	public ModelRenderer golemHeadSmart;
	public ModelRenderer golemHeadObserver;
	public ModelRenderer golemMarker;
	public ModelRenderer golemBody;
	public ModelRenderer golemRightArm;
	public ModelRenderer golemLeftArm;
	public ModelRenderer golemRightArmStrong;
	public ModelRenderer golemLeftArmStrong;
	public ModelRenderer golemRightLeg;
	public ModelRenderer golemLeftLeg;
	public ModelRenderer golemRightLegFast;
	public ModelRenderer golemLeftLegFast;
	public int pass = 0;
	
	public ModelGolem(boolean p)
	{
		float f1 = 0.0f;
		float f2 = p ? -5.0f : 30.0f;
		int var3 = 128;
		int var4 = 128;
		this.golemHead = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemHead.setRotationPoint(0.0f, 0.0f + f2, -2.0f);
		this.golemHead.setTextureOffset(0, 0).addBox(-4.0f, -11.0f, -5.5f, 8, 9, 8, f1);
		this.golemHeadSmart = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemHeadSmart.setRotationPoint(0.0f, 0.0f + f2, -2.0f);
		this.golemHeadSmart.setTextureOffset(0, 96).addBox(-4.0f, -13.0f, -5.5f, 8, 11, 8, f1);
		this.golemHeadObserver = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemHeadObserver.setRotationPoint(0.0f, 0.0f + f2, -2.0f);
		this.golemHeadObserver.setTextureOffset(0, 17).addBox(-4.0f, -11.0f, -5.5f, 8, 9, 8, f1);
		this.golemBody = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemBody.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
		this.golemBody.setTextureOffset(0, 40).addBox(-8.0f, -2.0f, -6.0f, 16, 12, 11, f1);
		this.golemBody.setTextureOffset(0, 70).addBox(-4.5f, 10.0f, -3.0f, 9, 5, 6, f1 + 0.5f);
		this.golemMarker = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemMarker.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
		this.golemMarker.setTextureOffset(0, 88).addBox(-2.0f, 3.0f, -7.0f, 4, 6, 1, f1);
		this.golemRightArm = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemRightArm.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
		this.golemRightArm.setTextureOffset(60, 21).addBox(-12.0f, -2.5f, -3.0f, 4, 25, 6, f1);
		this.golemLeftArm = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemLeftArm.mirror = true;
		this.golemLeftArm.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
		this.golemLeftArm.setTextureOffset(60, 21).addBox(8.0f, -2.5f, -3.0f, 4, 25, 6, f1);
		this.golemRightArmStrong = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemRightArmStrong.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
		this.golemRightArmStrong.setTextureOffset(60, 58).addBox(-12.0f, -2.5f, -3.0f, 4, 25, 6, f1);
		this.golemLeftArmStrong = new ModelRenderer((ModelBase) this).setTextureSize(var3, var4);
		this.golemLeftArmStrong.mirror = true;
		this.golemLeftArmStrong.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
		this.golemLeftArmStrong.setTextureOffset(60, 58).addBox(8.0f, -2.5f, -3.0f, 4, 25, 6, f1);
		this.golemRightLeg = new ModelRenderer((ModelBase) this, 0, 22).setTextureSize(var3, var4);
		this.golemRightLeg.setRotationPoint(-4.0f, 18.0f + f2, 0.0f);
		this.golemRightLeg.setTextureOffset(37, 0).addBox(-3.5f, -3.0f, -3.0f, 6, 16, 5, f1);
		this.golemLeftLeg = new ModelRenderer((ModelBase) this, 0, 22).setTextureSize(var3, var4);
		this.golemLeftLeg.mirror = true;
		this.golemLeftLeg.setTextureOffset(37, 0).setRotationPoint(5.0f, 18.0f + f2, 0.0f);
		this.golemLeftLeg.addBox(-3.5f, -3.0f, -3.0f, 6, 16, 5, f1);
		this.golemRightLegFast = new ModelRenderer((ModelBase) this, 0, 22).setTextureSize(var3, var4);
		this.golemRightLegFast.setRotationPoint(-4.0f, 18.0f + f2, 0.0f);
		this.golemRightLegFast.setTextureOffset(60, 0).addBox(-3.5f, -3.0f, -3.0f, 6, 16, 5, f1);
		this.golemLeftLegFast = new ModelRenderer((ModelBase) this, 0, 22).setTextureSize(var3, var4);
		this.golemLeftLegFast.mirror = true;
		this.golemLeftLegFast.setTextureOffset(60, 0).setRotationPoint(5.0f, 18.0f + f2, 0.0f);
		this.golemLeftLegFast.addBox(-3.5f, -3.0f, -3.0f, 6, 16, 5, f1);
	}
	
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7);
		EntityGolemBase en = (EntityGolemBase) par1Entity;
		GL11.glPushMatrix();
		if(this.pass == 2)
		{
			GL11.glEnable((int) 3042);
			GL11.glBlendFunc((int) 770, (int) 771);
		}
		GL11.glScaled((double) 0.4, (double) 0.4, (double) 0.4);
		if(en.getGolemTypeForDisplay() == 2)
		{
			this.golemHeadSmart.render(par7);
		} else if(en.getGolemTypeForDisplay() == 3)
		{
			this.golemHeadObserver.render(par7);
		} else
		{
			this.golemHead.render(par7);
		}
		short var5 = en.getColor();
		if(var5 >= 0)
		{
//			Color c = new Color(BlockMarkerItem.colors[var5]);
//			float r = (float) c.getRed() / 255.0f;
//			float g = (float) c.getGreen() / 255.0f;
//			float b = (float) c.getBlue() / 255.0f;
//			GL11.glColor3f((float) r, (float) g, (float) b);
//			this.golemMarker.render(par7);
			GL11.glColor3f((float) 1.0f, (float) 1.0f, (float) 1.0f);
		}
		this.golemBody.render(par7);
		if(en.getGolemTypeForDisplay() == 1)
		{
			this.golemRightLegFast.render(par7);
			this.golemLeftLegFast.render(par7);
		} else
		{
			this.golemRightLeg.render(par7);
			this.golemLeftLeg.render(par7);
		}
		if(en.getGolemTypeForDisplay() == 4)
		{
			this.golemRightArmStrong.render(par7);
			this.golemLeftArmStrong.render(par7);
		} else
		{
			this.golemRightArm.render(par7);
			this.golemLeftArm.render(par7);
		}
		if(this.pass == 2)
		{
			GL11.glDisable((int) 3042);
		}
		GL11.glScaled((double) 1.0, (double) 1.0, (double) 1.0);
		GL11.glPopMatrix();
	}
	
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6)
	{
		this.golemHead.rotateAngleY = par4 / 57.295776f;
		this.golemHead.rotateAngleX = par5 / 57.295776f;
		this.golemHeadSmart.rotateAngleY = this.golemHead.rotateAngleY;
		this.golemHeadSmart.rotateAngleX = this.golemHead.rotateAngleX;
		this.golemHeadObserver.rotateAngleY = this.golemHead.rotateAngleY;
		this.golemHeadObserver.rotateAngleX = this.golemHead.rotateAngleX;
		this.golemRightLeg.rotateAngleX = -1.5f * this.func_78172_a(par1, 13.0f) * par2;
		this.golemLeftLeg.rotateAngleX = 1.5f * this.func_78172_a(par1, 13.0f) * par2;
		this.golemRightLeg.rotateAngleY = 0.0f;
		this.golemLeftLeg.rotateAngleY = 0.0f;
		this.golemRightLegFast.rotateAngleX = this.golemRightLeg.rotateAngleX;
		this.golemLeftLegFast.rotateAngleX = this.golemLeftLeg.rotateAngleX;
		this.golemRightLegFast.rotateAngleY = 0.0f;
		this.golemLeftLegFast.rotateAngleY = 0.0f;
	}
	
	public void setLivingAnimations(EntityLiving par1EntityLiving, float par2, float par3, float par4)
	{
		EntityGolemBase var5 = (EntityGolemBase) par1EntityLiving;
		int var6 = var5.getActionTimer();
		if(var6 > 0)
		{
			this.golemRightArm.rotateAngleX = -2.0f + 1.5f * this.func_78172_a((float) var6 - par4, 10.0f);
			this.golemLeftArm.rotateAngleX = -2.0f + 1.5f * this.func_78172_a((float) var6 - par4, 10.0f);
		} else if(var5.leftArm > 0)
		{
			this.golemLeftArm.rotateAngleX = -2.0f + 1.5f * this.func_78172_a((float) var5.leftArm - par4, 10.0f);
		} else if(!var5.getCarriedForDisplay().isEmpty())
		{
			this.golemRightArm.rotateAngleX = -1.0f;
			this.golemLeftArm.rotateAngleX = -1.0f;
		} else
		{
			this.golemRightArm.rotateAngleX = (-0.2f + 1.5f * this.func_78172_a(par2, 13.0f)) * par3;
			this.golemLeftArm.rotateAngleX = (-0.2f - 1.5f * this.func_78172_a(par2, 13.0f)) * par3;
		}
		this.golemRightArmStrong.rotateAngleX = this.golemRightArm.rotateAngleX;
		this.golemLeftArmStrong.rotateAngleX = this.golemLeftArm.rotateAngleX;
	}
	
	private float func_78172_a(float par1, float par2)
	{
		return (Math.abs(par1 % par2 - par2 * 0.5f) - par2 * 0.25f) / (par2 * 0.25f);
	}
}