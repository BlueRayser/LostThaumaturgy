package com.pengu.lostthaumaturgy.api.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import com.pengu.hammercore.utils.RoundRobinList;
import com.pengu.lostthaumaturgy.tile.TileTaintedSoil;

public class TaintedSoilEvent extends BlockEvent
{
	public final TileTaintedSoil soil;
	
	public TaintedSoilEvent(World world, BlockPos pos, TileTaintedSoil soil)
	{
		super(world, pos, world.getBlockState(pos));
		this.soil = soil;
	}
	
	public static class GetDrops extends TaintedSoilEvent
	{
		public final RoundRobinList<ItemStack> drops = new RoundRobinList<ItemStack>();
		
		public GetDrops(World world, BlockPos pos, TileTaintedSoil soil)
		{
			super(world, pos, soil);
		}
	}
}