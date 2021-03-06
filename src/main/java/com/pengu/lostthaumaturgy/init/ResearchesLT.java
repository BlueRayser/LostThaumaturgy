package com.pengu.lostthaumaturgy.init;

import java.lang.reflect.Field;

import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.custom.research.ResearchRegistry;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.api.IThaumonomicon;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class ResearchesLT
{
	public static final Research //
	        SILVERWOOD_VIS_TANK = new Research(LTInfo.MOD_ID + ":silverwood_vis_tank", 25, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0xBBBBBB).setIcon(new ItemStack(BlocksLT.VIS_TANK_SILVERWOOD)), //
	        REINFORCED_VIS_TANK = new Research(LTInfo.MOD_ID + ":reinforced_vis_tank", 30, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0x911CFF).setIcon(new ItemStack(BlocksLT.VIS_TANK_REINFORCED)), //
	        CRYSTALLINE_BELL = new Research(LTInfo.MOD_ID + ":crystalline_bell", 75, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0xAFFFFF).setIcon(new ItemStack(ItemsLT.CRYSTALLINE_BELL)), //
	        PRESSURIZED_COUNDUIT = new Research(LTInfo.MOD_ID + ":pressurized_conduit", 50, Research.CATEGORY_THAUMATURGY).setIcon(new ItemStack(BlocksLT.PRESSURIZED_CONDUIT)), //
	        THAUMIUM_VIS_PUMP = new Research(LTInfo.MOD_ID + ":thaumium_vis_pump", 80, Research.CATEGORY_THAUMATURGY).setColor(0xC14CFF).setIcon(new ItemStack(BlocksLT.THAUMIUM_VIS_PUMP)), //
	        THAUMIUM_BELLOWS = new Research(LTInfo.MOD_ID + ":thaumium_bellows", 50, Research.CATEGORY_THAUMATURGY).setIcon(new ItemStack(BlocksLT.THAUMIUM_BELLOWS)), //
	        SINGULARITY = new Research(LTInfo.MOD_ID + ":singularity", 35, Research.CATEGORY_LOST_KNOWLEDGE).setIcon(new ItemStack(ItemsLT.SINGULARITY)), //
	        QUICKSILVER_CORE = new Research(LTInfo.MOD_ID + ":quicksilver_core", 35, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0xBEBDEA).setIcon(new ItemStack(ItemsLT.QUICKSILVER_CORE)), //
	        WAND_REVERSAL = new Research(LTInfo.MOD_ID + ":wand_reversal", 45, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0x7298B3).setIcon(new ItemStack(ItemsLT.WAND_REVERSAL)),
	        CONCENTRATED_TAINT = new Research(LTInfo.MOD_ID + ":concentrated_taint", 35, Research.CATEGORY_TAINTED).setIcon(new ItemStack(ItemsLT.CUSTOM_POTION, 1, 1)),
	        POTION_PURITY = new Research(LTInfo.MOD_ID + ":potion_purity", 55, Research.CATEGORY_TAINTED).setIcon(new ItemStack(ItemsLT.CUSTOM_POTION, 1, 2)),
	        CONCENTRATED_EVIL = new Research(LTInfo.MOD_ID + ":concentrated_evil", 60, Research.CATEGORY_TAINTED).setIcon(new ItemStack(ItemsLT.CONCENTRATED_EVIL));
	
	public static void registerResearches()
	{
		for(Field f : ResearchesLT.class.getDeclaredFields())
			if(Research.class.isAssignableFrom(f.getType()))
				try
				{
					Research r = (Research) f.get(null);
					ResearchRegistry.registerResearch(r);
				} catch(Throwable er)
				{
				}
		
		IThaumonomicon t = IThaumonomicon.instance;
		t.setCategoryItemIcon(EnumMultiMaterialType.ANCIENT_SEAL.stack(), Research.CATEGORY_LOST_KNOWLEDGE);
		t.setCategoryItemIcon(EnumMultiMaterialType.AQUEOUS_CRYSTAL.stack(), Research.CATEGORY_BASICS);
		t.setCategoryItemIcon(EnumMultiMaterialType.ZOMBIE_BRAINS.stack(), Research.CATEGORY_ELDRITCH);
		t.setCategoryItemIcon(EnumMultiMaterialType.THAUMIUM_INGOT.stack(), Research.CATEGORY_THAUMATURGY);
		t.setCategoryItemIcon(new ItemStack(BlocksLT.INFUSER_DARK), Research.CATEGORY_FORBIDDEN);
		t.setCategoryItemIcon(EnumMultiMaterialType.CONGEALED_TAINT.stack(), Research.CATEGORY_TAINTED);
		t.setCategoryItemIcon(EnumMultiMaterialType.DEPLETED_CRYSTAL.stack(), Research.CATEGORY_UNDEFINED);
	}
}