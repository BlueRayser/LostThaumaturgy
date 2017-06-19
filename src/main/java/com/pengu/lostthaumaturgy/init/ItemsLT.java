package com.pengu.lostthaumaturgy.init;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

import com.pengu.lostthaumaturgy.items.ItemAuraDetector;
import com.pengu.lostthaumaturgy.items.ItemCrystallineBell;
import com.pengu.lostthaumaturgy.items.ItemCustomPotion;
import com.pengu.lostthaumaturgy.items.ItemElementalPickaxe;
import com.pengu.lostthaumaturgy.items.ItemGolemPlacer;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemSingularity;
import com.pengu.lostthaumaturgy.items.ItemThaumonomicon;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.items.ItemVoidCompass;
import com.pengu.lostthaumaturgy.items.ItemWand;
import com.pengu.lostthaumaturgy.items.ItemWandOfItemFreeze;
import com.pengu.lostthaumaturgy.items.ItemWandReversal;
import com.pengu.lostthaumaturgy.items.armor.ItemThaumiumArmor;
import com.pengu.lostthaumaturgy.items.armor.ItemVoidArmor;
import com.pengu.lostthaumaturgy.items.armor.boots.ItemBootsStriding;
import com.pengu.lostthaumaturgy.items.armor.helm.ItemGogglesRevealing;
import com.pengu.lostthaumaturgy.items.tools.pick.ItemPickaxeThaumium;

public class ItemsLT
{
	public static final ItemMultiMaterial MULTI_MATERIAL = new ItemMultiMaterial();
	public static final ItemGogglesRevealing GOGGLES_OF_REVEALING = new ItemGogglesRevealing();
	public static final ItemBootsStriding BOOTS_STRIDING = new ItemBootsStriding();
	public static final Item //
	        CRYSTALLINE_BELL = new ItemCrystallineBell(), //
	        DISCOVERY = new ItemResearch(), //
	        THAUMONOMICON = new ItemThaumonomicon(), //
	        THAUMIUM_HELMET = new ItemThaumiumArmor(EntityEquipmentSlot.HEAD), //
	        THAUMIUM_CHESTPLATE = new ItemThaumiumArmor(EntityEquipmentSlot.CHEST), //
	        THAUMIUM_LEGGINGS = new ItemThaumiumArmor(EntityEquipmentSlot.LEGS), //
	        THAUMIUM_BOOTS = new ItemThaumiumArmor(EntityEquipmentSlot.FEET), //
	        VOID_HELMET = new ItemVoidArmor(EntityEquipmentSlot.HEAD), //
	        VOID_CHESTPLATE = new ItemVoidArmor(EntityEquipmentSlot.CHEST), //
	        VOID_LEGGINGS = new ItemVoidArmor(EntityEquipmentSlot.LEGS), //
	        VOID_BOOTS = new ItemVoidArmor(EntityEquipmentSlot.FEET), //
	        WAND_ITEM_FREEZE = new ItemWandOfItemFreeze(), //
	        WAND_REVERSAL = new ItemWandReversal(), //
	        SINGULARITY = new ItemSingularity(), //
	        CUSTOM_POTION = new ItemCustomPotion(), //
	        AURA_DETECTOR = new ItemAuraDetector(), //
	        VOID_COMPASS = new ItemVoidCompass(), //
	        // GOLEM_PLACER = new ItemGolemPlacer(), //
	        WAND = ItemWand.WAND;
	public static final ItemTool //
	        THAUMIUM_PICKAXE = new ItemPickaxeThaumium(), //
	        ELEMENTAL_PICKAXE = new ItemElementalPickaxe();
	public static final ItemUpgrade //
	        QUICKSILVER_CORE = new ItemUpgrade("quicksilver_core"), //
	        STABILIZED_SINGULARITY = new ItemUpgrade("stabilized_singularity"), //
	        HARNESSED_RAGE = new ItemUpgrade("harnessed_rage"), //
	        CONCENTRATED_EVIL = new ItemUpgrade("concentrated_evil"), //
	        INFINITE_SADNESS = new ItemUpgrade("infinite_saddness"), //
	        CONTAINED_EMPTINESS = new ItemUpgrade("contained_emptiness"), //
	        COLLECTED_WISDOM = new ItemUpgrade("collected_wisdom"), //
	        CONCENTRATED_PURITY = new ItemUpgrade("concentrated_purity");
}