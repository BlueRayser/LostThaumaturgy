package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCinnabarOre extends Block
{
	public BlockCinnabarOre()
    {
		super(Material.ROCK);
		setUnlocalizedName("cinnabar_ore");
		setSoundType(SoundType.STONE);
		setHardness(1.5F);
		setHarvestLevel("pickaxe", 2);
		setResistance(5);
    }
}