package com.pengu.lostthaumaturgy.block.monolith;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.ListUtils;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.event.FillVoidChestEvent;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.net.wisp.PacketAreaWisp;
import com.pengu.lostthaumaturgy.tile.TileVoidChest;
import com.pengu.lostthaumaturgy.tile.monolith.TileExtraRoom;

public class BlockExtraRoom extends BlockRendered implements ITileBlock<TileExtraRoom>, ITileEntityProvider
{
	public BlockExtraRoom()
	{
		super(Material.ROCK);
		setBlockUnbreakable();
		setUnlocalizedName("lt_extra_room");
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileExtraRoom tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileExtraRoom.class);
		
		facing = facing.getOpposite();
		if(tile == null || tile.orientation.get() != facing.ordinal())
			return false;
		
		boolean key = playerIn.getHeldItem(hand).isItemEqual(EnumMultiMaterialType.ELDRITCH_KEYSTONE_TLHUTLH.stack());
		
		if(key && !worldIn.isRemote)
		{
			EnumFacing f = EnumFacing.VALUES[tile.orientation.get()];
			BlockPos center = pos.offset(f, 7);
			
			for(int y = -3; y < 4; ++y)
				for(int x = -6; x < 7; ++x)
					for(int z = -6; z < 7; ++z)
					{
						BlockPos p = center.add(x, y, z);
						
						boolean wall = x == -6 || z == -6 || x == 6 || z == 6;
						wall |= (y == -2 || y == 2) && (x == -5 || z == -5 || x == 5 || z == 5) && (x < -1 || x > 1) && (z < -1 || z > 1);
						
						worldIn.setBlockState(p, (y == -3 || y == 3 || wall ? BlocksLT.ELDRITCH_BLOCK : Blocks.AIR).getDefaultState());
					}
			
			worldIn.setBlockState(center.down(), BlocksLT.MONOLITH.getDefaultState());
			worldIn.setBlockState(center, BlocksLT.MONOLITH.getDefaultState());
			worldIn.setBlockState(center.up(), BlocksLT.MONOLITH.getDefaultState());
			
			facing = facing.rotateY();
			
			int x1 = Math.min(pos.getX() - facing.getFrontOffsetX(), pos.getX() + facing.getFrontOffsetX());
			int y1 = pos.getY() - 1;
			int z1 = Math.min(pos.getZ() - facing.getFrontOffsetZ(), pos.getZ() + facing.getFrontOffsetZ());
			int x2 = Math.max(pos.getX() - facing.getFrontOffsetX(), pos.getX() + facing.getFrontOffsetX());
			int y2 = pos.getY() + 1;
			int z2 = Math.max(pos.getZ() - facing.getFrontOffsetZ(), pos.getZ() + facing.getFrontOffsetZ());
			
			for(int x = x1; x <= x2; ++x)
				for(int y = y1; y <= y2; ++y)
					for(int z = z1; z <= z2; ++z)
					{
						BlockPos p2 = new BlockPos(x, y, z);
						worldIn.setBlockToAir(p2);
						worldIn.setBlockToAir(p2.offset(facing.rotateYCCW()));
					}
			
			List<BlockPos> positions = ListUtils.randomizeList(Arrays.asList(center.add(-3, -2, -3), center.add(-3, -2, 3), center.add(3, -2, -3), center.add(3, -2, 3)), worldIn.rand);
			int chests = worldIn.rand.nextInt(5);
			for(int i = 0; i < chests && !positions.isEmpty(); ++i)
			{
				BlockPos p = positions.remove(0);
				worldIn.setBlockState(p, BlocksLT.VOID_CHEST.getDefaultState());
				TileVoidChest chest = WorldUtil.cast(worldIn.getTileEntity(p), TileVoidChest.class);
				if(chest == null)
					worldIn.setTileEntity(p, chest = new TileVoidChest());
				MinecraftForge.EVENT_BUS.post(new FillVoidChestEvent(worldIn, p, chest));
			}
			
			HammerCore.audioProxy.playSoundAt(worldIn, LTInfo.MOD_ID + ":rumble", pos, 4F, 1F, SoundCategory.BLOCKS);
			HCNetwork.getManager("particles").sendToAllAround(new PacketAreaWisp(180, new AxisAlignedBB(x1 + .5, y1 + 1.5, z1 + .5, x2 + 1.5, y2 + 1.5, z2 + 1.5), 2F, 5), getSyncPoint(worldIn, pos, 48));
		}
		
		return key;
	}
	
	public TargetPoint getSyncPoint(World world, BlockPos pos, int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileExtraRoom();
	}
	
	@Override
	public Class<TileExtraRoom> getTileClass()
	{
		return TileExtraRoom.class;
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return LTInfo.MOD_ID + ":blocks/eldritch_block/1";
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