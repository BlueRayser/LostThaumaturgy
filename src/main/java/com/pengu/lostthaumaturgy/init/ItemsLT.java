package com.pengu.lostthaumaturgy.init;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.items.DefaultSealSymbol;
import com.pengu.lostthaumaturgy.items.ItemAuraDetector;
import com.pengu.lostthaumaturgy.items.ItemCrystallineBell;
import com.pengu.lostthaumaturgy.items.ItemCustomPotion;
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
import com.pengu.lostthaumaturgy.items.tools.axe.ItemAxeElemental;
import com.pengu.lostthaumaturgy.items.tools.axe.ItemAxeThaumium;
import com.pengu.lostthaumaturgy.items.tools.hoe.ItemHoeElemental;
import com.pengu.lostthaumaturgy.items.tools.hoe.ItemHoeThaumium;
import com.pengu.lostthaumaturgy.items.tools.pick.ItemPickaxeElemental;
import com.pengu.lostthaumaturgy.items.tools.pick.ItemPickaxeThaumium;
import com.pengu.lostthaumaturgy.items.tools.shovel.ItemShovelElemental;
import com.pengu.lostthaumaturgy.items.tools.shovel.ItemShovelThaumium;
import com.pengu.lostthaumaturgy.items.tools.swords.ItemSwordElemental;
import com.pengu.lostthaumaturgy.items.tools.swords.ItemSwordThaumium;

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
	public static final Item //
	        THAUMIUM_AXE = new ItemAxeThaumium(), //
	        THAUMIUM_HOE = new ItemHoeThaumium(), //
	        THAUMIUM_PICKAXE = new ItemPickaxeThaumium(), //
	        THAUMIUM_SHOVEL = new ItemShovelThaumium(), //
	        THAUMIUM_SWORD = new ItemSwordThaumium(), //
	        ELEMENTAL_AXE = new ItemAxeElemental(), //
	        ELEMENTAL_HOE = new ItemHoeElemental(), //
	        ELEMENTAL_PICKAXE = new ItemPickaxeElemental(), //
	        ELEMENTAL_SHOVEL = new ItemShovelElemental(), //
	        ELEMENTAL_SWORD = new ItemSwordElemental();
	public static final ItemUpgrade //
	        QUICKSILVER_CORE = new ItemUpgrade("quicksilver_core"), //
	        STABILIZED_SINGULARITY = new ItemUpgrade("stabilized_singularity"), //
	        HARNESSED_RAGE = new ItemUpgrade("harnessed_rage"), //
	        CONCENTRATED_EVIL = new ItemUpgrade("concentrated_evil"), //
	        INFINITE_SADNESS = new ItemUpgrade("infinite_saddness"), //
	        CONTAINED_EMPTINESS = new ItemUpgrade("contained_emptiness"), //
	        COLLECTED_WISDOM = new ItemUpgrade("collected_wisdom"), //
	        CONCENTRATED_PURITY = new ItemUpgrade("concentrated_purity");
	public static final ItemSealSymbol //
	        RUNICESSENCE_MAGIC = new DefaultSealSymbol("runic_essence_magic", 0, 0x8300FD), //
	        RUNICESSENCE_AIR = new DefaultSealSymbol("runic_essence_air", 1, 0xE2D978), //
	        RUNICESSENCE_WATER = new DefaultSealSymbol("runic_essence_water", 2, 0x335B9B), //
	        RUNICESSENCE_EARTH = new DefaultSealSymbol("runic_essence_earth", 3, 0x00C000), //
	        RUNICESSENCE_FIRE = new DefaultSealSymbol("runic_essence_fire", 4, 0xB7583E), //
	        RUNICESSENCE_DARK = new DefaultSealSymbol("runic_essence_dark", 5, 0x411B6D), //
	        RUNICESSENCE_DEPLETED = new DefaultSealSymbol("runic_essence_depleted", 6, 0xE6E6E6);
}