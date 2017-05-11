package com.pengu.lostthaumaturgy.custom.aura.taint;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaintRegistry
{
	private static final List<ITaintHandler> taintHandlers = new ArrayList<>();
	
	static
	{
		registerTaintHandler(new TaintHandlerLostThaumaturgy());
	}
	
	public static boolean registerTaintHandler(ITaintHandler handler)
	{
		if(taintHandlers.contains(handler))
			return false;
		for(ITaintHandler h : taintHandlers)
			if(h.handlerId().equals(handler.handlerId()))
				return false;
		taintHandlers.add(handler);
		return true;
	}
	
	public static boolean canTaintBlock(World world, BlockPos pos)
	{
		for(ITaintHandler handler : taintHandlers)
			if(handler.canTaintBlock(world, pos))
				return true;
		return false;
	}
	
	public static boolean canCureBlock(World world, BlockPos pos)
	{
		for(ITaintHandler handler : taintHandlers)
			if(handler.canCureBlock(world, pos))
				return true;
		return false;
	}
	
	public static boolean taintBlock(World world, BlockPos pos)
	{
		for(ITaintHandler handler : taintHandlers)
			if(handler.canTaintBlock(world, pos))
			{
				boolean tainted = handler.taintBlock(world, pos);
				if(tainted)
					return true;
			}
		return false;
	}
	
	public static boolean cureBlock(World world, BlockPos pos)
	{
		for(ITaintHandler handler : taintHandlers)
			if(handler.canCureBlock(world, pos))
			{
				boolean cured = handler.cureBlock(world, pos);
				if(cured)
					return true;
			}
		return false;
	}
}