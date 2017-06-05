package com.pengu.lostthaumaturgy.utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundObject
{
	public ResourceLocation name;
	public SoundEvent sound;
	
	public SoundObject(ResourceLocation name)
	{
		this.name = name;
	}
}