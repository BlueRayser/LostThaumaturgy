package com.pengu.lostthaumaturgy.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.entity.EntityCustomSplashPotion;

public class ItemCustomPotion extends Item
{
	public final String[] names = { "concentrated_vis", "concentrated_taint", "potion_purity", "potion_primordial" };
	
	public ItemCustomPotion()
	{
		setUnlocalizedName("custom_potion");
		setHasSubtypes(true);
		setMaxStackSize(16);
		addPropertyOverride(new ResourceLocation("type"), (stack, world, entity) -> stack.getItemDamage());
		setMaxDamage(names.length);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + Info.MOD_ID + ":" + (stack.getItemDamage() < 0 || stack.getItemDamage() >= names.length ? "unnamed" : names[stack.getItemDamage()]);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean isRepairable()
	{
		return false;
	}
	
	@Override
	public boolean isDamageable()
	{
		return false;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
			for(int i = 0; i < names.length; ++i)
				items.add(new ItemStack(this, 1, i));
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
			int type = playerIn.getHeldItem(handIn).getItemDamage() % 4;
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