package com.pengu.lostthaumaturgy.core.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.utils.RoundRobinList;
import com.pengu.hammercore.utils.WorldLocation;

public class WoodHelper
{
	public final World world;
	private RoundRobinList<BlockPos> positions = new RoundRobinList<BlockPos>();
	
	private WoodHelper(WorldLocation loc, int depth)
	{
		this.world = loc.getWorld();
		addLocation(this, world, loc.getPos(), depth);
	}
	
	public BlockPos getNearest(BlockPos toPos)
	{
		BlockPos p = null;
		positions.setPos(0);
		for(int i = 0; i < positions.size(); ++i)
		{
			BlockPos pos = positions.next();
			if(p == null || (pos.distanceSq(toPos) <= p.distanceSq(toPos)))
				p = pos;
		}
		return p;
	}
	
	public BlockPos getFarthest(BlockPos toPos)
	{
		BlockPos p = null;
		positions.setPos(0);
		for(int i = 0; i < positions.size(); ++i)
		{
			BlockPos pos = positions.next();
			if(p == null || (pos.distanceSq(toPos) >= p.distanceSq(toPos)))
				p = pos;
		}
		return p;
	}
	
	public static WoodHelper newHelper(WorldLocation loc, int maxSearchDepth)
	{
		return new WoodHelper(loc, maxSearchDepth);
	}
	
	private static void addLocation(WoodHelper helper, World world, BlockPos pos, int depth)
	{
		Block b = world.getBlockState(pos).getBlock();
		if(b instanceof BlockLog && !helper.positions.contains(pos))
		{
			helper.positions.add(pos);
			if(depth > 0)
				for(int x = -1; x < 2; ++x)
					for(int y = -1; y < 2; ++y)
						for(int z = -1; z < 2; ++z)
						{
							BlockPos p = pos.add(x, y, z);
							if(world.getBlockState(p).getBlock() instanceof BlockLog && !helper.positions.contains(p))
								addLocation(helper, world, p, depth - 1);
						}
		}
	}
}