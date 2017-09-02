package com.pengu.lostthaumaturgy.core.block.wood.silverwood;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockSilverwoodPlanks extends Block
{
	public BlockSilverwoodPlanks()
	{
		super(Material.WOOD, MapColor.SNOW);
		setUnlocalizedName("silverwood_planks");
		setHardness(2.0F);
		setResistance(5.0F);
		setSoundType(SoundType.WOOD);
	}
}