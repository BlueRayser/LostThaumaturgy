package com.pengu.lostthaumaturgy.block.wood.greatwood;

import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

public class BlockGreatwoodLog extends BlockLog
{
	public BlockGreatwoodLog()
	{
		setUnlocalizedName("greatwood_log");
		Blocks.FIRE.setFireInfo(this, 5, 5);
		setDefaultState(getDefaultState().withProperty(LOG_AXIS, EnumAxis.Y));
		setTickRandomly(true);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, LOG_AXIS);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(LOG_AXIS, EnumAxis.values()[meta % EnumAxis.values().length]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(LOG_AXIS).ordinal();
	}
}