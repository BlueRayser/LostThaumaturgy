package com.pengu.lostthaumaturgy.init;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.oredict.OreDictionary;

import com.pengu.lostthaumaturgy.api.RecipesInfuser;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class InfuserLT
{
	public static void registerInfuser()
	{
		EnumMultiMaterialType[] crystals = { EnumMultiMaterialType.VAPOROUS_CRYSTAL, EnumMultiMaterialType.AQUEOUS_CRYSTAL, EnumMultiMaterialType.FIERY_CRYSTAL, EnumMultiMaterialType.EARTHEN_CRYSTAL, EnumMultiMaterialType.VIS_CRYSTAL, EnumMultiMaterialType.TAINTED_CRYSTAL };
		
		Set<ItemStack> metalToThaumium = new HashSet<>();
		
		for(String od : OreDictionary.getOreNames())
		{
			if(od.startsWith("ingot"))
			{
				String metal = od.substring(5).toLowerCase();
				
				boolean match = false;
				
				if(metal.equals("iron"))
					match = true;
				
				if(metal.equals("bronze"))
					match = true;
				
				if(metal.equals("brass"))
					match = true;
				
				if(metal.equals("silver"))
					match = true;
				
				if(match)
					metalToThaumium.addAll(OreDictionary.getOres(od));
			}
		}
		
		for(EnumMultiMaterialType crystal : crystals)
		{
			for(ItemStack stack : metalToThaumium)
				RecipesInfuser.addInfusing(EnumMultiMaterialType.THAUMIUM_INGOT.stack(), 5, crystal.stack(), stack);
			
			RecipesInfuser.addInfusing(EnumMultiMaterialType.ENCHANTED_FABRIC.stack(), 25, crystal.stack(), new ItemStack(Blocks.WOOL, 1, -1), new ItemStack(Items.STRING));
			RecipesInfuser.addInfusing(EnumMultiMaterialType.ENCHANTED_SILVERWOOD.stack(), 15, crystal.stack(), new ItemStack(BlocksLT.SILVERWOOD_LOG));
			
			for(ItemStack stack : OreDictionary.getOres("logWood"))
				RecipesInfuser.addInfusing(EnumMultiMaterialType.ENCHANTED_WOOD.stack(4), 15, crystal.stack(), stack);
		}
		
		RecipesInfuser.addInfusing(EnumMultiMaterialType.ANIMATED_PISTON.stack(), 50, EnumMultiMaterialType.VAPOROUS_CRYSTAL.stack(), new ItemStack(Items.GOLD_INGOT), new ItemStack(Blocks.PISTON));
		RecipesInfuser.addInfusing(new ItemStack(BlocksLT.VIS_TANK_SILVERWOOD), 50, RecipesInfuser.createPredicateFromResearches(ResearchesLT.SILVERWOOD_VIS_TANK), new ItemStack(BlocksLT.VIS_TANK), EnumMultiMaterialType.ENCHANTED_SILVERWOOD.stack(), EnumMultiMaterialType.ENCHANTED_SILVERWOOD.stack(), EnumMultiMaterialType.ENCHANTED_SILVERWOOD.stack());
		RecipesInfuser.addInfusing(new ItemStack(BlocksLT.VIS_TANK_REINFORCED), 75, RecipesInfuser.createPredicateFromResearches(ResearchesLT.REINFORCED_VIS_TANK), new ItemStack(BlocksLT.VIS_TANK_SILVERWOOD), EnumMultiMaterialType.THAUMIUM_INGOT.stack());
		RecipesInfuser.addInfusing(EnumMultiMaterialType.ALUMENTUM.stack(), 9, new ItemStack(Items.REDSTONE), new ItemStack(Items.COAL));
		RecipesInfuser.addInfusing(new ItemStack(BlocksLT.NITOR), 10, new ItemStack(Items.REDSTONE), new ItemStack(Items.GLOWSTONE_DUST));
		RecipesInfuser.addInfusing(new ItemStack(ItemsLT.SINGULARITY), 10, RecipesInfuser.createPredicateFromResearches(ResearchesLT.SINGULARITY), new ItemStack(BlocksLT.NITOR), EnumMultiMaterialType.ALUMENTUM.stack());
		RecipesInfuser.addInfusing(new ItemStack(ItemsLT.CRYSTALLINE_BELL), 150, RecipesInfuser.createPredicateFromResearches(ResearchesLT.CRYSTALLINE_BELL), new ItemStack(Items.DIAMOND), new ItemStack(Blocks.GLASS));
		RecipesInfuser.addInfusing(new ItemStack(BlocksLT.PRESSURIZED_CONDUIT), 20, RecipesInfuser.createPredicateFromResearches(ResearchesLT.PRESSURIZED_COUNDUIT), new ItemStack(Items.DIAMOND), new ItemStack(BlocksLT.CONDUIT), EnumMultiMaterialType.VAPOROUS_CRYSTAL.stack());
		RecipesInfuser.addInfusing(new ItemStack(BlocksLT.THAUMIUM_VIS_PUMP), 75, RecipesInfuser.createPredicateFromResearches(ResearchesLT.THAUMIUM_VIS_PUMP, ResearchesLT.THAUMIUM_BELLOWS), EnumMultiMaterialType.AQUEOUS_CRYSTAL.stack(), new ItemStack(BlocksLT.VIS_PUMP), new ItemStack(BlocksLT.THAUMIUM_BELLOWS), EnumMultiMaterialType.THAUMIUM_INGOT.stack(), EnumMultiMaterialType.THAUMIUM_INGOT.stack(), EnumMultiMaterialType.THAUMIUM_INGOT.stack());
		RecipesInfuser.addInfusing(new ItemStack(BlocksLT.THAUMIUM_BELLOWS), 50, RecipesInfuser.createPredicateFromResearches(ResearchesLT.THAUMIUM_BELLOWS), EnumMultiMaterialType.VAPOROUS_CRYSTAL.stack(), new ItemStack(BlocksLT.BELLOWS), EnumMultiMaterialType.THAUMIUM_INGOT.stack(), EnumMultiMaterialType.THAUMIUM_INGOT.stack(), EnumMultiMaterialType.THAUMIUM_INGOT.stack(), EnumMultiMaterialType.THAUMIUM_INGOT.stack());
		RecipesInfuser.addInfusing(new ItemStack(ItemsLT.QUICKSILVER_CORE), 50, RecipesInfuser.createPredicateFromResearches(ResearchesLT.QUICKSILVER_CORE), new ItemStack(Blocks.STONE_SLAB), EnumMultiMaterialType.VAPOROUS_CRYSTAL.stack(), EnumMultiMaterialType.QUICKSILVER.stack());
		RecipesInfuser.addInfusing(new ItemStack(ItemsLT.STABILIZED_SINGULARITY), 50, new ItemStack(Blocks.STONE_SLAB), EnumMultiMaterialType.EARTHEN_CRYSTAL.stack(), new ItemStack(ItemsLT.SINGULARITY));
		RecipesInfuser.addInfusing(new ItemStack(ItemsLT.WAND_REVERSAL), 50, RecipesInfuser.createPredicateFromResearches(ResearchesLT.WAND_REVERSAL), new ItemStack(ItemsLT.SINGULARITY), EnumMultiMaterialType.ENCHANTED_SILVERWOOD.stack(), new ItemStack(Items.GOLD_INGOT), EnumMultiMaterialType.THAUMIUM_INGOT.stack(), new ItemStack(BlocksLT.CRYSTAL_ORE_VIS));
		RecipesInfuser.addInfusing(new ItemStack(ItemsLT.CUSTOM_POTION, 1, 2), 25, new ItemStack(Items.GLASS_BOTTLE), new ItemStack(BlocksLT.SILVERWOOD_LEAVES));
		RecipesInfuser.addInfusing(new ItemStack(ItemsLT.CUSTOM_POTION), 140, new ItemStack(Items.GLASS_BOTTLE), EnumMultiMaterialType.VIS_CRYSTAL.stack());
		RecipesInfuser.addInfusing(new ItemStack(ItemsLT.COLLECTED_WISDOM), 100, new ItemStack(Blocks.STONE_SLAB), EnumMultiMaterialType.VIS_CRYSTAL.stack(), EnumMultiMaterialType.ZOMBIE_BRAINS.stack());
		RecipesInfuser.addInfusing(EnumMultiMaterialType.EXTRACT_PUREST_MAGIC.stack(), 250, EnumMultiMaterialType.VIS_CRYSTAL.stack(), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new ItemStack(BlocksLT.SHIMMERLEAF));
		RecipesInfuser.addInfusing(EnumMultiMaterialType.EXTRACT_FOULEST_TAINT.stack(), 250, EnumMultiMaterialType.TAINTED_CRYSTAL.stack(), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new ItemStack(BlocksLT.SHIMMERLEAF));
		RecipesInfuser.addInfusing(EnumMultiMaterialType.EXTRACT_WARMEST_FIRE.stack(), 250, EnumMultiMaterialType.FIERY_CRYSTAL.stack(), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new ItemStack(BlocksLT.SHIMMERLEAF));
		RecipesInfuser.addInfusing(EnumMultiMaterialType.EXTRACT_DEEPEST_EARTH.stack(), 250, EnumMultiMaterialType.EARTHEN_CRYSTAL.stack(), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new ItemStack(BlocksLT.SHIMMERLEAF));
		RecipesInfuser.addInfusing(EnumMultiMaterialType.EXTRACT_LIGHTEST_AIR.stack(), 250, EnumMultiMaterialType.VAPOROUS_CRYSTAL.stack(), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new ItemStack(BlocksLT.SHIMMERLEAF));
		RecipesInfuser.addInfusing(EnumMultiMaterialType.EXTRACT_COOLEST_WATER.stack(), 250, EnumMultiMaterialType.AQUEOUS_CRYSTAL.stack(), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new ItemStack(BlocksLT.SHIMMERLEAF));
	}
	
	public static void registerDarkInfuser()
	{
		RecipesInfuser.addDarkInfusing(EnumMultiMaterialType.SOUL_FRAGMENT.stack(), 40, new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND));
	}
}