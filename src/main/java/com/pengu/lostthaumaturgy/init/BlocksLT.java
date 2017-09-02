package com.pengu.lostthaumaturgy.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

import com.pengu.lostthaumaturgy.core.block.BlockAdvancedVisValve;
import com.pengu.lostthaumaturgy.core.block.BlockAuxiliumTable;
import com.pengu.lostthaumaturgy.core.block.BlockBellows;
import com.pengu.lostthaumaturgy.core.block.BlockCinnabarOre;
import com.pengu.lostthaumaturgy.core.block.BlockConduit;
import com.pengu.lostthaumaturgy.core.block.BlockCrucible;
import com.pengu.lostthaumaturgy.core.block.BlockCrucibleEyes;
import com.pengu.lostthaumaturgy.core.block.BlockCrucibleThaumium;
import com.pengu.lostthaumaturgy.core.block.BlockCrucibleVoid;
import com.pengu.lostthaumaturgy.core.block.BlockCrystallizer;
import com.pengu.lostthaumaturgy.core.block.BlockDarknessGenerator;
import com.pengu.lostthaumaturgy.core.block.BlockDepletedOreCrystal;
import com.pengu.lostthaumaturgy.core.block.BlockDuplicator;
import com.pengu.lostthaumaturgy.core.block.BlockGenerator;
import com.pengu.lostthaumaturgy.core.block.BlockInfuser;
import com.pengu.lostthaumaturgy.core.block.BlockInfuserDark;
import com.pengu.lostthaumaturgy.core.block.BlockLyingItem;
import com.pengu.lostthaumaturgy.core.block.BlockMetal;
import com.pengu.lostthaumaturgy.core.block.BlockNitor;
import com.pengu.lostthaumaturgy.core.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.core.block.BlockPenguCobbleGen;
import com.pengu.lostthaumaturgy.core.block.BlockPlant;
import com.pengu.lostthaumaturgy.core.block.BlockPressurizedConduit;
import com.pengu.lostthaumaturgy.core.block.BlockPurifier;
import com.pengu.lostthaumaturgy.core.block.BlockReinforcedVisTank;
import com.pengu.lostthaumaturgy.core.block.BlockRepairer;
import com.pengu.lostthaumaturgy.core.block.BlockSeal;
import com.pengu.lostthaumaturgy.core.block.BlockSingularityJar;
import com.pengu.lostthaumaturgy.core.block.BlockStudiumTable;
import com.pengu.lostthaumaturgy.core.block.BlockTaintedLog;
import com.pengu.lostthaumaturgy.core.block.BlockTaintedPlant;
import com.pengu.lostthaumaturgy.core.block.BlockTaintedSoil;
import com.pengu.lostthaumaturgy.core.block.BlockThaumiumBellows;
import com.pengu.lostthaumaturgy.core.block.BlockTotem;
import com.pengu.lostthaumaturgy.core.block.BlockVisCondenser;
import com.pengu.lostthaumaturgy.core.block.BlockVisFilter;
import com.pengu.lostthaumaturgy.core.block.BlockVisPump;
import com.pengu.lostthaumaturgy.core.block.BlockVisPumpThaumium;
import com.pengu.lostthaumaturgy.core.block.BlockVisTank;
import com.pengu.lostthaumaturgy.core.block.BlockVisValve;
import com.pengu.lostthaumaturgy.core.block.BlockVoidChest;
import com.pengu.lostthaumaturgy.core.block.BlockWandConstructor;
import com.pengu.lostthaumaturgy.core.block.infuser.BlockFuser;
import com.pengu.lostthaumaturgy.core.block.infuser.BlockInfuserBase;
import com.pengu.lostthaumaturgy.core.block.monolith.BlockCrystalReceptacle;
import com.pengu.lostthaumaturgy.core.block.monolith.BlockExtraRoom;
import com.pengu.lostthaumaturgy.core.block.monolith.BlockMonolith;
import com.pengu.lostthaumaturgy.core.block.monolith.BlockMonolithOpener;
import com.pengu.lostthaumaturgy.core.block.wood.BlockTaintedLeaves;
import com.pengu.lostthaumaturgy.core.block.wood.greatwood.BlockGreatwoodLeaves;
import com.pengu.lostthaumaturgy.core.block.wood.greatwood.BlockGreatwoodLog;
import com.pengu.lostthaumaturgy.core.block.wood.greatwood.BlockGreatwoodSapling;
import com.pengu.lostthaumaturgy.core.block.wood.silverwood.BlockSilverwoodConduit;
import com.pengu.lostthaumaturgy.core.block.wood.silverwood.BlockSilverwoodLeaves;
import com.pengu.lostthaumaturgy.core.block.wood.silverwood.BlockSilverwoodLog;
import com.pengu.lostthaumaturgy.core.block.wood.silverwood.BlockSilverwoodPlanks;
import com.pengu.lostthaumaturgy.core.block.wood.silverwood.BlockSilverwoodStairs;
import com.pengu.lostthaumaturgy.core.block.wood.silverwood.BlockSilverwoodVisTank;
import com.pengu.lostthaumaturgy.core.block.world.BlockCinderpearl;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class BlocksLT
{
	public static final Block //
	        CRUCIBLE = new BlockCrucible(), //
	        CRUCIBLE_EYES = new BlockCrucibleEyes(), //
	        CRUCIBLE_THAUMIUM = new BlockCrucibleThaumium(), //
	        CRUCIBLE_VOID = new BlockCrucibleVoid(), //
	        CONDUIT = new BlockConduit(), //
	        CONDUIT_SILVERWOOD = new BlockSilverwoodConduit(), //
	        PRESSURIZED_CONDUIT = new BlockPressurizedConduit(), //
	        VIS_TANK = new BlockVisTank(), //
	        VIS_TANK_REINFORCED = new BlockReinforcedVisTank(), //
	        VIS_TANK_SILVERWOOD = new BlockSilverwoodVisTank(), //
	        VIS_PUMP = new BlockVisPump(), //
	        VIS_CONDENSER = new BlockVisCondenser(), //
	        REPAIRER = new BlockRepairer(), //
	        THAUMIUM_VIS_PUMP = new BlockVisPumpThaumium(), //
	        INFUSER = new BlockInfuser(), //
	        INFUSER_DARK = new BlockInfuserDark(), //
	        VIS_FILTER = new BlockVisFilter(), //
	        BELLOWS = new BlockBellows(), //
	        THAUMIUM_BELLOWS = new BlockThaumiumBellows(), //
	        VIS_PURIFIER = new BlockPurifier(), //
	        VIS_VALVE = new BlockVisValve(), //
	        ADVANCED_VIS_VALVE = new BlockAdvancedVisValve(), //
	        STUDIUM_TABLE = new BlockStudiumTable(), //
	        AUXILIUM_TABLE = new BlockAuxiliumTable(), //
	        SEAL = new BlockSeal(), //
	        WAND_CONSTRUCTOR = new BlockWandConstructor(), //
	        CRYSTALLIZER = new BlockCrystallizer(), //
	        DARKNESS_GENERATOR = new BlockDarknessGenerator(), //
	        NITOR = new BlockNitor(), //
	        TAINTED_SOIL = new BlockTaintedSoil(), //
	        VOID_CHEST = new BlockVoidChest(), //
	        PENGU_COBBLEGEN = new BlockPenguCobbleGen(), //
	        GENERATOR = new BlockGenerator(), //
	        SINGULARITY_JAR = new BlockSingularityJar(), //
	        DUPLICATOR = new BlockDuplicator();
	
	public static final Block //
	        SILVERWOOD_LEAVES = new BlockSilverwoodLeaves(), //
	        GREATWOOD_LEAVES = new BlockGreatwoodLeaves(), //
	        TAINTED_LEAVES = new BlockTaintedLeaves(), //
	        SILVERWOOD_LOG = new BlockSilverwoodLog(), //
	        GREATWOOD_LOG = new BlockGreatwoodLog(), //
	        TAINTED_LOG = new BlockTaintedLog(), //
	        GREATWOOD_SAPLING = new BlockGreatwoodSapling(), //
	        LYING_ITEM = new BlockLyingItem(), //
	        TOTEM_DAWN = new BlockTotem(true), //
	        TOTEM_TWILIGHT = new BlockTotem(false), //
	        ELDRITCH_BLOCK = new Block(Material.ROCK).setHardness(1.5F).setUnlocalizedName("eldritch_block").setResistance(Float.POSITIVE_INFINITY), //
	        MONOLITH = new BlockMonolith(), MONOLITH_CRYSTAL_RECEPTACLE = new BlockCrystalReceptacle(), MONOLITH_OPENER = new BlockMonolithOpener(), //
	        MONOLITH_EXTRA_ROOM = new BlockExtraRoom(), //
	        THAUMIUM_BLOCK = new BlockMetal().setUnlocalizedName("thaumium_block"), //
	        VOID_BLOCK = new BlockMetal().setUnlocalizedName("void_block");
	
	public static final Block //
	        SHIMMERLEAF = new BlockPlant("shimmerleaf", new AxisAlignedBB(.1, 0, .1, .9, .8, .9)), //
	        TAINTEDLEAF = new BlockPlant("taintedleaf", new AxisAlignedBB(.1, 0, .1, .9, .8, .9)), //
	        CINDERPEARL = new BlockCinderpearl(), //
	        TAINTED_PLANT = new BlockTaintedPlant(), //
	        INFUSER_BASE = new BlockInfuserBase(), //
	        FUSER_MB = new BlockFuser();
	
	public static final Block //
	        CRYSTAL_ORE_VAPOROUS = new BlockOreCrystal(EnumMultiMaterialType.VAPOROUS_CRYSTAL, "vaporous", true, 0xFFD905), //
	        CRYSTAL_ORE_AQUEOUS = new BlockOreCrystal(EnumMultiMaterialType.AQUEOUS_CRYSTAL, "aqueous", true, 0x0043FF), //
	        CRYSTAL_ORE_EARTHEN = new BlockOreCrystal(EnumMultiMaterialType.EARTHEN_CRYSTAL, "earthen", true, 0x00FF00), //
	        CRYSTAL_ORE_FIERY = new BlockOreCrystal(EnumMultiMaterialType.FIERY_CRYSTAL, "fiery", true, 0xFF0000), //
	        CRYSTAL_ORE_VIS = new BlockOreCrystal(EnumMultiMaterialType.VIS_CRYSTAL, "vis", true, 0xAA00FF), //
	        CRYSTAL_ORE_TAINTED = new BlockOreCrystal(EnumMultiMaterialType.TAINTED_CRYSTAL, "tainted", false, 0x2B134C), //
	        CRYSTAL_ORE_DEPLETED = new BlockDepletedOreCrystal(), //
	        CINNABAR_ORE = new BlockCinnabarOre();
	
	public static final Block //
	        SILVERWOOD_PLANKS = new BlockSilverwoodPlanks(), //
	        SILVERWOOD_STAIRS = new BlockSilverwoodStairs();
	
	static
	{
		ELDRITCH_BLOCK.setLightLevel(1F).setHarvestLevel("pickaxe", 2);
	}
}