package com.pengu.lostthaumaturgy.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.api.blocks.ITaintedBlock;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class BlockTaintedPlant extends BlockPlant implements ITaintedBlock
{
	public static PropertyInteger GROWTH = PropertyInteger.create("growth", 0, 1);
	
	public BlockTaintedPlant()
	{
		super("tainted_plant", new AxisAlignedBB(.1, 0, .1, .9, .8, .9));
		setHardness(1F);
		setTickRandomly(true);
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		if(random.nextInt(4) != 0)
			return;
		
		if(!canSustainBush(worldIn.getBlockState(pos.down())))
		{
			worldIn.setBlockToAir(pos);
			
			AtmosphereChunk si = AuraTicker.getAuraChunkFromBlockCoords(worldIn, pos);
			si.taint += 5 + random.nextInt(20);
			si.badVibes += 4 + random.nextInt(10);
			
			return;
		}
		
		if(getMetaFromState(state) == 0)
			worldIn.setBlockState(pos, getStateFromMeta(1));
		else
		{
			worldIn.setBlockState(pos, getStateFromMeta(0));
			AtmosphereChunk si = AuraTicker.getAuraChunkFromBlockCoords(worldIn, pos);
			si.taint += 5 + random.nextInt(20);
			si.badVibes += 4 + random.nextInt(10);
		}
	}
	
	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock() == BlocksLT.TAINTED_SOIL;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return Arrays.asList();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, GROWTH);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(GROWTH, meta % 2);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return getDefaultState().getValue(GROWTH);
	}
}