package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPlant extends BlockBush
{
	private final AxisAlignedBB aabb;
	
	public BlockPlant(String name, AxisAlignedBB aabb)
	{
		this.aabb = aabb;
		setSoundType(SoundType.PLANT);
		setUnlocalizedName(name);
		setHarvestLevel(null, 0);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return aabb;
	}
}