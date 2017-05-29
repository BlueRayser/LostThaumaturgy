package com.pengu.lostthaumaturgy.block.monolith;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.tile.monolith.TileMonolith;

public class BlockMonolith extends BlockRendered implements ITileEntityProvider, ITileBlock<TileMonolith>
{
	public BlockMonolith()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(-1F);
		setUnlocalizedName("lt_monolith");
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		TileMonolith tile = WorldUtil.cast(source.getTileEntity(pos), TileMonolith.class);
		if(tile != null)
			return super.getBoundingBox(state, source, pos).addCoord(0, tile.getYOffset(0), 0);
		return super.getBoundingBox(state, source, pos);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileMonolith();
	}
	
	@Override
	public Class<TileMonolith> getTileClass()
	{
		return TileMonolith.class;
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return LTInfo.MOD_ID + ":monolith/eldritch_block/0";
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		BlockPos _p = pos.up();
		if(worldIn.getBlockState(_p).getBlock() == this)
			worldIn.setBlockToAir(_p);
		
		_p = pos.down();
		if(worldIn.getBlockState(_p).getBlock() == this)
			worldIn.setBlockToAir(_p);
		
		super.breakBlock(worldIn, pos, state);
	}
}