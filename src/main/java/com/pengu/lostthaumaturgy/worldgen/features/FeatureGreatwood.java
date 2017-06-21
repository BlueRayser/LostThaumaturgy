package com.pengu.lostthaumaturgy.worldgen.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import com.pengu.lostthaumaturgy.init.BlocksLT;

public class FeatureGreatwood extends WorldGenAbstractTree
{
	public FeatureGreatwood()
	{
		super(true);
	}
	
	static final byte[] otherCoordPairs = new byte[] { 2, 0, 0, 1, 2, 1 };
	Random rand = new Random();
	World world;
	int[] basePos = new int[] { 0, 0, 0 };
	int heightLimit = 0;
	int height;
	double heightAttenuation = 0.618;
	double field_875_h = 1.0;
	double field_874_i = 0.381;
	double horizontalspread = 1.66;
	double thickness = 1.0;
	int trunkSize = 2;
	int heightLimitLimit = 10;
	int leafDistanceLimit = 4;
	int[][] leafNodes;
	
	void generateLeafNodeList()
	{
		int i;
		height = (int) ((double) heightLimit * heightAttenuation);
		if(height >= heightLimit)
			height = heightLimit - 1;
		if((i = (int) (1.382 + Math.pow(thickness * (double) heightLimit / 13.0, 2.0))) < 1)
			i = 1;
		int[][] ai = new int[i * heightLimit][4];
		int j = basePos[1] + heightLimit - leafDistanceLimit;
		int k = 1;
		int l2 = basePos[1] + height;
		int i1 = j - basePos[1];
		ai[0][0] = basePos[0];
		ai[0][1] = j--;
		ai[0][2] = basePos[2];
		ai[0][3] = l2;
		while(i1 >= 0)
		{
			float f = func_528_a(i1);
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
				double d1 = horizontalspread * ((double) f * ((double) rand.nextFloat() + 0.328));
				int k1 = MathHelper.floor((double) (d1 * Math.sin(d2 = (double) rand.nextFloat() * 2.0 * 3.14159) + (double) basePos[0] + d));
				int[] ai1 = new int[] { k1, j, l1 = MathHelper.floor((double) (d1 * Math.cos(d2) + (double) basePos[2] + d)) };
				if(checkBlockLine(ai1, ai2 = new int[] { k1, j + leafDistanceLimit, l1 }) != -1)
					continue;
				int[] ai3 = new int[] { basePos[0], basePos[1], basePos[2] };
				double d3 = Math.sqrt(Math.pow(Math.abs(basePos[0] - ai1[0]), 2.0) + Math.pow(Math.abs(basePos[2] - ai1[2]), 2.0));
				double d4 = d3 * field_874_i;
				ai3[1] = (double) ai1[1] - d4 > (double) l2 ? l2 : (int) ((double) ai1[1] - d4);
				if(checkBlockLine(ai3, ai1) != -1)
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
		leafNodes = new int[k][4];
		System.arraycopy(ai, 0, leafNodes, 0, k);
	}
	
	void placeLeaves(int i, int j, int k, float f, byte byte0, IBlockState l2)
	{
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
				Block i2 = world.getBlockState(new BlockPos(ai1[0], ai1[1], ai1[2])).getBlock();
				if(i2 != Blocks.AIR && !(i2 instanceof BlockLeaves))
				{
					++l1;
					continue;
				}
				setBlockAndNotifyAdequately(world, new BlockPos(ai1[0], ai1[1], ai1[2]), l2);
				++l1;
			}
		}
	}
	
	float func_528_a(int i)
	{
		if((double) i < (double) heightLimit * 0.3)
		{
			return -1.618f;
		}
		float f = (float) heightLimit / 2.0f;
		float f1 = (float) heightLimit / 2.0f - (float) i;
		float f2 = f1 == 0.0f ? f : (Math.abs(f1) >= f ? 0.0f : (float) Math.sqrt(Math.pow(Math.abs(f), 2.0) - Math.pow(Math.abs(f1), 2.0)));
		return f2 *= 0.5f;
	}
	
	float func_526_b(int i)
	{
		if(i < 0 || i >= leafDistanceLimit)
		{
			return -1.0f;
		}
		return i != 0 && i != leafDistanceLimit - 1 ? 3.0f : 2.0f;
	}
	
	void generateLeafNode(int i, int j, int k)
	{
		int i1 = j + leafDistanceLimit;
		for(int l2 = j; l2 < i1; ++l2)
		{
			float f = func_526_b(l2 - j);
			placeLeaves(i, l2, k, f, (byte) 1, BlocksLT.GREATWOOD_LEAVES.getDefaultState());
		}
	}
	
	void placeBlockLine(int[] ai, int[] ai1, IBlockState state)
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
			ai3[j] = MathHelper.floor((double) ((double) (ai[j] + k) + 0.5));
			ai3[byte1] = MathHelper.floor((double) ((double) ai[byte1] + (double) k * d + 0.5));
			ai3[byte2] = MathHelper.floor((double) ((double) ai[byte2] + (double) k * d1 + 0.5));
			setBlockAndNotifyAdequately(world, new BlockPos(ai3[0], ai3[1], ai3[2]), state);
		}
	}
	
	void generateLeaves()
	{
		int j = leafNodes.length;
		for(int i = 0; i < j; ++i)
		{
			int k = leafNodes[i][0];
			int l2 = leafNodes[i][1];
			int i1 = leafNodes[i][2];
			generateLeafNode(k, l2, i1);
		}
	}
	
	boolean leafNodeNeedsBase(int i)
	{
		return (double) i >= (double) heightLimit * 0.2;
	}
	
	void generateTrunk()
	{
		int i = basePos[0];
		int j = basePos[1];
		int k = basePos[1] + height;
		int l2 = basePos[2];
		int[] ai = new int[] { i, j, l2 };
		int[] ai1 = new int[] { i, k, l2 };
		if(trunkSize == 2)
		{
			placeBlockLine(ai, ai1, BlocksLT.GREATWOOD_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y));
			int[] arrn = ai;
			arrn[0] = arrn[0] + 1;
			int[] arrn2 = ai1;
			arrn2[0] = arrn2[0] + 1;
			placeBlockLine(ai, ai1, BlocksLT.GREATWOOD_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y));
			int[] arrn3 = ai;
			arrn3[2] = arrn3[2] + 1;
			int[] arrn4 = ai1;
			arrn4[2] = arrn4[2] + 1;
			placeBlockLine(ai, ai1, BlocksLT.GREATWOOD_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y));
			int[] arrn5 = ai;
			arrn5[0] = arrn5[0] - 1;
			int[] arrn6 = ai1;
			arrn6[0] = arrn6[0] - 1;
			placeBlockLine(ai, ai1, BlocksLT.GREATWOOD_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y));
		} else
			placeBlockLine(ai, ai1, BlocksLT.GREATWOOD_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y));
	}
	
	void generateLeafNodeBases()
	{
		int j = leafNodes.length;
		int[] ai = new int[] { basePos[0], basePos[1], basePos[2] };
		for(int i = 0; i < j; ++i)
		{
			int[] ai1 = leafNodes[i];
			int[] ai2 = new int[] { ai1[0], ai1[1], ai1[2] };
			ai[1] = ai1[3];
			int k = ai[1] - basePos[1];
			if(!leafNodeNeedsBase(k))
				continue;
			placeBlockLine(ai, ai2, BlocksLT.GREATWOOD_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
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
		{
			return -1;
		}
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
			ai3[byte1] = MathHelper.floor((double) ((double) ai[byte1] + (double) j * d));
			ai3[byte2] = MathHelper.floor((double) ((double) ai[byte2] + (double) j * d1));
			Block l2 = world.getBlockState(new BlockPos(ai3[0], ai3[1], ai3[2])).getBlock();
			if(l2 != Blocks.AIR && !(l2 instanceof BlockLeaves))
				break;
		}
		if(j == k)
		{
			return -1;
		}
		return Math.abs(j);
	}
	
	boolean validTreeLocation(int x, int z)
	{
		int[] ai = new int[] { basePos[0] + x, basePos[1], basePos[2] + z };
		int[] ai1 = new int[] { basePos[0] + x, basePos[1] + heightLimit - 1, basePos[2] + z };
		Block i = world.getBlockState(new BlockPos(basePos[0] + x, basePos[1] - 1, basePos[2] + z)).getBlock();
		if(i != Blocks.DIRT && i != Blocks.GRASS)
			return false;
		int j = checkBlockLine(ai, ai1);
		if(j == -1)
			return true;
		if(j < 6)
			return false;
		heightLimit = j;
		return true;
	}
	
	public void func_517_a(double d, double d1, double d2)
	{
		heightLimitLimit = (int) (d * 12.0);
		if(d > 0.5)
			leafDistanceLimit = 5;
		horizontalspread = d1;
		thickness = d2;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand_, BlockPos position)
	{
		world = worldIn;
		long l2 = rand_.nextLong();
		rand.setSeed(l2);
		basePos[0] = position.getX();
		basePos[1] = position.getY();
		basePos[2] = position.getZ();
		if(heightLimit == 0)
			heightLimit = 10 + rand.nextInt(heightLimitLimit);
		int x = 0;
		int z = 0;
		for(x = 0; x < trunkSize; ++x)
			for(z = 0; z < trunkSize; ++z)
				if(!validTreeLocation(x, z))
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
		return true;
	}
}