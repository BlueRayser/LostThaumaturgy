package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.api.mhb.ICubeManager;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.vec.Cuboid6;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockTraceableRendered;
import com.pengu.lostthaumaturgy.tile.TileAdvancedVisValve;
import com.pengu.lostthaumaturgy.tile.TileConduit;

public class BlockAdvancedVisValve extends BlockTraceableRendered implements ITileEntityProvider, ITileBlock<TileAdvancedVisValve>, ICubeManager
{
	public BlockAdvancedVisValve()
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("advanced_vis_valve");
		setHardness(.2F);
		setResistance(4F);
	}
	
	@Override
	public Class<TileAdvancedVisValve> getTileClass()
	{
		return TileAdvancedVisValve.class;
	}
	
	@Override
	public TileAdvancedVisValve createNewTileEntity(World worldIn, int meta)
	{
		return new TileAdvancedVisValve();
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileAdvancedVisValve tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileAdvancedVisValve.class);
		if(tile != null)
		{
			if(!worldIn.isRemote)
				HammerCore.audioProxy.playSoundAt(worldIn, "block.lever.click", pos, 1F, tile.setting == 2 ? .4F : .6F, SoundCategory.PLAYERS);
			tile.prevSetting = tile.setting;
			tile.setting = (tile.setting + 1) % 3;
			tile.tick();
			tile.sync(); // sync to others
			return true;
		}
		return false;
	}
	
	@Override
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state)
	{
		TileConduit conduit = WorldUtil.cast(world.getTileEntity(pos), TileConduit.class);
		if(conduit != null)
		{
			if(conduit.hitboxes == null || conduit.hitboxes.length == 0)
				conduit.rebake();
			return conduit.hitboxes;
		}
		
		double bp = 6 / 16D;
		double ep = 10 / 16D;
		return new Cuboid6[] { new Cuboid6(bp, bp, bp, ep, ep, ep) };
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		int open = 0;
		TileAdvancedVisValve tile = WorldUtil.cast(world.getTileEntity(pos), TileAdvancedVisValve.class);
		if(tile != null)
			open = tile.setting;
		return LTInfo.MOD_ID + ":blocks/advanced_vis_valve_" + (open == 0 ? "off" : open == 2 ? "taint" : "vis");
	}
}