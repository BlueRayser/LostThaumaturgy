package com.pengu.lostthaumaturgy.block;

import java.util.HashMap;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.tile.TileBellows;
import com.pengu.lostthaumaturgy.tile.TileThaumiumBellows;

public class BlockThaumiumBellows extends BlockRendered implements ITileBlock<TileThaumiumBellows>, ITileEntityProvider
{
	public BlockThaumiumBellows()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("thaumium_bellows");
		setHardness(4.5F);
		setHarvestLevel("pickaxe", 1);
		setResistance(4F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileThaumiumBellows();
	}
	
	@Override
	public Class<TileThaumiumBellows> getTileClass()
	{
		return TileThaumiumBellows.class;
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
	
	private static final HashMap<String, EnumFacing> placings = new HashMap<>();
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		if(!placer.isSneaking())
			placings.put(pos.toString() + "|" + world.provider.getDimension(), EnumFacing.fromAngle(placer.rotationYawHead));
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		EnumFacing place = placings.remove(pos.toString() + "|" + worldIn.provider.getDimension());
		
		TileBellows bellows = WorldUtil.cast(worldIn.getTileEntity(pos), TileBellows.class);
		if(bellows == null)
		{
			bellows = new TileBellows();
			worldIn.setTileEntity(pos, bellows);
		}
		
		if(place == null)
			for(EnumFacing suggested : EnumFacing.VALUES)
			{
				if(suggested.getAxis() == Axis.Y)
					continue;
				bellows.orientation = suggested.ordinal() - 2;
				if(bellows.isBoosting())
					break;
			}
		
		if(place != null)
			bellows.orientation = place.ordinal() - 2;
		bellows.sync();
		
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return LTInfo.MOD_ID + ":blocks/thaumium_bellows";
	}
}