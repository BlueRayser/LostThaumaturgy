package com.pengu.lostthaumaturgy.custom.aura.taint;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITaintHandler
{
	/** Taint process */
	boolean canTaintBlock(World world, BlockPos pos);
	
	boolean taintBlock(World world, BlockPos pos);
	
	/** Purification process */
	boolean canCureBlock(World world, BlockPos pos);
	
	boolean cureBlock(World world, BlockPos pos);
	
	/** Used to prevent duplications */
	String handlerId();
}