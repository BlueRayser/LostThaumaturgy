package com.pengu.lostthaumaturgy.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class WorldGenSilverwood extends WorldGenAbstractTree implements IWorldGenerator
{
	public WorldGenSilverwood()
	{
		super(true);
	}
	
	static final byte[] otherCoordPairs = new byte[] { 2, 0, 0, 1, 2, 1 };
	Random rand;
	World worldObj;
	BlockPos basePos;
	int heightLimit = 0;
	int height;
	double heightAttenuation = 0.618;
	double field_875_h = 1.0;
	double field_874_i = 1.381;
	double horizontalspread = 0.66;
	double thickness = 1.0;
	int trunkSize = 1;
	int heightLimitLimit = 9;
	int leafDistanceLimit = 3;
	int[][] leafNodes;
	
	/**
	 * Generate some world
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
		if(random.nextInt(500) < 40)
			for(int i = 0; i < random.nextInt(2); i++)
			{
				int x = chunkX * 16 + random.nextInt(16);
				int z = chunkZ * 16 + random.nextInt(16);
				BlockPos pos = world.getHeight(new BlockPos(x, 255, z));
				
				generate(world, random, pos);
			}
	}
	
	void generateLeafNodeList()
	{
		int i;
		this.height = (int) ((double) this.heightLimit * this.heightAttenuation);
		if(this.height >= this.heightLimit)
		{
			this.height = this.heightLimit - 1;
		}
		if((i = (int) (1.382 + Math.pow(this.thickness * (double) this.heightLimit / 13.0, 2.0))) < 1)
		{
			i = 1;
		}
		int[][] ai = new int[i * this.heightLimit][4];
		int j = basePos.getY() + this.heightLimit - this.leafDistanceLimit;
		int k = 1;
		int l2 = this.basePos.getY() + this.height;
		int i1 = j - this.basePos.getY();
		ai[0][0] = this.basePos.getX();
		ai[0][1] = j--;
		ai[0][2] = this.basePos.getZ();
		ai[0][3] = l2;
		while(i1 >= 0)
		{
			float f = this.func_528_a(i1);
			if(f < 0.0f)
			{
				--j;
				--i1;
				continue;
			}
			double d = 0.5;
			for(int j1 = 0; j1 < i; ++j1)
			{
				int[] ai2;
				double d2;
				int l1;
				double d1 = this.horizontalspread * ((double) f * ((double) this.rand.nextFloat() + 0.328));
				int k1 = (int) Math.floor((double) (d1 * Math.sin(d2 = (double) this.rand.nextFloat() * 2.0 * 3.14159) + (double) this.basePos.getX() + d));
				int[] ai1 = new int[] { k1, j, l1 = (int) Math.floor((double) (d1 * Math.cos(d2) + (double) this.basePos.getZ() + d)) };
				if(this.checkBlockLine(ai1, ai2 = new int[] { k1, j + this.leafDistanceLimit, l1 }) != -1)
					continue;
				int[] ai3 = new int[] { this.basePos.getX(), this.basePos.getY(), this.basePos.getZ() };
				double d3 = Math.sqrt(Math.pow(Math.abs(this.basePos.getX() - ai1[0]), 2.0) + Math.pow(Math.abs(this.basePos.getZ() - ai1[2]), 2.0));
				double d4 = d3 * this.field_874_i;
				ai3[1] = (double) ai1[1] - d4 > (double) l2 ? l2 : (int) ((double) ai1[1] - d4);
				if(this.checkBlockLine(ai3, ai1) != -1)
					continue;
				ai[k][0] = k1;
				ai[k][1] = j;
				ai[k][2] = l1;
				ai[k][3] = ai3[1];
				++k;
			}
			--j;
			--i1;
		}
		this.leafNodes = new int[k][4];
		System.arraycopy(ai, 0, this.leafNodes, 0, k);
	}
	
	void placeLeaves(int i, int j, int k, float f, byte byte0, IBlockState l2)
	{
		if(worldObj == null)
			return;
		
		int i1 = (int) ((double) f + 0.618);
		byte byte1 = otherCoordPairs[byte0];
		byte byte2 = otherCoordPairs[byte0 + 3];
		int[] ai = new int[] { i, j, k };
		int[] ai1 = new int[] { 0, 0, 0 };
		int k1 = -i1;
		ai1[byte0] = ai[byte0];
		for(int j1 = -i1; j1 <= i1; ++j1)
		{
			ai1[byte1] = ai[byte1] + j1;
			int l1 = -i1;
			while(l1 <= i1)
			{
				double d = Math.sqrt(Math.pow((double) Math.abs(j1) + 0.5, 2.0) + Math.pow((double) Math.abs(l1) + 0.5, 2.0));
				if(d > (double) f)
				{
					++l1;
					continue;
				}
				ai1[byte2] = ai[byte2] + l1;
				BlockPos bpos = new BlockPos(ai1[0], ai1[1], ai1[2]);
				if(worldObj.getBlockState(bpos) == null)
				{
					++l1;
					continue;
				}
				Block i2 = this.worldObj.getBlockState(bpos).getBlock();
				if(i2 != Blocks.AIR && i2 != Blocks.LEAVES && i2 != Blocks.LEAVES2)
				{
					++l1;
					continue;
				}
				setBlockAndNotifyAdequately(worldObj, bpos, l2);
				++l1;
			}
		}
	}
	
	void generateLeafNode(int i, int j, int k)
	{
		int i1 = j + this.leafDistanceLimit;
		for(int l2 = j; l2 < i1; ++l2)
		{
			float f = this.func_526_b(l2 - j);
			this.placeLeaves(i, l2, k, f, (byte) 1, BlocksLT.SILVERWOOD_LEAVES.getDefaultState());
		}
	}
	
	// public void func_517_a(double d, double d1, double d2)
	// {
	// this.heightLimitLimit = (int) (d * 12.0);
	// if(d > 0.5)
	// {
	// this.leafDistanceLimit = 5;
	// }
	// this.horizontalspread = d1;
	// this.thickness = d2;
	// }
	
	float func_528_a(int i)
	{
		if((double) i < (double) this.heightLimit * 0.3)
		{
			return -1.618f;
		}
		float f = (float) this.heightLimit / 2.0f;
		float f1 = (float) this.heightLimit / 2.0f - (float) i;
		float f2 = f1 == 0.0f ? f : (Math.abs(f1) >= f ? 0.0f : (float) Math.sqrt(Math.pow(Math.abs(f), 2.0) - Math.pow(Math.abs(f1), 2.0)));
		return f2 *= 0.5f;
	}
	
	float func_526_b(int i)
	{
		if(i < 0 || i >= this.leafDistanceLimit)
		{
			return -1.0f;
		}
		return i != 0 && i != this.leafDistanceLimit - 1 ? 3.0f : 2.0f;
	}
	
	void placeBlockLine(int[] ai, int[] ai1, IBlockState i)
	{
		int[] ai2 = new int[] { 0, 0, 0 };
		int j = 0;
		for(int byte0 = 0; byte0 < 3; byte0 = (int) ((byte) (byte0 + 1)))
		{
			ai2[byte0] = ai1[byte0] - ai[byte0];
			if(Math.abs(ai2[byte0]) <= Math.abs(ai2[j]))
				continue;
			j = byte0;
		}
		if(ai2[j] == 0)
		{
			return;
		}
		byte byte1 = otherCoordPairs[j];
		byte byte2 = otherCoordPairs[j + 3];
		int byte3 = ai2[j] > 0 ? 1 : -1;
		double d = (double) ai2[byte1] / (double) ai2[j];
		double d1 = (double) ai2[byte2] / (double) ai2[j];
		int[] ai3 = new int[] { 0, 0, 0 };
		int l2 = ai2[j] + byte3;
		for(int k = 0; k != l2; k += byte3)
		{
			ai3[j] = (int) Math.floor((double) ((double) (ai[j] + k) + 0.5));
			ai3[byte1] = (int) Math.floor((double) ((double) ai[byte1] + (double) k * d + 0.5));
			ai3[byte2] = (int) Math.floor((double) ((double) ai[byte2] + (double) k * d1 + 0.5));
			setBlockAndNotifyAdequately(worldObj, new BlockPos(ai3[0], ai3[1], ai3[2]), i);
		}
	}
	
	void generateLeaves()
	{
		int j = this.leafNodes.length;
		for(int i = 0; i < j; ++i)
		{
			int k = this.leafNodes[i][0];
			int l2 = this.leafNodes[i][1];
			int i1 = this.leafNodes[i][2];
			this.generateLeafNode(k, l2, i1);
		}
	}
	
	boolean leafNodeNeedsBase(int i)
	{
		return (double) i >= (double) this.heightLimit * 0.2;
	}
	
	void generateTrunk()
	{
		int i = this.basePos.getX();
		int j = this.basePos.getY();
		int k = this.basePos.getY() + this.height;
		int l2 = this.basePos.getZ();
		int[] ai = new int[] { i, j, l2 };
		int[] ai1 = new int[] { i, k, l2 };
		this.placeBlockLine(ai, ai1, BlocksLT.SILVERWOOD_LOG.getDefaultState());
		if(this.trunkSize == 2)
		{
			int[] arrn = ai;
			arrn[0] = arrn[0] + 1;
			int[] arrn2 = ai1;
			arrn2[0] = arrn2[0] + 1;
			this.placeBlockLine(ai, ai1, BlocksLT.SILVERWOOD_LOG.getDefaultState());
			int[] arrn3 = ai;
			arrn3[2] = arrn3[2] + 1;
			int[] arrn4 = ai1;
			arrn4[2] = arrn4[2] + 1;
			this.placeBlockLine(ai, ai1, BlocksLT.SILVERWOOD_LOG.getDefaultState());
			int[] arrn5 = ai;
			arrn5[0] = arrn5[0] - 1;
			int[] arrn6 = ai1;
			arrn6[0] = arrn6[0] - 1;
			this.placeBlockLine(ai, ai1, BlocksLT.SILVERWOOD_LOG.getDefaultState());
		}
	}
	
	void generateLeafNodeBases()
	{
		int j = this.leafNodes.length;
		int[] ai = new int[] { this.basePos.getX(), this.basePos.getY(), this.basePos.getZ() };
		for(int i = 0; i < j; ++i)
		{
			int[] ai1 = this.leafNodes[i];
			int[] ai2 = new int[] { ai1[0], ai1[1], ai1[2] };
			ai[1] = ai1[3];
			int k = ai[1] - this.basePos.getY();
			if(!this.leafNodeNeedsBase(k))
				continue;
			this.placeBlockLine(ai, ai2, BlocksLT.SILVERWOOD_LOG.getDefaultState());
		}
	}
	
	int checkBlockLine(int[] ai, int[] ai1)
	{
		int j;
		int[] ai2 = new int[] { 0, 0, 0 };
		int i = 0;
		for(int byte0 = 0; byte0 < 3; byte0 = (int) ((byte) (byte0 + 1)))
		{
			ai2[byte0] = ai1[byte0] - ai[byte0];
			if(Math.abs(ai2[byte0]) <= Math.abs(ai2[i]))
				continue;
			i = byte0;
		}
		if(ai2[i] == 0)
			return -1;
		byte byte1 = otherCoordPairs[i];
		byte byte2 = otherCoordPairs[i + 3];
		int byte3 = ai2[i] > 0 ? 1 : -1;
		double d = (double) ai2[byte1] / (double) ai2[i];
		double d1 = (double) ai2[byte2] / (double) ai2[i];
		int[] ai3 = new int[] { 0, 0, 0 };
		int k = ai2[i] + byte3;
		for(j = 0; j != k; j += byte3)
		{
			ai3[i] = ai[i] + j;
			ai3[byte1] = (int) Math.floor((double) ((double) ai[byte1] + (double) j * d));
			ai3[byte2] = (int) Math.floor((double) ((double) ai[byte2] + (double) j * d1));
			Block l2 = this.worldObj.getBlockState(new BlockPos(ai3[0], ai3[1], ai3[2])).getBlock();
			if(l2 != Blocks.AIR && l2 != Blocks.LEAVES)
				break;
		}
		if(j == k)
			return -1;
		return Math.abs(j);
	}
	
	boolean validTreeLocation()
	{
		int[] ai = new int[] { this.basePos.getX(), this.basePos.getY(), this.basePos.getZ() };
		int[] ai1 = new int[] { this.basePos.getX(), this.basePos.getY() + this.heightLimit - 1, this.basePos.getZ() };
		Block i = this.worldObj.getBlockState(new BlockPos(this.basePos.getX(), this.basePos.getY() - 1, this.basePos.getZ())).getBlock();
		if(i != Blocks.GRASS && i != Blocks.DIRT)
			return false;
		int j = this.checkBlockLine(ai, ai1);
		if(j == -1)
			return true;
		if(j < 6)
			return false;
		this.heightLimit = j;
		return true;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		this.rand = rand;
		worldObj = worldIn;
		long l2 = rand.nextLong();
		rand.setSeed(l2);
		basePos = position;
		if(heightLimit == 0)
			heightLimit = 5 + rand.nextInt(heightLimitLimit);
		if(!validTreeLocation())
			return false;
		try
		{
			generateLeafNodeList();
			generateLeaves();
			generateTrunk();
			generateLeafNodeBases();
		} catch(Throwable err)
		{
		}
		worldObj = null;
		
		int flowerCount = rand.nextInt(8);
		
		for(int i = 0; i < flowerCount; ++i)
		{
			BlockPos tpos = new BlockPos(position.getX() + offset(rand, 6), position.getY() + offset(rand, 6), position.getZ() + offset(rand, 6));
			
			for(int t = 0; t < 25; ++t)
			{
				BlockPos gpos = posFromTop(tpos, worldIn, GRASS_OR_DIRT);
				if(gpos != null && worldIn.getBlockState(gpos.up()).getBlock().isReplaceable(worldIn, gpos.up()))
				{
					worldIn.setBlockState(gpos.up(), BlocksLT.SHIMMERLEAF.getDefaultState());
					break;
				}
			}
		}
		
		return true;
	}
	
	private int offset(Random rand, int rad)
	{
		return rand.nextInt(rad) - rand.nextInt(rad);
	}
	
	public static final Predicate<IBlockState> GRASS_OR_DIRT = Predicates.or(Predicates.equalTo(Blocks.GRASS.getDefaultState()), Predicates.equalTo(Blocks.DIRT.getDefaultState()));
	
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