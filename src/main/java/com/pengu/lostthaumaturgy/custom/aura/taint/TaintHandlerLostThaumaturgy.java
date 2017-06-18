package com.pengu.lostthaumaturgy.custom.aura.taint;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.utils.WorldLocation;
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
		if(!world.isBlockLoaded(pos))
			return false;
		IBlockState state = world.getBlockState(pos);
		
		if(BlockTaintedSoil.isTaintable(world, pos))
		{
			int sides = 0;
			
			for(EnumFacing f : EnumFacing.VALUES)
			{
				WorldLocation wl = new WorldLocation(world, pos.offset(f));
				if(!wl.getBlock().isSideSolid(wl.getState(), world, wl.getPos(), f.getOpposite()))
					sides++;
			}
			
			return sides > 0;
		}
		
		if(state.getBlock() == BlocksLT.SHIMMERLEAF)
			return true;
		
		if(state.getBlock() instanceof BlockOreCrystal)
			return true;
		
		if(state.getBlock() == Blocks.LOG || state.getBlock() == Blocks.LOG2)
			return true;
		
		return false;
	}
	
	@Override
	public boolean taintBlock(World world, BlockPos pos)
	{
		if(!world.isBlockLoaded(pos))
			return false;
		IBlockState state = world.getBlockState(pos);
		
		if(BlockTaintedSoil.isTaintable(world, pos))
		{
			BlockTaintedSoil.placeSoil(world, pos);
			if(world.rand.nextInt(15) == 0 && world.isAirBlock(pos.up()))
				world.setBlockState(pos.up(), BlocksLT.TAINTED_PLANT.getStateFromMeta(world.rand.nextInt(2)));
			return true;
		}
		
		if(state.getBlock() == BlocksLT.SHIMMERLEAF)
		{
			world.setBlockState(pos, BlocksLT.TAINTEDLEAF.getDefaultState());
			return true;
		}
		
		if(state.getBlock() instanceof BlockOreCrystal)
		{
			TileEntity te = world.getTileEntity(pos);
			NBTTagCompound nbt = new NBTTagCompound();
			te.writeToNBT(nbt);
			
			world.setBlockState(pos, BlocksLT.CRYSTAL_ORE_TAINTED.getDefaultState());
			
			te = world.getTileEntity(pos);
			if(te != null)
			{
				te.readFromNBT(nbt);
				if(te instanceof TileSyncable)
					((TileSyncable) te).sync();
			}
		}
		
		if(state.getBlock() == Blocks.LOG || state.getBlock() == Blocks.LOG2)
		{
			EnumAxis axis = state.getValue(BlockLog.LOG_AXIS);
			IBlockState nstate = BlocksLT.TAINTED_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, axis);
			world.setBlockState(pos, nstate);
		}
		
		return false;
	}
	
	@Override
	public boolean canCureBlock(World world, BlockPos pos)
	{
		if(!world.isBlockLoaded(pos))
			return false;
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() == BlocksLT.TAINTEDLEAF)
			return true;
		if(world.getTileEntity(pos) instanceof TileTaintedSoil)
			return true;
		if(state.getBlock() == BlocksLT.TAINTED_PLANT || state.getBlock() == BlocksLT.TAINTED_LOG)
			return true;
		
		return false;
	}
	
	@Override
	public boolean cureBlock(World world, BlockPos pos)
	{
		if(!world.isBlockLoaded(pos))
			return false;
		IBlockState state = world.getBlockState(pos);
		
		if(world.getTileEntity(pos) instanceof TileTaintedSoil)
			BlockTaintedSoil.cleanSoil(world, pos);
		
		if(state.getBlock() == BlocksLT.TAINTEDLEAF)
			world.setBlockState(pos, BlocksLT.SHIMMERLEAF.getDefaultState());
		
		if(state.getBlock() == BlocksLT.TAINTED_PLANT || state.getBlock() == BlocksLT.TAINTED_LOG)
			world.setBlockToAir(pos);
		
		return false;
	}
	
	@Override
	public String handlerId()
	{
		return LTInfo.MOD_ID;
	}
}