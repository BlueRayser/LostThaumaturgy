package com.pengu.lostthaumaturgy.block.monolith;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.api.ITileBlock;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.tile.monolith.TileMonolithOpener;

public class BlockMonolithOpener extends BlockRendered implements ITileEntityProvider, ITileBlock<TileMonolithOpener>
{
	public BlockMonolithOpener()
	{
		super(Material.ROCK);
		setUnlocalizedName("lt_monolith_opener");
		setBlockUnbreakable();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileMonolithOpener();
	}
	
	@Override
	public Class<TileMonolithOpener> getTileClass()
	{
		return TileMonolithOpener.class;
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return LTInfo.MOD_ID + ":blocks/eldritch_block/0";
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return false;
	}
	
	public static void buildMonolith(World world, BlockPos pos)
	{
		AuraTicker.addMonolith(pos);
		
		for(int i = -1; i < 2; ++i)
			for(int j = -1; j < 2; ++j)
			{
				for(int k = -1; k < 8; ++k)
					world.setBlockToAir(pos.add(i, k, j));
				
				world.setBlockState(pos.add(i, -1, j), BlocksLT.ELDRITCH_BLOCK.getDefaultState());
				world.setBlockState(pos.add(i, 0, j), BlocksLT.ELDRITCH_BLOCK.getDefaultState());
			}
		
		for(int i = 0; i < 5; ++i)
			world.setBlockState(pos.up(3 + i), BlocksLT.MONOLITH.getDefaultState());
		
		world.setBlockState(pos, BlocksLT.MONOLITH_OPENER.getDefaultState());
		world.setBlockState(pos.east(), BlocksLT.MONOLITH_CRYSTAL_RECEPTACLE.getDefaultState());
		world.setBlockState(pos.west(), BlocksLT.MONOLITH_CRYSTAL_RECEPTACLE.getDefaultState());
		world.setBlockState(pos.south(), BlocksLT.MONOLITH_CRYSTAL_RECEPTACLE.getDefaultState());
		world.setBlockState(pos.north(), BlocksLT.MONOLITH_CRYSTAL_RECEPTACLE.getDefaultState());
	}
}