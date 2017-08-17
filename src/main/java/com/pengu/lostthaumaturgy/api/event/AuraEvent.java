package com.pengu.lostthaumaturgy.api.event;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;

public class AuraEvent extends Event
{
	public final AtmosphereChunk si;
	public final World world;
	
	public AuraEvent(AtmosphereChunk si, World world)
	{
		this.si = si;
		this.world = world;
	}
	
	public static class Generate extends AuraEvent
	{
		public final Random rand;
		
		public Generate(AtmosphereChunk si, World world, Random rand)
		{
			super(si, world);
			this.rand = rand;
		}
	}
	
	public static class Update extends AuraEvent
	{
		public Update(AtmosphereChunk si, World world)
		{
			super(si, world);
		}
	}
}