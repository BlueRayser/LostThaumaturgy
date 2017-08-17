package com.pengu.lostthaumaturgy.items.tools.hoe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp1;

public class ItemHoeElemental extends ItemHoe
{
	public ItemHoeElemental()
	{
		super(ItemMaterialsLT.tool_elemental);
		setUnlocalizedName("elemental_hoe");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		
		cc: for(int left = 0; left < 50; ++left)
		{
			BlockPos sel = null;
			int tries = 45;
			c2: while(tries > 0)
			{
				BlockPos chosen = pos.add(player.getRNG().nextInt(5) - player.getRNG().nextInt(5), 0, player.getRNG().nextInt(5) - player.getRNG().nextInt(5));
				IBlockState state = worldIn.getBlockState(chosen);
				
				if(state.getBlock() == Blocks.GRASS_PATH || state.getBlock() == Blocks.GRASS)
				{
					sel = chosen;
					break;
				}
				
				if(state.getBlock() == Blocks.DIRT)
					switch((BlockDirt.DirtType) state.getValue(BlockDirt.VARIANT))
					{
					case DIRT:
						sel = chosen;
					break c2;
					case COARSE_DIRT:
						sel = chosen;
					break c2;
					default:
					break;
					}
				
				--tries;
				if(tries == 0)
					continue cc;
			}
			
			if(sel != null)
				addTillPos(player.getHeldItem(hand), sel);
		}
		
		return EnumActionResult.PASS;
	}
	
	public static void addTillPos(ItemStack stack, BlockPos pos)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagList list = stack.getTagCompound().getTagList("ToTill", NBT.TAG_LONG);
		list.appendTag(new NBTTagLong(pos.toLong()));
		stack.getTagCompound().setTag("ToTill", list);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(!(entityIn instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) entityIn;
		
		int tillsSkip = 25;
		
		cc: while(stack.hasTagCompound() && stack.getTagCompound().hasKey("ToTill", NBT.TAG_LIST) && tillsSkip-- > 0)
		{
			NBTTagList toTill = stack.getTagCompound().getTagList("ToTill", NBT.TAG_LONG);
			if(toTill.tagCount() > 0)
			{
				NBTTagLong longPos = WorldUtil.cast(toTill.get(0), NBTTagLong.class);
				if(longPos != null)
				{
					BlockPos pos = BlockPos.fromLong(longPos.getLong());
					if(worldIn.isBlockLoaded(pos))
					{
						toTill.removeTag(0);
						
						IBlockState iblockstate = worldIn.getBlockState(pos);
						Block block = iblockstate.getBlock();
						
						if(worldIn.isAirBlock(pos.up()))
						{
							IBlockState state = null;
							
							if(block == Blocks.GRASS || block == Blocks.GRASS_PATH)
								setBlock(stack, player, worldIn, pos, state = Blocks.FARMLAND.getDefaultState());
							
							if(block == Blocks.DIRT)
							{
								switch((BlockDirt.DirtType) iblockstate.getValue(BlockDirt.VARIANT))
								{
								case DIRT:
									setBlock(stack, player, worldIn, pos, state = Blocks.FARMLAND.getDefaultState());
								break;
								case COARSE_DIRT:
									setBlock(stack, player, worldIn, pos, state = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
								break;
								default:
								break;
								}
							}
							
							if(state != null)
							{
								if(!worldIn.isRemote)
									for(int i = 0; i < 8; ++i)
										HCNetwork.manager.sendToAllAround(new PacketFXWisp1(pos.getX() + player.getRNG().nextFloat(), pos.getY() + 14 / 16D + player.getRNG().nextFloat() * (1 / 8D), pos.getZ() + player.getRNG().nextFloat(), .8F, 3), new WorldLocation(worldIn, pos).getPointWithRad(48));
								break cc;
							} else
								continue cc;
						} else
							continue cc;
					}
				}
			}
		}
	}
}