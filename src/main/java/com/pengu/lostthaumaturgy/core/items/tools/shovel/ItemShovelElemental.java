package com.pengu.lostthaumaturgy.core.items.tools.shovel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.blocks.ITaintedBlock;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp1;

public class ItemShovelElemental extends ItemSpade
{
	public static Set<IBlockState> areaMinedBlocks = new HashSet<>();
	
	static
	{
		areaMinedBlocks.add(Blocks.DIRT.getStateFromMeta(0));
		areaMinedBlocks.add(Blocks.DIRT.getStateFromMeta(1));
		areaMinedBlocks.add(Blocks.DIRT.getStateFromMeta(2));
		areaMinedBlocks.add(Blocks.GRASS.getDefaultState());
		areaMinedBlocks.add(Blocks.GRAVEL.getDefaultState());
		areaMinedBlocks.add(Blocks.SAND.getStateFromMeta(0));
		areaMinedBlocks.add(Blocks.SAND.getStateFromMeta(1));
	}
	
	public ItemShovelElemental()
	{
		super(ItemMaterialsLT.tool_elemental);
		setUnlocalizedName("elemental_shovel");
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		if(!player.isSneaking())
		{
			WorldLocation loc = new WorldLocation(player.world, pos);
			RayTraceResult res = RayTracer.retrace(player);
			if(res != null && res.getBlockPos().equals(pos) && areaMinedBlocks.contains(loc.getState()))
			{
				EnumFacing side = res.sideHit;
				boolean doesX = side.getFrontOffsetX() == 0;
				boolean doesY = side.getFrontOffsetY() == 0;
				boolean doesZ = side.getFrontOffsetZ() == 0;
				
				if(player.isServerWorld())
					for(int x = (doesX ? -1 : 0); x < (doesX ? 2 : 1); ++x)
						for(int y = (doesY ? -1 : 0); y < (doesY ? 2 : 1); ++y)
							for(int z = (doesZ ? -1 : 0); z < (doesZ ? 2 : 1); ++z)
							{
								BlockPos p = pos.add(x, y, z);
								IBlockState state = loc.getWorld().getBlockState(p);
								if(areaMinedBlocks.contains(state))
								{
									List<EntityItem> before = loc.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p));
									loc.getWorld().destroyBlock(p, !player.capabilities.isCreativeMode);
									List<EntityItem> after = loc.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p));
									after.removeAll(before);
									
									for(EntityItem e : after)
									{
										e.setNoPickupDelay();
										e.setPositionAndUpdate(player.posX, player.posY, player.posZ);
									}
									
									itemstack.damageItem(1, player);
									for(int i = 0; i < 8; ++i)
										HCNetwork.manager.sendToAllAround(new PacketFXWisp1(p.getX() + player.getRNG().nextFloat(), p.getY() + player.getRNG().nextFloat(), p.getZ() + player.getRNG().nextFloat(), .8F, 3), loc.getPointWithRad(48));
								}
							}
				
				return true;
			}
		}
		return false;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(ItemDye.applyBonemeal(ItemStack.EMPTY, worldIn, pos))
		{
			if(!worldIn.isRemote)
				worldIn.playEvent(2005, pos, 0);
			player.getHeldItem(hand).damageItem(10, player);
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.FAIL;
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		float str = super.getStrVsBlock(stack, state);
		if(state.getBlock() instanceof ITaintedBlock)
			str *= 15F;
		return str;
	}
}