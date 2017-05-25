package com.pengu.lostthaumaturgy.init;

import java.lang.reflect.Field;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.HammerCore;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal.Getter;
import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.custom.research.ResearchRegistry;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.api.IThaumonomicon;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class ResearchesLT
{
	public static final Research //
	        SILVERWOOD_VIS_TANK = new Research(LTInfo.MOD_ID + ":silverwood_vis_tank", 25, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0xBBBBBB), //
	        REINFORCED_VIS_TANK = new Research(LTInfo.MOD_ID + ":reinforced_vis_tank", 30, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0x911CFF), //
	        CRYSTALLINE_BELL = new Research(LTInfo.MOD_ID + ":crystalline_bell", 75, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0xAFFFFF), //
	        PRESSURIZED_COUNDUIT = new Research(LTInfo.MOD_ID + ":pressurized_conduit", 50, Research.CATEGORY_THAUMATURGY), //
	        THAUMIUM_VIS_PUMP = new Research(LTInfo.MOD_ID + ":thaumium_vis_pump", 80, Research.CATEGORY_THAUMATURGY).setColor(0xC14CFF), //
	        THAUMIUM_BELLOWS = new Research(LTInfo.MOD_ID + ":thaumium_bellows", 50, Research.CATEGORY_THAUMATURGY), //
	        SINGULARITY = new Research(LTInfo.MOD_ID + ":singularity", 35, Research.CATEGORY_LOST_KNOWLEDGE), //
	        QUICKSILVER_CORE = new Research(LTInfo.MOD_ID + ":quicksilver_core", 35, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0xBEBDEA), //
	        WAND_REVERSAL = new Research(LTInfo.MOD_ID + ":wand_reversal", 45, Research.CATEGORY_LOST_KNOWLEDGE).setColor(0x7298B3);
	
	public static void registerResearches()
	{
		if(HammerCore.pipelineProxy.pipeIfOnGameSide(new Object(), Side.CLIENT) != null)
		{
			SILVERWOOD_VIS_TANK.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(BlocksLT.VIS_TANK_SILVERWOOD));
			REINFORCED_VIS_TANK.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(BlocksLT.VIS_TANK_REINFORCED));
			CRYSTALLINE_BELL.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(ItemsLT.CRYSTALLINE_BELL));
			PRESSURIZED_COUNDUIT.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(BlocksLT.PRESSURIZED_CONDUIT));
			THAUMIUM_VIS_PUMP.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(BlocksLT.THAUMIUM_VIS_PUMP));
			THAUMIUM_BELLOWS.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(BlocksLT.THAUMIUM_BELLOWS));
			SINGULARITY.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(ItemsLT.SINGULARITY));
			QUICKSILVER_CORE.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(ItemsLT.QUICKSILVER_CORE));
			WAND_REVERSAL.getPageHandler().thaumonomiconIcon = new Getter(new ItemStack(ItemsLT.WAND_REVERSAL));
		}
		
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
		t.setCategoryItemIcon(EnumMultiMaterialType.DEPLETED_CRYSTAL.stack(), Research.CATEGORY_UNDEFINED);
	}
}