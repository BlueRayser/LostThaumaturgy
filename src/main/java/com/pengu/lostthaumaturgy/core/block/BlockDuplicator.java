package com.pengu.lostthaumaturgy.core.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.gui.GuiManager;
import com.pengu.hammercore.tile.TileSyncable;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.core.tile.TileDuplicator;

public class BlockDuplicator extends BlockRendered implements ITileEntityProvider, ITileBlock<TileDuplicator>
{
	public BlockDuplicator()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(2F);
		setHarvestLevel("pickaxe", 1);
		setUnlocalizedName("duplicator");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileDuplicator();
	}
	
	@Override
	public Class<TileDuplicator> getTileClass()
	{
		return TileDuplicator.class;
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return Info.MOD_ID + ":blocks/duplicator/side";
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		GuiManager.openGui(playerIn, WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class));
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		EnumFacing side = placer.getHorizontalFacing().getOpposite();
		WorldLocation loc = new WorldLocation(worldIn, pos);
		TileDuplicator tile = loc.getTileOfType(TileDuplicator.class);
		if(tile == null)
			loc.setTile(tile = new TileDuplicator());
		tile.orientation.set(side.ordinal());
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		WorldLocation loc = new WorldLocation(worldIn, pos);
		TileDuplicator tile = loc.getTileOfType(TileDuplicator.class);
		if(tile != null)
			tile.inventory.drop(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
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