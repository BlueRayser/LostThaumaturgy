package com.pengu.lostthaumaturgy.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkUtils
{
	public static BlockPos getChunkPos(int chunkX, int chunkZ, int offX, int offY, int offZ)
	{
		int cOffX = chunkX < 0 ? -offX : offX;
		int cOffZ = chunkZ < 0 ? -offZ : offZ;
		return new BlockPos(chunkX * 16 + cOffX, offY, chunkZ * 16 + cOffZ);
	}
	
	public static BlockPos getChunPos(Chunk c, BlockPos off)
	{
		return getChunkPos(c.x, c.z, off.getX(), off.getY(), off.getZ());
	}
}