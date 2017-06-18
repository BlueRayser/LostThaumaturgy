package com.pengu.lostthaumaturgy.api.fuser;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.init.ResearchesLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class RecipesFuser
{
	private static final RecipesFuser INSTANCE = new RecipesFuser();
	
	private final List<IFuserRecipe> recipes = Lists.<IFuserRecipe> newArrayList();
	
	/**
	 * Constructs a new instance with all vanilla recipes pre-registered.
	 */
	private RecipesFuser()
	{
		LostThaumaturgy.LOG.info("Registering Arcane Crafter Recipes...");
		
		addRecipe(new ShapedFuserRecipe(BlocksLT.VIS_PUMP, "waw", "wcw", "wbw", 'w', EnumMultiMaterialType.ENCHANTED_WOOD, 'a', EnumMultiMaterialType.AQUEOUS_CRYSTAL, 'b', BlocksLT.BELLOWS, 'c', BlocksLT.CONDUIT).setVisUsage(2F, 0F));
		addRecipe(new ShapedFuserRecipe(BlocksLT.VIS_FILTER, "wiw", "cac", "wiw", 'w', EnumMultiMaterialType.ENCHANTED_WOOD, 'i', "ingotIron", 'c', BlocksLT.CONDUIT, 'a', EnumMultiMaterialType.ALUMENTUM).setVisUsage(1.5F, 1.5F));
		addRecipe(new ShapedFuserRecipe(ItemsLT.ELEMENTAL_PICKAXE, "fff", " t ", " w ", 'f', EnumMultiMaterialType.FIERY_CRYSTAL, 't', ItemsLT.THAUMIUM_PICKAXE, 'w', EnumMultiMaterialType.ENCHANTED_WOOD).setVisUsage(5, 0).setResearch(ResearchesLT.ELEMENTAL_PICKAXE));
		addRecipe(new ShapedFuserRecipe(BlocksLT.VIS_PURIFIER, "geg", "ses", "geg", 'g', "ingotGold", 'e', EnumMultiMaterialType.ENCHANTED_SILVERWOOD, 's', BlocksLT.SILVERWOOD_LOG).setResearch(ResearchesLT.VIS_PURIFIER).setVisUsage(1.25F, 3.5F));
		addRecipe(new ShapelessFuserRecipe(Items.BLAZE_POWDER, EnumMultiMaterialType.CINDERPEARL_POD));
		addRecipe(new ShapedFuserRecipe(BlocksLT.CRYSTALLIZER, "123", "456", "787", '1', EnumMultiMaterialType.VAPOROUS_CRYSTAL, '2', EnumMultiMaterialType.VIS_CRYSTAL, '3', EnumMultiMaterialType.AQUEOUS_CRYSTAL, '4', EnumMultiMaterialType.EARTHEN_CRYSTAL, '5', "gemDiamond", '6', EnumMultiMaterialType.FIERY_CRYSTAL, '7', "ingotGold", '8', "ingotIron").setVisUsage(15F, 0).setResearch(ResearchesLT.CRYSTALLIZER));
		addRecipe(new ShapedFuserRecipe(EnumMultiMaterialType.TRAVELING_TRUNK.stack(), "www", "wsw", "www", 'w', EnumMultiMaterialType.ENCHANTED_WOOD, 's', EnumMultiMaterialType.SOUL_FRAGMENT).setVisUsage(25F, 0));
		addRecipe(new ShapedFuserRecipe(BlocksLT.GENERATOR, "gwg", "wsw", "gwg", 'g', "paneGlass", 'w', EnumMultiMaterialType.ENCHANTED_WOOD.stack(), 's', ItemsLT.STABILIZED_SINGULARITY).setVisUsage(1.5F, 0));
		addRecipe(new ShapedFuserRecipe(BlocksLT.ADVANCED_VIS_VALVE, " v ", "gcg", " t ", 'v', EnumMultiMaterialType.VIS_CRYSTAL, 't', EnumMultiMaterialType.TAINTED_CRYSTAL, 'g', "ingotGold", 'c', BlocksLT.VIS_VALVE).setResearch(ResearchesLT.ADVANCED_VIS_VALVE).setVisUsage(4.5F, 0));
		
		LostThaumaturgy.LOG.info("Registered " + recipes.size() + " Default Recipes.");
	}
	
	/**
	 * Adds a default shaped recipe for {@link ItemStack}. It uses default
	 * minecraft's recipe patterns and supports {@link OreDictionary} (string
	 * components)
	 */
	public void addShapedRecipe(ItemStack item, Object... recipe)
	{
		addRecipe(new ShapedFuserRecipe(item, recipe));
	}
	
	/**
	 * Adds a default shaped recipe for {@link Item}. It uses default
	 * minecraft's recipe patterns and supports {@link OreDictionary} (string
	 * components)
	 */
	public void addShapedRecipe(Item item, Object... recipe)
	{
		addRecipe(new ShapedFuserRecipe(item, recipe));
	}
	
	/**
	 * Adds a default shaped recipe for {@link Block}. It uses default
	 * minecraft's recipe patterns and supports {@link OreDictionary} (string
	 * components)
	 */
	public void addShapedRecipe(Block block, Object... recipe)
	{
		addRecipe(new ShapedFuserRecipe(block, recipe));
	}
	
	/**
	 * Adds a default shapeless recipe for {@link ItemStack}. It uses default
	 * minecraft's recipe patterns and supports {@link OreDictionary} (string
	 * components)
	 */
	public void addShapelessRecipe(ItemStack item, Object... recipe)
	{
		addRecipe(new ShapelessFuserRecipe(item, recipe));
	}
	
	/**
	 * Adds a default shapeless recipe for {@link Item}. It uses default
	 * minecraft's recipe patterns and supports {@link OreDictionary} (string
	 * components)
	 */
	public void addShapelessRecipe(Item item, Object... recipe)
	{
		addRecipe(new ShapelessFuserRecipe(item, recipe));
	}
	
	/**
	 * Adds a default shapeless recipe for {@link Block}. It uses default
	 * minecraft's recipe patterns and supports {@link OreDictionary} (string
	 * components)
	 */
	public void addShapelessRecipe(Block block, Object... recipe)
	{
		addRecipe(new ShapelessFuserRecipe(block, recipe));
	}
	
	/**
	 * Adds a custom recipe that can handle other factors like moon phase, etc.
	 */
	public void addRecipe(IFuserRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	/**
	 * Retrieves a list with all recipes registered. You can remove any recipe
	 * if you feel like tweaking the gameplay.
	 */
	public List<IFuserRecipe> getRecipes()
	{
		return recipes;
	}
	
	/**
	 * Gets the current instance of recipe registry for Arcane Crafter.
	 */
	public static RecipesFuser getInstance()
	{
		return INSTANCE;
	}
}