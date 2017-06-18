package com.pengu.lostthaumaturgy.items.armor.boots;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.items.ISpeedBoots;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;

public class ItemBootsStriding extends ItemArmor implements ISpeedBoots
{
	public ItemBootsStriding()
	{
		super(ItemMaterialsLT.armor_boots_striding, 2, EntityEquipmentSlot.FEET);
		setUnlocalizedName("boots_striding");
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return LTInfo.MOD_ID + ":textures/armor/boots_striding.png";
	}
	
	@Override
	public boolean isRepairable()
	{
		return true;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return repair.getItem() == Items.LEATHER;
	}
	
	@Override
	public float getWalkBoost(ItemStack boots)
	{
		return .1F;
	}
	
	@Override
	public float getJumpMod(ItemStack boots)
	{
		return .7F;
	}
	
	@Override
	public float getStepAssist(ItemStack boots)
	{
		return 1;
	}
}