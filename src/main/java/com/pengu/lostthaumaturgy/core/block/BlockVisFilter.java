package com.pengu.lostthaumaturgy.core.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.core.tile.TileVisFilter;

public class BlockVisFilter extends BlockRendered implements ITileBlock<TileVisFilter>, ITileEntityProvider
{
	public BlockVisFilter()
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("vis_filter");
		setHarvestLevel("pickaxe", 1);
		setHardness(1.5F);
		setResistance(4F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileVisFilter();
	}
	
	@Override
	public Class<TileVisFilter> getTileClass()
	{
		return TileVisFilter.class;
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
	
	public static final AxisAlignedBB FILTER_AABB = new AxisAlignedBB(2D / 16, 0, 2D / 16, 14D / 16, 1, 14D / 16);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return FILTER_AABB;
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return Info.MOD_ID + ":blocks/vis_filter_side_disconnected";
	}
}