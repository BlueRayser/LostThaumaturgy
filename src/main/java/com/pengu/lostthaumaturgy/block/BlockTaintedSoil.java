package com.pengu.lostthaumaturgy.block;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;

import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.tile.TileTaintedSoil;

public class BlockTaintedSoil extends Block implements ITileEntityProvider, ITileBlock<TileTaintedSoil>
{
	public BlockTaintedSoil()
	{
		super(Material.GROUND);
		setHardness(10F);
		setResistance(100F);
	}
	
	public static void placeSoil(World world, BlockPos pos)
	{
		BlockSnapshot snapshot = new BlockSnapshot(world, pos, world.getBlockState(pos));
		TileTaintedSoil soil = new TileTaintedSoil();
		world.setBlockState(pos, BlocksLT.TAINTED_SOIL.getDefaultState());
		world.setTileEntity(pos, soil);
		soil.setSnapshot(snapshot);
	}
	
	public static void cleanSoil(World world, BlockPos pos)
	{
		TileTaintedSoil soil = WorldUtil.cast(world.getTileEntity(pos), TileTaintedSoil.class);
		if(soil != null)
			soil.getSnapshot().restore(true);
	}
	
	public static boolean isTaintable(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		ResourceLocation reg = b.getRegistryName();
		
		if(LTConfigs.taintableBlocks != null)
			for(String v : LTConfigs.taintableBlocks)
			{
				ResourceLocation vr = new ResourceLocation(v);
				if(vr.equals(reg))
					return true;
			}
		
		return false;
	}
	
	@Override
	public Class<TileTaintedSoil> getTileClass()
	{
		return TileTaintedSoil.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileTaintedSoil();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return Arrays.asList();
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return super.getBlockHardness(blockState, worldIn, pos);
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity)
	{
		return super.getSoundType(state, world, pos, entity);
	}
}