package com.pengu.lostthaumaturgy.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.client.fx.FXGreenFlame;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.tile.TileCrucible;

public class BlockCrucible extends BlockContainer implements ITileBlock<TileCrucible>
{
	protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);
	
	public BlockCrucible()
	{
		super(Material.IRON);
		setUnlocalizedName("crucible");
		setHardness(3);
		setResistance(17);
	}
	
	@Override
	public BlockCrucible setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);
		return this;
	}
	
	@Override
	public TileCrucible createNewTileEntity(World worldIn, int meta)
	{
		TileCrucible tc = new TileCrucible();
		tc.setTier(500, .5F, .25F);
		return tc;
	}
	
	@Override
	public Class<TileCrucible> getTileClass()
	{
		return TileCrucible.class;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_)
	{
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
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
	
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn.ticksExisted % 20 == 0 && !(entityIn instanceof EntityItem))
		{
			if(!worldIn.isRemote)
			{
				HammerCore.audioProxy.playSoundAt(worldIn, "block.fire.extinguish", pos, .4F, 1.5F, SoundCategory.BLOCKS);
				TileCrucible tc = WorldUtil.cast(worldIn.getTileEntity(pos), TileCrucible.class);
				if(tc != null)
				{
					float maxAccept = tc.maxVis - tc.taintedVis - tc.pureVis;
					if(maxAccept > 0)
						tc.taintedVis += Math.min(maxAccept, 1);
					tc.sync();
				}
			}
			entityIn.attackEntityFrom(DamageSource.IN_WALL, 1F);
		}
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side.getAxis() != Axis.Y;
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
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
//		if(rand.nextInt(4) != 0) return;
		
		ParticleManager mgr = Minecraft.getMinecraft().effectRenderer;
		
		for(int i = 0; i < rand.nextInt(4); ++i)
		{
			double x = pos.getX() + .2 + rand.nextDouble() * .6;
			double y = pos.getY() + .12;
			double z = pos.getZ() + .2 + rand.nextDouble() * .6;
			
			FXGreenFlame flame = new FXGreenFlame(worldIn, x, y, z, 0, 0, 0);
			
			mgr.addEffect(flame.setScale(0.1F));
		}
	}
}