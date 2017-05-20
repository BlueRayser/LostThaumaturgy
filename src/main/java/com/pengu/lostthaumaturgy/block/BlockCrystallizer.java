package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.api.ITileBlock;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.tile.TileCrystallizer;

public class BlockCrystallizer extends BlockRendered implements ITileEntityProvider, ITileBlock<TileCrystallizer>
{
	public BlockCrystallizer()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("crystallizer");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
	    return new TileCrystallizer();
	}
	
	@Override
	public Class<TileCrystallizer> getTileClass()
	{
	    return TileCrystallizer.class;
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
		return LTInfo.MOD_ID + ":blocks/crystallizer/bottom";
	}
}