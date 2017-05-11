package com.pengu.lostthaumaturgy.block;

import com.mrdimka.hammercore.api.ITileBlock;
import com.pengu.lostthaumaturgy.tile.TileBellows;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBellows extends BlockContainer implements ITileBlock<TileBellows>
{
	public BlockBellows()
    {
		super(Material.IRON);
		setUnlocalizedName("bellows");
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
	    return new TileBellows();
	}
	
	@Override
	public Class<TileBellows> getTileClass()
	{
	    return TileBellows.class;
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
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
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
}