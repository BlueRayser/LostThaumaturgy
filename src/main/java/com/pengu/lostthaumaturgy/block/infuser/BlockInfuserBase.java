package com.pengu.lostthaumaturgy.block.infuser;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.items.ItemWand;
import com.pengu.lostthaumaturgy.net.wisp.PacketAreaWisp;

public class BlockInfuserBase extends Block
{
	public BlockInfuserBase()
	{
		super(Material.ROCK);
		setHardness(4F);
		setResistance(20F);
		setUnlocalizedName("infuser_base");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if(!stack.isEmpty() && stack.getItem() instanceof ItemWand && ItemWand.getVis(stack) > 5 * ItemWand.getVisUsage(stack))
		{
			ItemWand.removeVis(stack, 5 * ItemWand.getVisUsage(stack));
			BlockPos place = getPlaceLocation(worldIn, pos);
			if(place != null && !worldIn.isRemote)
			{
				BlockFuser.place(new WorldLocation(worldIn, place));
				HammerCore.audioProxy.playSoundAt(worldIn, LTInfo.MOD_ID + ":tinkering", place, 1F, 1F, SoundCategory.PLAYERS);
				
				HCNetwork.manager.sendToAllAround(new PacketAreaWisp(256, new AxisAlignedBB(place.add(1, 1, 1), place.add(3, 2, 3)), 1.2F, 5), new WorldLocation(worldIn, place).getPointWithRad(48));
			}
			return place != null;
		}
		return false;
	}
	
	public BlockPos getPlaceLocation(World world, BlockPos pos)
	{
		a1:
		{
			for(int x = 0; x < 2; ++x)
				for(int z = 0; z < 2; ++z)
					if(world.getBlockState(pos.add(x, 0, z)).getBlock() != this)
						break a1;
			return pos;
		}
		
		a2:
		{
			for(int x = -1; x < 1; ++x)
				for(int z = -1; z < 1; ++z)
					if(world.getBlockState(pos.add(x, 0, z)).getBlock() != this)
						break a2;
			return pos.add(-1, 0, -1);
		}
		
		a3:
		{
			for(int x = -1; x < 1; ++x)
				for(int z = 0; z < 2; ++z)
					if(world.getBlockState(pos.add(x, 0, z)).getBlock() != this)
						break a3;
			return pos.add(-1, 0, 0);
		}
		
		a4:
		{
			for(int x = 0; x < 2; ++x)
				for(int z = -1; z < 1; ++z)
					if(world.getBlockState(pos.add(x, 0, z)).getBlock() != this)
						break a4;
			return pos.add(0, 0, -1);
		}
		
		return null;
	}
}