package com.pengu.lostthaumaturgy.worldgen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.world.gen.IWorldGenFeature;
import com.pengu.lostthaumaturgy.block.BlockLyingItem;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class WorldGenLostArtifacts implements IWorldGenFeature
{
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return 1024;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return 16;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return 64;
	}
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		if(world.getBlockState(pos.down()).getBlock() == Blocks.STONE && world.isAirBlock(pos))
		{
			EnumMultiMaterialType[] artifacts = { EnumMultiMaterialType.ANCIENT_POTTERY, EnumMultiMaterialType.TARNISHED_CHALICE, EnumMultiMaterialType.WORN_STATUETTE, EnumMultiMaterialType.ANCIENT_SEAL, EnumMultiMaterialType.ANCIENT_WEAPON };
			ItemStack stack = artifacts[rand.nextInt(artifacts.length)].stack();
			if(rand.nextInt(5) == 0)
				stack = EnumMultiMaterialType.ANCIENT_STONE_TABLET.stack();
			BlockLyingItem.place(world, pos, stack);
		}
	}
}