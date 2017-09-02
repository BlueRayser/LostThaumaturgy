package com.pengu.lostthaumaturgy.core.block.wood.silverwood;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.core.tile.TileSilverwoodVisTank;
import com.pengu.lostthaumaturgy.core.tile.TileVisTank;

public class BlockSilverwoodVisTank extends BlockRendered implements ITileBlock<TileSilverwoodVisTank>, ITileEntityProvider
{
	public BlockSilverwoodVisTank()
	{
		super(Material.WOOD);
		setUnlocalizedName("silverwood_vis_tank");
		setSoundType(SoundType.WOOD);
		setHardness(1.5F);
		setResistance(4F);
	}
	
	public static final AxisAlignedBB TANK_AABB = new AxisAlignedBB(1 / 16D, 0, 1 / 16D, 15 / 16D, 1, 15 / 16D);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return TANK_AABB;
	}
	
	@Override
	public Class<TileSilverwoodVisTank> getTileClass()
	{
		return TileSilverwoodVisTank.class;
	}
	
	@Override
	public TileSilverwoodVisTank createNewTileEntity(World worldIn, int meta)
	{
		return new TileSilverwoodVisTank();
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
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return Info.MOD_ID + ":blocks/silverwood_vis_tank/top";
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileVisTank tank = WorldUtil.cast(worldIn.getTileEntity(pos), TileVisTank.class);
		if(tank != null)
			return Math.round(((tank.pureVis + tank.taintedVis) / tank.getMaxVis()) * 15F);
		return 0;
	}
}