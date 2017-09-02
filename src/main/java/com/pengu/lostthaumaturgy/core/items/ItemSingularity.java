package com.pengu.lostthaumaturgy.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.core.entity.EntitySingularity;

public class ItemSingularity extends Item
{
	public ItemSingularity()
	{
		setMaxStackSize(16);
		setUnlocalizedName("singularity");
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(!worldIn.isRemote)
		{
			worldIn.spawnEntity(new EntitySingularity(worldIn, playerIn));
			playerIn.getHeldItem(handIn).shrink(1);
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}