package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.gui.GuiManager;
import com.pengu.lostthaumaturgy.tile.TileInfuser;

public class BlockInfuser extends BlockContainer implements ITileBlock<TileInfuser>
{
	public BlockInfuser()
	{
		super(Material.ROCK);
		setUnlocalizedName("infuser");
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 1);
		setHardness(1.5F);
		setResistance(4F);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		GuiManager.openGui(playerIn, WorldUtil.cast(worldIn.getTileEntity(pos), TileInfuser.class));
	    return true;
	}
	
	@Override
	public TileInfuser createNewTileEntity(World worldIn, int meta)
	{
		return new TileInfuser();
	}
	
	@Override
	public Class<TileInfuser> getTileClass()
	{
		return TileInfuser.class;
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
		TileInfuser infuser = WorldUtil.cast(worldIn.getTileEntity(pos), TileInfuser.class);
		if(infuser != null) infuser.infuserItemStacks.drop(worldIn, pos);
	    super.breakBlock(worldIn, pos, state);
	}
}