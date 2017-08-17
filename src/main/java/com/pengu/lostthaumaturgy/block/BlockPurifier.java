package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.lostthaumaturgy.tile.TilePurifier;

public class BlockPurifier extends Block implements ITileEntityProvider, ITileBlock<TilePurifier>
{
	public static final PropertyEnum<Axis> PROPERTY_AXIS = PropertyEnum.create("axis", Axis.class);
	
	public BlockPurifier()
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("vis_purifier");
		setHardness(3.5F);
		setResistance(5);
	}
	
	@Override
	public Class<TilePurifier> getTileClass()
	{
		return TilePurifier.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TilePurifier();
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(PROPERTY_AXIS).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(PROPERTY_AXIS, Axis.values()[meta % Axis.values().length]);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PROPERTY_AXIS);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getStateFromMeta(EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis().ordinal());
	}
}