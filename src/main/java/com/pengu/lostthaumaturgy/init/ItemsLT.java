package com.pengu.lostthaumaturgy.init;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

import com.pengu.lostthaumaturgy.items.ItemCrystallineBell;
import com.pengu.lostthaumaturgy.items.ItemGogglesRevealing;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemThaumiumArmor;
import com.pengu.lostthaumaturgy.items.ItemThaumonomicon;
import com.pengu.lostthaumaturgy.items.ItemWandOfItemFreeze;

public class ItemsLT
{
	public static final ItemMultiMaterial MULTI_MATERIAL = new ItemMultiMaterial();
	public static final ItemGogglesRevealing GOGGLES_OF_REVEALING = new ItemGogglesRevealing();
	public static final Item //
	        CRYSTALLINE_BELL = new ItemCrystallineBell(), //
	        DISCOVERY = new ItemResearch(), //
	        THAUMONOMICON = new ItemThaumonomicon(), //
	        THAUMIUM_HELMET = new ItemThaumiumArmor(EntityEquipmentSlot.HEAD), //
	        THAUMIUM_CHESTPLATE = new ItemThaumiumArmor(EntityEquipmentSlot.CHEST), //
	        THAUMIUM_LEGGINGS = new ItemThaumiumArmor(EntityEquipmentSlot.LEGS), //
	        THAUMIUM_BOOTS = new ItemThaumiumArmor(EntityEquipmentSlot.FEET), //
	        WAND_ITEM_FREEZE = new ItemWandOfItemFreeze();
}