package com.pengu.lostthaumaturgy.items;

import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import com.pengu.lostthaumaturgy.LTInfo;

public class ItemThaumiumArmor extends ItemArmor
{
	public static final ArmorMaterial thaumium = EnumHelper.addArmorMaterial(LTInfo.MOD_ID + ":thaumium", LTInfo.MOD_ID + ":textures/armor/thaumium_1.png", 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	
	public ItemThaumiumArmor(EntityEquipmentSlot equipmentSlotIn)
	{
		super(thaumium, 2, equipmentSlotIn);
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