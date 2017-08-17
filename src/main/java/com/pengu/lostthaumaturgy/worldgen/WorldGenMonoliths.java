package com.pengu.lostthaumaturgy.worldgen;

import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.pengu.hammercore.utils.ChunkUtils;
import com.pengu.hammercore.world.gen.IWorldGenFeature;
import com.pengu.lostthaumaturgy.block.monolith.BlockMonolithOpener;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;

public class WorldGenMonoliths implements IWorldGenFeature
{
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return 2;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return 0;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return 255;
	}
	
	private static final BlockPos center = new BlockPos(8, 255, 8);
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		pos = world.getHeight(ChunkUtils.getChunkPos(world.getChunkFromBlockCoords(pos), center)).down();
		double dist = Math.sqrt(AtmosphereTicker.getDistanceSqToClosestMonolith(pos));
		if(dist < 500 || pos.getY() < 40 || !world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP))
			return;
		generateSurroundings(world, rand, pos);
		AtmosphereTicker.addMonolith(pos);
		BlockMonolithOpener.buildMonolith(world, pos);
	}
	
	public void generateSurroundings(World world, Random rand, BlockPos pos)
	{
		Biome b = world.getBiome(pos);
		if(b.getTemperature() >= 1.8F)
			genSandStoneSurroundings(world, rand, pos.getX(), pos.getY(), pos.getZ());
		else if(b.getRainfall() >= .8F)
			genMossyStoneSurroundings(world, rand, pos.getX(), pos.getY(), pos.getZ());
		else
			genStoneSurroundings(world, rand, pos.getX(), pos.getY(), pos.getZ());
	}
	
	private void genSandStoneSurroundings(World world, Random random, int x, int y, int z)
	{
		for(int a = 0; a < 75; ++a)
			world.setBlockState(new BlockPos(x + random.nextInt(4) - random.nextInt(4), y, z + random.nextInt(4) - random.nextInt(4)), Blocks.SANDSTONE.getDefaultState());
		for(int side = 0; side < 4; ++side)
		{
			int mx = 0;
			int mz = 0;
			switch(side)
			{
			case 0:
			{
				mz = 2;
				mx = 2;
				break;
			}
			case 1:
			{
				mx = 2;
				mz = -2;
				break;
			}
			case 2:
			{
				mz = -2;
				mx = -2;
				break;
			}
			case 3:
			{
				mx = -2;
				mz = 2;
			}
			}
			for(int a2 = 0; a2 < random.nextInt(5); ++a2)
			{
				int md2 = 0;
				switch(a2)
				{
				case 1:
				case 3:
				{
					md2 = 1;
					break;
				}
				case 2:
				case 4:
				{
					md2 = 2;
				}
				}
				world.setBlockState(new BlockPos(x + mx, y + a2 + 1, z + mz), Blocks.SANDSTONE.getStateFromMeta(md2));
			}
		}
	}
	
	private void genStoneSurroundings(World world, Random random, int x, int y, int z)
	{
		Biome bio = world.getBiome(new BlockPos(x, y, z));
		boolean vines = false;
		if(bio == Biomes.SWAMPLAND || bio == Biomes.JUNGLE)
			vines = true;
		for(int a = 0; a < 75; ++a)
			world.setBlockState(new BlockPos(x + random.nextInt(4) - random.nextInt(4), y, z + random.nextInt(4) - random.nextInt(4)), Blocks.STONEBRICK.getStateFromMeta(random.nextInt(3)));
		for(int side = 0; side < 4; ++side)
		{
			int mx = 0;
			int mz = 0;
			switch(side)
			{
			case 0:
			{
				mz = 2;
				mx = 2;
				break;
			}
			case 1:
			{
				mx = 2;
				mz = -2;
				break;
			}
			case 2:
			{
				mz = -2;
				mx = -2;
				break;
			}
			case 3:
			{
				mx = -2;
				mz = 2;
			}
			}
			for(int a2 = 0; a2 < random.nextInt(5); ++a2)
			{
				world.setBlockState(new BlockPos(x + mx, y + a2 + 1, z + mz), Blocks.STONEBRICK.getStateFromMeta(3));
				
				if(a2 > 1)
				{
					if(vines && random.nextBoolean())
						world.setBlockState(new BlockPos(x + mx + 1, y + a2 + 1, z + mz), Blocks.VINE.getStateFromMeta(2));
					if(vines && random.nextBoolean())
						world.setBlockState(new BlockPos(x + mx - 1, y + a2 + 1, z + mz), Blocks.VINE.getStateFromMeta(8));
					if(vines && random.nextBoolean())
						world.setBlockState(new BlockPos(x + mx, y + a2 + 1, z + mz + 1), Blocks.VINE.getStateFromMeta(4));
					if(vines && random.nextBoolean())
						world.setBlockState(new BlockPos(x + mx, y + a2 + 1, z + mz - 1), Blocks.VINE.getStateFromMeta(1));
				}
				
				if(a2 != 3)
					continue;
				if(random.nextBoolean())
				{
					world.setBlockState(new BlockPos(x + mx + mx / 2 * -1, y + a2 + 1, z + mz), Blocks.STONEBRICK.getStateFromMeta(3));
				}
				if(!random.nextBoolean())
					continue;
				world.setBlockState(new BlockPos(x + mx, y + a2 + 1, z + mz + mz / 2 * -1), Blocks.STONEBRICK.getStateFromMeta(3));
			}
		}
	}
	
	private void genMossyStoneSurroundings(World world, Random random, int x, int y, int z)
	{
		for(int a = 0; a < 75; ++a)
			world.setBlockState(new BlockPos(x + random.nextInt(4) - random.nextInt(4), y, z + random.nextInt(4) - random.nextInt(4)), Blocks.MOSSY_COBBLESTONE.getDefaultState());
		
		for(int side = 0; side < 4; ++side)
		{
			int mx = 0;
			int mz = 0;
			switch(side)
			{
			case 0:
			{
				mz = 2;
				mx = 2;
				break;
			}
			case 1:
			{
				mx = 2;
				mz = -2;
				break;
			}
			case 2:
			{
				mz = -2;
				mx = -2;
				break;
			}
			case 3:
			{
				mx = -2;
				mz = 2;
			}
			}
			
			for(int a2 = 0; a2 < random.nextInt(5); ++a2)
			{
				if(a2 > 1)
				{
					if(random.nextBoolean())
						world.setBlockState(new BlockPos(x + mx + 1, y + a2 + 1, z + mz), Blocks.MOSSY_COBBLESTONE.getDefaultState());
					if(random.nextBoolean())
						world.setBlockState(new BlockPos(x + mx - 1, y + a2 + 1, z + mz), Blocks.MOSSY_COBBLESTONE.getDefaultState());
					if(random.nextBoolean())
						world.setBlockState(new BlockPos(x + mx, y + a2 + 1, z + mz + 1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
					if(random.nextBoolean())
						world.setBlockState(new BlockPos(x + mx, y + a2 + 1, z + mz - 1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
				}
				
				if(a2 != 3)
					continue;
				if(random.nextBoolean())
					world.setBlockState(new BlockPos(x + mx + mx / 2 * -1, y + a2 + 1, z + mz), Blocks.MOSSY_COBBLESTONE.getDefaultState());
				if(!random.nextBoolean())
					continue;
				world.setBlockState(new BlockPos(x + mx, y + a2 + 1, z + mz + mz / 2 * -1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
			}
		}
	}
}