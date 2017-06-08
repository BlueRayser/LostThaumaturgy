package com.pengu.lostthaumaturgy.init;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

import com.pengu.lostthaumaturgy.LTInfo;

public class ItemMaterialsLT
{
	public static final ArmorMaterial armor_goggles = EnumHelper.addArmorMaterial(LTInfo.MOD_ID + ":goggles", LTInfo.MOD_ID + ":textures/armor/goggles.png", 5, new int[] { 1, 2, 3, 1 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	public static final ArmorMaterial armor_thaumium = EnumHelper.addArmorMaterial(LTInfo.MOD_ID + ":thaumium", LTInfo.MOD_ID + ":textures/armor/thaumium_1.png", 35, new int[] { 4, 6, 7, 4 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	public static final ArmorMaterial armor_void = EnumHelper.addArmorMaterial(LTInfo.MOD_ID + ":void", LTInfo.MOD_ID + ":textures/armor/void_1.png", 30, new int[] { 4, 7, 8, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
	public static final ToolMaterial tool_elemental = EnumHelper.addToolMaterial(LTInfo.MOD_ID + ":elemental", 4, 561, 10F, 4F, 10);
	public static final ToolMaterial tool_thaumium = EnumHelper.addToolMaterial(LTInfo.MOD_ID + ":thaumium", 3, 250, 6F, 2F, 10);
}