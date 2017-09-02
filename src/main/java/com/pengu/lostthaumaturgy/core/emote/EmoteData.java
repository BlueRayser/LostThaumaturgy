package com.pengu.lostthaumaturgy.core.emote;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;

public class EmoteData
{
	protected final String id;
	protected final int world;
	protected double x, y, z;
	protected float red = 1, green = 1, blue = 1, alpha = 1, scale = 1;
	protected int fadeIn = 5, stay = 10, fadeOut = 5;
	
	public EmoteData(int world, double x, double y, double z, String id)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}
	
	public String getId()
	{
		return id;
	}
	
	public int getWorld()
	{
		return world;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
	
	public float getAlpha()
	{
		return alpha;
	}
	
	public float getBlue()
	{
		return blue;
	}
	
	public float getGreen()
	{
		return green;
	}
	
	public float getRed()
	{
		return red;
	}
	
	public float getScale()
	{
		return scale;
	}
	
	public int getStay()
	{
		return stay;
	}
	
	public int getFadeIn()
	{
		return fadeIn;
	}
	
	public int getFadeOut()
	{
		return fadeOut;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Id", id);
		nbt.setInteger("World", world);
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
		nbt.setFloat("Red", red);
		nbt.setFloat("Green", green);
		nbt.setFloat("Blue", blue);
		nbt.setFloat("Alpha", alpha);
		nbt.setInteger("FadeIn", fadeIn);
		nbt.setInteger("Stay", stay);
		nbt.setInteger("FadeOut", fadeOut);
		nbt.setFloat("Scale", scale);
		return nbt;
	}
	
	@Nonnull
	public static EmoteData readFromNBT(NBTTagCompound nbt)
	{
		EmoteData data = new EmoteData(nbt.getInteger("World"), nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"), nbt.getString("Id"));
		data.red = nbt.getFloat("Red");
		data.green = nbt.getFloat("Green");
		data.blue = nbt.getFloat("Blue");
		data.alpha = nbt.getFloat("Alpha");
		data.fadeIn = nbt.getInteger("FadeIn");
		data.stay = nbt.getInteger("Stay");
		data.fadeOut = nbt.getInteger("FadeOut");
		data.scale = nbt.getFloat("Scale");
		return data;
	}
}