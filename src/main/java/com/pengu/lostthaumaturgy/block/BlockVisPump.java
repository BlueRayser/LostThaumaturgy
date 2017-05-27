package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.tile.TileVisPump;

public class BlockVisPump extends BlockRendered implements ITileBlock<TileVisPump>, ITileEntityProvider
{
	public BlockVisPump()
	{
		super(Material.WOOD);
		setUnlocalizedName("vis_pump");
		setSoundType(SoundType.WOOD);
		setHardness(1.5F);
		setResistance(4F);
	}
	
	@Override
	public TileVisPump createNewTileEntity(World worldIn, int meta)
	{
		return new TileVisPump();
	}
	
	@Override
	public Class<TileVisPump> getTileClass()
	{
		return TileVisPump.class;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileVisPump pump = WorldUtil.cast(worldIn.getTileEntity(pos), TileVisPump.class);
		if(pump == null)
		{
			pump = createNewTileEntity(worldIn, stack.getItemDamage());
			worldIn.setTileEntity(pos, pump);
		}
		
		pump.orientation = EnumFacing.getDirectionFromEntityLiving(pos, placer);
		if(placer.isSneaking())
			pump.orientation = pump.orientation.getOpposite();
		if(!worldIn.isRemote)
			pump.sync();
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
		boolean working = true;
		TileVisPump pump = WorldUtil.cast(world.getTileEntity(pos), TileVisPump.class);
		if(pump != null)
			working = !pump.gettingPower();
		return LTInfo.MOD_ID + ":blocks/vis_pump_o" + (working ? "n" : "ff");
	}
}