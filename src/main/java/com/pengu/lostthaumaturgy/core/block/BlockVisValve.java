package com.pengu.lostthaumaturgy.core.block;

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
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.block.def.BlockTraceableRendered;
import com.pengu.lostthaumaturgy.core.tile.TileConduit;
import com.pengu.lostthaumaturgy.core.tile.TileVisValve;

public class BlockVisValve extends BlockTraceableRendered implements ITileEntityProvider, ITileBlock<TileVisValve>, ICubeManager
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
		TileVisValve tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileVisValve.class);
		if(tile != null)
		{
			if(!worldIn.isRemote)
				HammerCore.audioProxy.playSoundAt(worldIn, "block.lever.click", pos, 1F, tile.open ? .4F : .6F, SoundCategory.PLAYERS);
			tile.open = !tile.open;
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
		TileVisValve valve = WorldUtil.cast(world.getTileEntity(pos), TileVisValve.class);
		return Info.MOD_ID + ":blocks/vis_valve_" + (valve != null && valve.open ? "off" : "on");
	}
}