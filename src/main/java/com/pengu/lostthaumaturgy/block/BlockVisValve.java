package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.tile.TileVisValve;

public class BlockVisValve extends BlockContainer implements ITileEntityProvider, ITileBlock<TileVisValve>
{
	public BlockVisValve()
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("vis_valve");
		setHardness(.2F);
		setResistance(4F);
	}
	
	@Override
	public Class<TileVisValve> getTileClass()
	{
		return TileVisValve.class;
	}
	
	@Override
	public TileVisValve createNewTileEntity(World worldIn, int meta)
	{
		return new TileVisValve();
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
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		AuraTicker.spillTaint(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	public static final AxisAlignedBB VALVE_AABB = new AxisAlignedBB(3.5 / 16, 3.5 / 16, 3.5 / 16, 12.5 / 16, 12.5 / 16, 12.5 / 16);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return VALVE_AABB;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileVisValve tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileVisValve.class);
		if(tile != null)
		{
			if(!worldIn.isRemote)
				HammerCore.audioProxy.playSoundAt(worldIn, "block.lever.click", pos, 1F, tile.open ? .4F : .6F, SoundCategory.PLAYERS);
			tile.open = !tile.open;
			tile.sync(); //sync to others
			return true;
		}
		return false;
	}
}