package com.pengu.lostthaumaturgy.worldgen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.pengu.hammercore.utils.ChunkUtils;
import com.pengu.lostthaumaturgy.worldgen.features.FeatureSilverwood;

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
				BlockPos pos = world.getHeight(ChunkUtils.getChunkPos(chunkX, chunkZ, random.nextInt(16), 255, random.nextInt(16)));
				
				if(pos.getY() < 40)
					continue;
				
				// Prevent tree bugs
				if(new FeatureSilverwood().generate(world, random, pos))
					break;
			}
	}
}