package com.pengu.lostthaumaturgy.core.worldgen;

import java.util.HashSet;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.core.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.core.tile.TileCrystalOre;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class WorldGenCrystals implements IWorldGenerator
{
	private static final BlockOreCrystal[] ores;
	
	static
	{
		HashSet<BlockOreCrystal> crystals = new HashSet<BlockOreCrystal>(BlockOreCrystal.crystals);
		crystals.removeIf(t -> !t.generatesInWorld);
		ores = crystals.toArray(new BlockOreCrystal[0]);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		GenerateOre(world, random, chunkX * 16, chunkZ * 16);
	}
	
	public static void GenerateOre(World world, Random random, int x, int z)
	{
		int randPosX;
		int i;
		int randPosZ;
		int randPosY;
		
		if(random.nextInt(1000) < 60 * 13)
			for(int j = 0; j < random.nextInt(8); ++j)
				for(i = 0; i < 2; ++i)
				{
					int tries;
					randPosX = x + random.nextInt(16);
					randPosY = random.nextInt(world.getHeight() / 3);
					randPosZ = z + random.nextInt(16);
					int levels = world.getHeight() / 12;
					BlockPos pos = new BlockPos(randPosX, randPosY, randPosZ);
					int face = TileCrystalOre.suggestOrientationForWorldGen(world, pos);
					if(face == -1)
						continue;
					IBlockState ore = ores[world.rand.nextInt(ores.length)].getDefaultState();
					world.setBlockState(pos, ore);
					TileCrystalOre tco = WorldUtil.cast(world.getTileEntity(pos), TileCrystalOre.class);
					if(tco == null)
					{
						tco = new TileCrystalOre();
						world.setTileEntity(pos, tco);
					}
					tco.orientation.set((short) face);
					tco.crystals.set((short) (1 + world.rand.nextInt(4)));
				}
		
		if(random.nextInt(1000) < 80 * 13)
			for(i = 0; i < 15; ++i)
			{
				randPosX = x + random.nextInt(16);
				randPosY = random.nextInt(world.getHeight() / 5);
				randPosZ = z + random.nextInt(16);
				
				BlockPos pos = new BlockPos(randPosX, randPosY, randPosZ);
				IBlockState block = world.getBlockState(pos);
				if(!block.equals(Blocks.STONE.getStateFromMeta(0)))
					continue;
				
				if(world.rand.nextInt(5) == 0)
					world.setBlockState(pos, BlocksLT.CINNABAR_ORE.getDefaultState());
			}
	}
}