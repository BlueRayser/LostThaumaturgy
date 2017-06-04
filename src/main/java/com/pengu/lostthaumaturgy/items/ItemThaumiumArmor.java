package com.pengu.lostthaumaturgy.items;

import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;

public class ItemThaumiumArmor extends ItemArmor
{
	public ItemThaumiumArmor(EntityEquipmentSlot equipmentSlotIn)
	{
		super(ItemMaterialsLT.armor_thaumium, 2, equipmentSlotIn);
		if(equipmentSlotIn == EntityEquipmentSlot.HEAD)
			setUnlocalizedName("thaumium_helmet");
		if(equipmentSlotIn == EntityEquipmentSlot.CHEST)
			setUnlocalizedName("thaumium_chestplate");
		if(equipmentSlotIn == EntityEquipmentSlot.LEGS)
			setUnlocalizedName("thaumium_leggings");
		if(equipmentSlotIn == EntityEquipmentSlot.FEET)
			setUnlocalizedName("thaumium_boots");
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return LTInfo.MOD_ID + ":textures/armor/thaumium_" + (slot == EntityEquipmentSlot.LEGS ? 2 : 1) + ".png";
	}
}