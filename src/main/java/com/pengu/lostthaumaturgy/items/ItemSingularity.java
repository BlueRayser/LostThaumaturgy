package com.pengu.lostthaumaturgy.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import com.mrdimka.hammercore.common.utils.ChatUtil;

public class ItemSingularity extends Item
{
	public ItemSingularity()
	{
		setMaxStackSize(16);
		setUnlocalizedName("singularity");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(worldIn.isRemote)
			ChatUtil.sendNoSpamClient("WIP, not working atm.");
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}