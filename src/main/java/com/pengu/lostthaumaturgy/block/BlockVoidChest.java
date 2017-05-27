package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.gui.GuiManager;
import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.tile.TileVoidChest;

public class BlockVoidChest extends Block implements ITileBlock<TileVoidChest>, ITileEntityProvider
{
	public BlockVoidChest()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("void_chest");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileVoidChest();
	}
	
	@Override
	public Class<TileVoidChest> getTileClass()
	{
		return TileVoidChest.class;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		GuiManager.openGui(playerIn, WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class));
		if(!worldIn.isRemote)
			HammerCore.audioProxy.playSoundAt(worldIn, LTInfo.MOD_ID + ":void_chest_open", pos, .3F, 1F, SoundCategory.BLOCKS);
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileVoidChest vc = WorldUtil.cast(worldIn.getTileEntity(pos), TileVoidChest.class);
		if(vc != null)
			vc.inventory.drop(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
}