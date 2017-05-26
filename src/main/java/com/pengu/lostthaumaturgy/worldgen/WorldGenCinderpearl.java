package com.pengu.lostthaumaturgy.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class WorldGenCinderpearl implements IWorldGenerator
{
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(rand.nextInt(200) < 40)
			for(int k = 0; k < rand.nextInt(2); ++k)
			{
				BlockPos pos = world.getHeight(new BlockPos(chunkX + rand.nextInt(16), 255, chunkZ + rand.nextInt(16)));
				if(AuraTicker.BIOME_FIREFLOWER.contains(world.getBiome(pos)))
				{
					int flowerCount = rand.nextInt(8);
					
					for(int j = 0; j < flowerCount; ++j)
					{
						BlockPos tpos = new BlockPos(pos.getX() + offset(rand, 6), pos.getY() + offset(rand, 6), pos.getZ() + offset(rand, 6));
						
						for(int t = 0; t < 25; ++t)
						{
							BlockPos gpos = posFromTop(tpos, world, GRASS_OR_DIRT_OR_SAND);
							if(gpos != null && world.getBlockState(gpos.up()).getBlock().isReplaceable(world, gpos.up()))
							{
								world.setBlockState(gpos.up(), BlocksLT.CINDERPEARL.getDefaultState());
								break;
							}
						}
					}
				}
			}
	}
	
	private int offset(Random rand, int rad)
	{
		return rand.nextInt(rad) - rand.nextInt(rad);
	}
	
	public static final Predicate<IBlockState> GRASS_OR_DIRT_OR_SAND = Predicates.or(Predicates.equalTo(Blocks.GRASS.getDefaultState()), Predicates.equalTo(Blocks.DIRT.getDefaultState()), Predicates.equalTo(Blocks.SAND.getDefaultState()));
	
	private BlockPos posFromTop(BlockPos pos, World world, Predicate<IBlockState> state)
	{
		pos = world.getHeight(pos);
		while(pos.getY() > 0 && world.isBlockLoaded(pos))
			if(state.apply(world.getBlockState(pos)))
				return pos;
			else
				pos = pos.down();
		return null;
	}
}