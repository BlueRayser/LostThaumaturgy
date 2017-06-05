package com.pengu.lostthaumaturgy.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.entity.EntityCustomSplashPotion;

public class ItemCustomPotion extends MultiVariantItem
{
	public ItemCustomPotion()
	{
		super("custom_potion", "concentrated_vis", "concentrated_taint", "potion_purity");
		insertPrefix(LTInfo.MOD_ID + ":");
		setMaxStackSize(16);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(!worldIn.isRemote)
		{
			int type = playerIn.getHeldItem(handIn).getItemDamage() % 3;
			EntityCustomSplashPotion potion = new EntityCustomSplashPotion(worldIn, playerIn);
			potion.setType(type);
			worldIn.spawnEntity(potion);
			if(!playerIn.capabilities.isCreativeMode)
				playerIn.getHeldItem(handIn).shrink(1);
			HammerCore.audioProxy.playSoundAt(worldIn, "entity.witch.throw", playerIn.getPosition(), .3F, .7F, SoundCategory.PLAYERS);
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}