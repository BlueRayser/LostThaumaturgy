package com.pengu.lostthaumaturgy.block.wood.silverwood;

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
import com.pengu.hammercore.api.mhb.ICubeManager;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.vec.Cuboid6;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockTraceableRendered;
import com.pengu.lostthaumaturgy.tile.TileSilverwoodConduit;

public class BlockSilverwoodConduit extends BlockTraceableRendered implements ITileEntityProvider, ITileBlock<TileSilverwoodConduit>, ICubeManager
{
	public BlockSilverwoodConduit()
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("silverwood_conduit");
		setHardness(.2F);
		setResistance(4F);
	}
	
	@Override
	public Class<TileSilverwoodConduit> getTileClass()
	{
		return TileSilverwoodConduit.class;
	}
	
	@Override
	public TileSilverwoodConduit createNewTileEntity(World worldIn, int meta)
	{
		return new TileSilverwoodConduit();
	}
	
	private static final AxisAlignedBB AABB = new AxisAlignedBB(6 / 16D, 6 / 16D, 6 / 16D, 10 / 16D, 10 / 16D, 10 / 16D);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return AABB;
	}
	
	@Override
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state)
	{
		TileSilverwoodConduit conduit = WorldUtil.cast(world.getTileEntity(pos), TileSilverwoodConduit.class);
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
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		TileSilverwoodConduit conduit = WorldUtil.cast(world.getTileEntity(pos), TileSilverwoodConduit.class);
		if(conduit != null)
			conduit.rebake();
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
		return LTInfo.MOD_ID + ":blocks/silverwood_vis_conduit";
	}
}