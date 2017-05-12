package com.pengu.lostthaumaturgy.client.render.color;

import com.mrdimka.hammercore.common.utils.WorldUtil;

import com.pengu.lostthaumaturgy.block.BlockOreCrystal;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ColorBlockOreCrystal implements IBlockColor
{
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
	{
		BlockOreCrystal ore = WorldUtil.cast(state.getBlock(), BlockOreCrystal.class);
		if(ore != null) return ore.getCrystalColor();
		return 0xFFFFFF;
	}
}