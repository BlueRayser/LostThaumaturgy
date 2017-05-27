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
import com.pengu.lostthaumaturgy.block.BlockCrystallizer;
import com.pengu.lostthaumaturgy.block.BlockDepletedOreCrystal;
import com.pengu.lostthaumaturgy.block.BlockGenerator;
import com.pengu.lostthaumaturgy.block.BlockInfuser;
import com.pengu.lostthaumaturgy.block.BlockLyingItem;
import com.pengu.lostthaumaturgy.block.BlockNitor;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.block.BlockPenguCobbleGen;
import com.pengu.lostthaumaturgy.block.BlockPlant;
import com.pengu.lostthaumaturgy.block.BlockPressurizedConduit;
import com.pengu.lostthaumaturgy.block.BlockPurifier;
import com.pengu.lostthaumaturgy.block.BlockReinforcedVisTank;
import com.pengu.lostthaumaturgy.block.BlockStudiumTable;
import com.pengu.lostthaumaturgy.block.BlockTaintedPlant;
import com.pengu.lostthaumaturgy.block.BlockTaintedSoil;
import com.pengu.lostthaumaturgy.block.BlockThaumiumBellows;
import com.pengu.lostthaumaturgy.block.BlockVisFilter;
import com.pengu.lostthaumaturgy.block.BlockVisPump;
import com.pengu.lostthaumaturgy.block.BlockVisPumpThaumium;
import com.pengu.lostthaumaturgy.block.BlockVisTank;
import com.pengu.lostthaumaturgy.block.BlockVisValve;
import com.pengu.lostthaumaturgy.block.BlockVoidChest;
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
	        CONDUIT = new BlockConduit(), //
	        PRESSURIZED_CONDUIT = new BlockPressurizedConduit(), //
	        VIS_TANK = new BlockVisTank(), //
	        VIS_TANK_REINFORCED = new BlockReinforcedVisTank(), //
	        VIS_TANK_SILVERWOOD = new BlockSilverwoodVisTank(), //
	        VIS_PUMP = new BlockVisPump(), //
	        THAUMIUM_VIS_PUMP = new BlockVisPumpThaumium(), //
	        INFUSER = new BlockInfuser(), //
	        VIS_FILTER = new BlockVisFilter(), //
	        BELLOWS = new BlockBellows(), //
	        THAUMIUM_BELLOWS = new BlockThaumiumBellows(), //
	        VIS_PURIFIER = new BlockPurifier(), //
	        VIS_VALVE = new BlockVisValve(), //
	        ADVANCED_VIS_VALVE = new BlockAdvancedVisValve(), //
	        STUDIUM_TABLE = new BlockStudiumTable(), //
	        AUXILIUM_TABLE = new BlockAuxiliumTable(), //
	        CRYSTALLIZER = new BlockCrystallizer(), //
	        NITOR = new BlockNitor(), //
	        TAINTED_SOIL = new BlockTaintedSoil(), //
	        VOID_CHEST = new BlockVoidChest(), //
	        PENGU_COBBLEGEN = new BlockPenguCobbleGen(), //
	        GENERATOR = new BlockGenerator();
	
	public static final Block //
	        SILVERWOOD_LEAVES = new BlockSilverwoodLeaves(), //
	        SILVERWOOD_LOG = new BlockSilverwoodLog(), //
	        LYING_ITEM = new BlockLyingItem(), //
	        ELDRITCH_BLOCK = new Block(Material.ROCK).setHardness(1.5F).setUnlocalizedName("eldritch_block").setResistance(Float.POSITIVE_INFINITY);
	
	public static final Block //
	        SHIMMERLEAF = new BlockPlant("shimmerleaf", new AxisAlignedBB(.1, 0, .1, .9, .8, .9)), //
	        TAINTEDLEAF = new BlockPlant("taintedleaf", new AxisAlignedBB(.1, 0, .1, .9, .8, .9)), //
	        CINDERPEARL = new BlockCinderpearl(), //
	        TAINTED_PLANT = new BlockTaintedPlant();
	
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
		ELDRITCH_BLOCK.setHarvestLevel("pickaxe", 2);
	}
}