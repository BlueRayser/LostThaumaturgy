package com.pengu.lostthaumaturgy.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

import com.pengu.lostthaumaturgy.block.BlockAdvancedVisValve;
import com.pengu.lostthaumaturgy.block.BlockAuxiliumTable;
import com.pengu.lostthaumaturgy.block.BlockBellows;
import com.pengu.lostthaumaturgy.block.BlockCinnabarOre;
import com.pengu.lostthaumaturgy.block.BlockConduit;
import com.pengu.lostthaumaturgy.block.BlockCrucible;
import com.pengu.lostthaumaturgy.block.BlockCrucibleEyes;
import com.pengu.lostthaumaturgy.block.BlockCrucibleThaumium;
import com.pengu.lostthaumaturgy.block.BlockCrucibleVoid;
import com.pengu.lostthaumaturgy.block.BlockCrystallizer;
import com.pengu.lostthaumaturgy.block.BlockDepletedOreCrystal;
import com.pengu.lostthaumaturgy.block.BlockDuplicator;
import com.pengu.lostthaumaturgy.block.BlockGenerator;
import com.pengu.lostthaumaturgy.block.BlockInfuser;
import com.pengu.lostthaumaturgy.block.BlockInfuserDark;
import com.pengu.lostthaumaturgy.block.BlockLyingItem;
import com.pengu.lostthaumaturgy.block.BlockMetal;
import com.pengu.lostthaumaturgy.block.BlockNitor;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.block.BlockPenguCobbleGen;
import com.pengu.lostthaumaturgy.block.BlockPlant;
import com.pengu.lostthaumaturgy.block.BlockPressurizedConduit;
import com.pengu.lostthaumaturgy.block.BlockPurifier;
import com.pengu.lostthaumaturgy.block.BlockReinforcedVisTank;
import com.pengu.lostthaumaturgy.block.BlockSeal;
import com.pengu.lostthaumaturgy.block.BlockSingularityJar;
import com.pengu.lostthaumaturgy.block.BlockStudiumTable;
import com.pengu.lostthaumaturgy.block.BlockTaintedLog;
import com.pengu.lostthaumaturgy.block.BlockTaintedPlant;
import com.pengu.lostthaumaturgy.block.BlockTaintedSoil;
import com.pengu.lostthaumaturgy.block.BlockThaumiumBellows;
import com.pengu.lostthaumaturgy.block.BlockTotem;
import com.pengu.lostthaumaturgy.block.BlockVisCondenser;
import com.pengu.lostthaumaturgy.block.BlockVisFilter;
import com.pengu.lostthaumaturgy.block.BlockVisPump;
import com.pengu.lostthaumaturgy.block.BlockVisPumpThaumium;
import com.pengu.lostthaumaturgy.block.BlockVisTank;
import com.pengu.lostthaumaturgy.block.BlockVisValve;
import com.pengu.lostthaumaturgy.block.BlockVoidChest;
import com.pengu.lostthaumaturgy.block.BlockWandConstructor;
import com.pengu.lostthaumaturgy.block.infuser.BlockFuser;
import com.pengu.lostthaumaturgy.block.infuser.BlockInfuserBase;
import com.pengu.lostthaumaturgy.block.monolith.BlockCrystalReceptacle;
import com.pengu.lostthaumaturgy.block.monolith.BlockExtraRoom;
import com.pengu.lostthaumaturgy.block.monolith.BlockMonolith;
import com.pengu.lostthaumaturgy.block.monolith.BlockMonolithOpener;
import com.pengu.lostthaumaturgy.block.silverwood.BlockSilverwoodConduit;
import com.pengu.lostthaumaturgy.block.silverwood.BlockSilverwoodLeaves;
import com.pengu.lostthaumaturgy.block.silverwood.BlockSilverwoodLog;
import com.pengu.lostthaumaturgy.block.silverwood.BlockSilverwoodPlanks;
import com.pengu.lostthaumaturgy.block.silverwood.BlockSilverwoodStairs;
import com.pengu.lostthaumaturgy.block.silverwood.BlockSilverwoodVisTank;
import com.pengu.lostthaumaturgy.block.world.BlockCinderpearl;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

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
	        NITOR = new BlockNitor(), //
	        TAINTED_SOIL = new BlockTaintedSoil(), //
	        VOID_CHEST = new BlockVoidChest(), //
	        PENGU_COBBLEGEN = new BlockPenguCobbleGen(), //
	        GENERATOR = new BlockGenerator(), //
	        SINGULARITY_JAR = new BlockSingularityJar(), //
	        DUPLICATOR = new BlockDuplicator();
	
	public static final Block //
	        SILVERWOOD_LEAVES = new BlockSilverwoodLeaves(), //
	        SILVERWOOD_LOG = new BlockSilverwoodLog(), //
	        TAINTED_LOG = new BlockTaintedLog(), //
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
	        CRYSTAL_ORE_AQUEOUS = new BlockOreCrystal(EnumMultiMaterialType.AQUEOUS_CRYSTAL, "aqueus", true, 0x0043FF), //
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