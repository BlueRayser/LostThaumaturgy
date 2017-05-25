package com.pengu.lostthaumaturgy.custom.aura.taint;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.block.BlockTaintedSoil;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.tile.TileTaintedSoil;

public class TaintHandlerLostThaumaturgy implements ITaintHandler
{
	@Override
    public boolean canTaintBlock(World world, BlockPos pos)
    {
		if(!world.isBlockLoaded(pos)) return false;
		IBlockState state = world.getBlockState(pos);
		
		if(BlockTaintedSoil.isTaintable(world, pos))
			return true;
		
		if(state.getBlock() == BlocksLT.SHIMMERLEAF)
			return true;
		if(state.getBlock() instanceof BlockOreCrystal)
			return true;
		
	    return false;
    }

	@Override
    public boolean taintBlock(World world, BlockPos pos)
    {
		if(!world.isBlockLoaded(pos)) return false;
		IBlockState state = world.getBlockState(pos);
		
		if(BlockTaintedSoil.isTaintable(world, pos))
		{
			BlockTaintedSoil.placeSoil(world, pos);
			return true;
		}
		
		if(state.getBlock() == BlocksLT.SHIMMERLEAF)
		{
			world.setBlockState(pos, BlocksLT.TAINTEDLEAF.getDefaultState());
			return true;
		}
		
		if(state.getBlock() instanceof BlockOreCrystal)
		{
			world.setBlockState(pos, BlocksLT.CRYSTAL_ORE_TAINTED.getDefaultState());
		}
		
	    return false;
    }

	@Override
    public boolean canCureBlock(World world, BlockPos pos)
    {
		if(!world.isBlockLoaded(pos)) return false;
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() == BlocksLT.CRYSTAL_ORE_TAINTED)
			return true;
		if(state.getBlock() == BlocksLT.TAINTEDLEAF)
			return true;
		if(world.getTileEntity(pos) instanceof TileTaintedSoil)
			return true;
		
	    return false;
    }

	@Override
    public boolean cureBlock(World world, BlockPos pos)
    {
		if(!world.isBlockLoaded(pos)) return false;
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() == BlocksLT.CRYSTAL_ORE_TAINTED)
			world.setBlockState(pos, new ArrayList<>(BlockOreCrystal.crystals).get(world.rand.nextInt(BlockOreCrystal.crystals.size())).getDefaultState());
		
		if(world.getTileEntity(pos) instanceof TileTaintedSoil)
			BlockTaintedSoil.cleanSoil(world, pos);
		
		if(state.getBlock() == BlocksLT.TAINTEDLEAF)
			world.setBlockState(pos, BlocksLT.SHIMMERLEAF.getDefaultState());
		
	    return false;
    }

	@Override
    public String handlerId()
    {
	    return LTInfo.MOD_ID;
    }
}