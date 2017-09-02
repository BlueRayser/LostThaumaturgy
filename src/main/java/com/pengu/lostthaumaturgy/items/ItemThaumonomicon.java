package com.pengu.lostthaumaturgy.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.LostThaumaturgy;

public class ItemThaumonomicon extends Item
{
	public ItemThaumonomicon()
	{
		setUnlocalizedName("thaumonomicon");
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(worldIn.isRemote)
			LostThaumaturgy.proxy.openThaumonomicon();
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}
}