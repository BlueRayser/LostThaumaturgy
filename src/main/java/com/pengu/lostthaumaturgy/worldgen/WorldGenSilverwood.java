package com.pengu.lostthaumaturgy.worldgen;

import java.util.Random;

import com.pengu.lostthaumaturgy.worldgen.features.FeatureSilverwood;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSilverwood implements IWorldGenerator
{
	/**
	 * Generate some world's silverwood trees.
	 * 
	 * @param random
	 *            the chunk specific {@link Random}.
	 * @param chunkX
	 *            the chunk X coordinate of this chunk.
	 * @param chunkZ
	 *            the chunk Z coordinate of this chunk.
	 * @param world
	 *            : additionalData[0] The minecraft {@link World} we're
	 *            generating for.
	 * @param chunkGenerator
	 *            : additionalData[1] The {@link IChunkProvider} that is
	 *            generating.
	 * @param chunkProvider
	 *            : additionalData[2] {@link IChunkProvider} that is requesting
	 *            the world generation.
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(random.nextInt(750) < 40)
			for(int i = 0; i < random.nextInt(2); i++)
			{
				int x = chunkX * 16 + random.nextInt(16);
				int z = chunkZ * 16 + random.nextInt(16);
				BlockPos pos = world.getHeight(new BlockPos(x, 255, z));
				
				//Prevent tree bugs
				new FeatureSilverwood().generate(world, random, pos);
			}
	}
}