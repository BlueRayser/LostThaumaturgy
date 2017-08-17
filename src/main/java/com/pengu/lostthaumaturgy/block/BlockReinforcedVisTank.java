package com.pengu.lostthaumaturgy.block;

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
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.tile.TileReinforcedVisTank;
import com.pengu.lostthaumaturgy.tile.TileVisTank;

public class BlockReinforcedVisTank extends BlockRendered implements ITileBlock<TileReinforcedVisTank>, ITileEntityProvider
{
	public BlockReinforcedVisTank()
	{
		super(Material.IRON);
		setUnlocalizedName("reinforced_vis_tank");
		setSoundType(SoundType.METAL);
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
	public Class<TileReinforcedVisTank> getTileClass()
	{
		return TileReinforcedVisTank.class;
	}
	
	@Override
	public TileReinforcedVisTank createNewTileEntity(World worldIn, int meta)
	{
		return new TileReinforcedVisTank();
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
		return LTInfo.MOD_ID + ":blocks/reinforced_vis_tank/top";
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