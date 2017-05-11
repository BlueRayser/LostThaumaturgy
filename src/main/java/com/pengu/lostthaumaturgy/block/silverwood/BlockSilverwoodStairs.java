package com.pengu.lostthaumaturgy.block.silverwood;

import com.pengu.lostthaumaturgy.init.BlocksLT;

import net.minecraft.block.BlockStairs;

public class BlockSilverwoodStairs extends BlockStairs
{
	public BlockSilverwoodStairs()
    {
		super(BlocksLT.SILVERWOOD_PLANKS.getDefaultState());
		setUnlocalizedName("silverwood_stairs");
    }
}