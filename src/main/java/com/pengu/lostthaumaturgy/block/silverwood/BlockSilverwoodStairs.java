package com.pengu.lostthaumaturgy.block.silverwood;

import com.pengu.lostthaumaturgy.init.BlocksLT;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;

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