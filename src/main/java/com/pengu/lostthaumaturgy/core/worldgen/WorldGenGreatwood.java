package com.pengu.lostthaumaturgy.core.worldgen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.pengu.hammercore.utils.ChunkUtils;
import com.pengu.lostthaumaturgy.core.worldgen.features.FeatureGreatwood;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;

public class WorldGenGreatwood implements IWorldGenerator
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
		if(random.nextInt(250) < 40)
			for(int i = 0; i < random.nextInt(2); i++)
			{
				AtmosphereChunk si = AtmosphereTicker.getAuraChunkFromChunkCoords(world, chunkX, chunkZ);
				
				BlockPos pos = world.getHeight(ChunkUtils.getChunkPos(chunkX, chunkZ, random.nextInt(16), 255, random.nextInt(16)));
				
				if(pos.getY() < 40)
					continue;
				
				// Prevent tree bugs
				if(new FeatureGreatwood().generate(world, random, pos))
				{
					for(int x = -2; x < 3; ++x)
						for(int z = -2; z < 3; ++z)
						{
							double distance = Math.sqrt(x * x + z * z);
							si = AtmosphereTicker.getAuraChunkFromChunkCoords(world, chunkX + x, chunkZ + z);
							if(si != null)
								si.vis = (short) Math.max(4000 + random.nextInt(500) + random.nextInt(1500) / distance, si.vis);
						}
					break;
				}
			}
	}
}