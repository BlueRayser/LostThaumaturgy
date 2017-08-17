package com.pengu.lostthaumaturgy.block.monolith;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.tile.monolith.TileCrystalReceptacle;

public class BlockCrystalReceptacle extends BlockRendered implements ITileEntityProvider, ITileBlock<TileCrystalReceptacle>
{
	public BlockCrystalReceptacle()
	{
		super(Material.IRON);
		setBlockUnbreakable();
		setUnlocalizedName("lt_receptacle");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(EnumMultiMaterialType.isNormalCrystal(playerIn.getHeldItem(hand)))
		{
			TileCrystalReceptacle r = WorldUtil.cast(worldIn.getTileEntity(pos), TileCrystalReceptacle.class);
			if(r != null && !r.INSERTED.get())
			{
				int type = playerIn.getHeldItem(hand).getItemDamage();
				int exp = r.EXPECTED_CRYSTAL.get();
				if(type == exp && !worldIn.isRemote)
					r.INSERTED.set(true);
				else if(!worldIn.isRemote)
					worldIn.createExplosion(playerIn, pos.getX() + .5, pos.getY() + 1.5, pos.getZ() + .5, 1.2F, false);
				if(!worldIn.isRemote)
					HammerCore.audioProxy.playSoundAt(worldIn, LTInfo.MOD_ID + ":place", pos, .7F, 1F, SoundCategory.BLOCKS);
				if(!playerIn.capabilities.isCreativeMode)
					playerIn.getHeldItem(hand).shrink(1);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return LTInfo.MOD_ID + ":blocks/monolith/crystal_receptacle";
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileCrystalReceptacle();
	}
	
	@Override
	public Class<TileCrystalReceptacle> getTileClass()
	{
		return TileCrystalReceptacle.class;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
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
}