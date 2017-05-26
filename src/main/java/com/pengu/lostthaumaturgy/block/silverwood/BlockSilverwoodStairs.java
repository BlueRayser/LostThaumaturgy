package com.pengu.lostthaumaturgy.block.silverwood;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;

import com.pengu.lostthaumaturgy.init.BlocksLT;

public class BlockSilverwoodStairs extends BlockStairs
{
	public BlockSilverwoodStairs()
	{
		super(BlocksLT.SILVERWOOD_PLANKS.getDefaultState());
		setUnlocalizedName("silverwood_stairs");
		
		setHardness(2.0F);
		setResistance(5.0F);
		setSoundType(SoundType.WOOD);
	}
}