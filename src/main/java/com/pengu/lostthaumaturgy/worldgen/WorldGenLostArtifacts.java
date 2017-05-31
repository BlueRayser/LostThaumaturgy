package com.pengu.lostthaumaturgy.worldgen;

import java.util.ArrayList;
import java.util.List;
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
			List<ItemStack> artifacts = new ArrayList<>();
			
			EnumMultiMaterialType[] lost_artifacts = { EnumMultiMaterialType.ANCIENT_POTTERY, EnumMultiMaterialType.TARNISHED_CHALICE, EnumMultiMaterialType.WORN_STATUETTE, EnumMultiMaterialType.ANCIENT_SEAL, EnumMultiMaterialType.ANCIENT_WEAPON };
			for(int i = 0; i < 33; ++i)
			{
				artifacts.add(lost_artifacts[rand.nextInt(lost_artifacts.length)].stack());
				if(rand.nextInt(8) == 0)
					artifacts.add(EnumMultiMaterialType.ANCIENT_STONE_TABLET.stack());
			}
			
			EnumMultiMaterialType[] forbidden_artifacts = { EnumMultiMaterialType.CRACKED_WISP_SHELL, EnumMultiMaterialType.DISTORTED_SKULL, EnumMultiMaterialType.INHUMAN_SKULL, EnumMultiMaterialType.DARKENED_CRYSTAL_EYE, EnumMultiMaterialType.KNOTTED_SPIKE };
			for(int i = 0; i < 12; ++i)
			{
				artifacts.add(lost_artifacts[rand.nextInt(lost_artifacts.length)].stack());
				if(rand.nextInt(8) == 0)
					artifacts.add(EnumMultiMaterialType.TOME_FORBIDDEN_KNOWLEDGE.stack());
			}
			
			ItemStack stack = artifacts.get(rand.nextInt(artifacts.size()));
			BlockLyingItem.place(world, pos, stack);
		}
	}
}