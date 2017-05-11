package com.pengu.lostthaumaturgy.client.fx;

import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FXGreenFlame extends ParticleFlame
{
	public FXGreenFlame(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
		particleRed = 0F;
		particleScale = 0.1F;
	}
	
	public FXGreenFlame setScale(float scale)
	{
		particleScale = scale;
		return this;
	}
}