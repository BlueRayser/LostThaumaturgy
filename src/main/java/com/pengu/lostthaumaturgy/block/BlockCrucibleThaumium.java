package com.pengu.lostthaumaturgy.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.proxy.ParticleProxy_Client;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.client.fx.FXGreenFlame;
import com.pengu.lostthaumaturgy.tile.TileCrucible;
import com.pengu.lostthaumaturgy.tile.TileCrucibleThaumium;

public class BlockCrucibleThaumium extends BlockRendered implements ITileBlock<TileCrucibleThaumium>, ITileEntityProvider
{
	protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);
	
	public BlockCrucibleThaumium()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("crucible_thaumium");
		setHardness(3);
		setResistance(17);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileCrucibleThaumium();
	}
	
	@Override
	public Class<TileCrucibleThaumium> getTileClass()
	{
		return TileCrucibleThaumium.class;
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
		if(entityIn.ticksExisted % 5 == 0 && !(entityIn instanceof EntityItem))
		{
			if(!worldIn.isRemote)
			{
				TileCrucible tc = WorldUtil.cast(worldIn.getTileEntity(pos), TileCrucible.class);
				if(tc != null)
				{
					float maxAccept = tc.maxVis - tc.taintedVis - tc.pureVis;
					if(maxAccept > 0)
					{
						if(entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).getGameProfile().getName().equals("APengu"))
						{
							tc.pureVis += Math.min(maxAccept, 1);
							if(worldIn.rand.nextInt(5) == 0)
								HammerCore.audioProxy.playSoundAt(worldIn, LTInfo.MOD_ID + ":creaking", pos, .4F, 1.5F, SoundCategory.BLOCKS);
						} else
						{
							tc.taintedVis += Math.min(maxAccept, 1);
							entityIn.attackEntityFrom(DamageSource.IN_WALL, 1F);
							HammerCore.audioProxy.playSoundAt(worldIn, "block.fire.extinguish", pos, .4F, 1.5F, SoundCategory.BLOCKS);
						}
						tc.sync();
						HCNetwork.spawnParticle(worldIn, EnumParticleTypes.SMOKE_LARGE, entityIn.posX, entityIn.posY, entityIn.posZ, 0, 0, 0);
					}
				}
			}
		}
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		// if(rand.nextInt(4) != 0) return;
		
		for(int i = 0; i < rand.nextInt(4); ++i)
		{
			double x = pos.getX() + .2 + rand.nextDouble() * .6;
			double y = pos.getY() + .12;
			double z = pos.getZ() + .2 + rand.nextDouble() * .6;
			
			FXGreenFlame flame = new FXGreenFlame(worldIn, x, y, z, 0, 0, 0);
			
			ParticleProxy_Client.queueParticleSpawn(flame.setScale(0.1F));
		}
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		TileCrucibleThaumium eyes = WorldUtil.cast(world.getTileEntity(pos), TileCrucibleThaumium.class);
		return LTInfo.MOD_ID + ":blocks/crucibles/thaumium/crucible_side_connected_" + (eyes != null && eyes.emitsPower() ? "on" : "off");
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileCrucibleThaumium eyes = WorldUtil.cast(blockAccess.getTileEntity(pos), TileCrucibleThaumium.class);
		if(eyes != null)
			return eyes.emitsPower() ? 15 : 0;
		return 0;
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return getStrongPower(blockState, blockAccess, pos, side);
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileCrucible c = WorldUtil.cast(worldIn.getTileEntity(pos), TileCrucible.class);
		if(c != null)
			return Math.round((c.pureVis + c.taintedVis) / c.maxVis * 15F);
		return 0;
	}
}