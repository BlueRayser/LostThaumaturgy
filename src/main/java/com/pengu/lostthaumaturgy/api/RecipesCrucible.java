package com.pengu.lostthaumaturgy.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.event.ReloadRegisteredCrucibleRecipesEvent;
import com.pengu.lostthaumaturgy.api.match.IMatcher;
import com.pengu.lostthaumaturgy.api.match.MatcherItemStack;
import com.pengu.lostthaumaturgy.api.match.MatcherOreDict;

public class RecipesCrucible
{
	private static final Map<IMatcher<ItemStack>, Float> registered = new HashMap<>();
	private static final Map<IMatcher<ItemStack>, Float> mapped = new HashMap<>();
	
	public static void registerNewSmelting(ItemStack container, float value)
	{
		if(container == null || container.isEmpty())
		{
			LostThaumaturgy.LOG.bigWarn("Attempted to register empty item stack!");
			return;
		}
		registerNewSmelting(new MatcherItemStack(container), value);
	}
	
	public static void registerNewSmelting(String od, float value)
	{
		registerNewSmelting(new MatcherOreDict(od), value);
	}
	
	public static void registerNewSmelting(IMatcher<ItemStack> container, float value)
	{
		if(getSmeltingValue(container) == 0F)
			registered.put(container, value);
		else
		{
			ItemStack item = container.defaultInstance();
			
			LostThaumaturgy.LOG.debug("Tried to register duplicate recipe: ItemStack@" + item.getCount() + "x" + item.getItem().getUnlocalizedName() + "@" + item.getItemDamage());
		}
	}
	
	/** Calls on world load to assign all values for items */
	public static void remapRecipes()
	{
		LostThaumaturgy.LOG.info("Remapping Crucible Recipes...");
		long start = System.currentTimeMillis();
		
		mapped.clear();
		mapped.putAll(registered);
		
		List<ItemStack> allStacks = new ArrayList<>();
		for(ResourceLocation r : Item.REGISTRY.getKeys())
		{
			Item i = Item.REGISTRY.getObject(r);
			NonNullList<ItemStack> subItems = NonNullList.create();
			i.getSubItems(i.getCreativeTab(), subItems);
			allStacks.addAll(subItems);
		}
		
		// for(ItemStack stack : allStacks)
		// {
		// if(getSmeltingValue(stack) > 0F)
		// continue;
		//
		// float value = getOrGenerateValue(stack, new HashSet<String>());
		// if(value > 0F)
		// {
		// if(getSmeltingValue(stack) > 0)
		// {
		// ItemContainer container = ItemContainer.create(stack);
		//
		// boolean disableOD = false;
		//
		// Item it = WorldUtil.cast(container.getItem(), Item.class);
		// if(it != null)
		// {
		// ItemStack stack2 = new ItemStack(it, 1, container.getDamage());
		// int[] ods = OreDictionary.getOreIDs(stack2);
		// disableOD = ods == null || ods.length == 0;
		// }
		//
		// boolean potential = false;
		//
		// for(Entry<ItemContainer, ItemMatchParams> e : mapped.keySet())
		// {
		// ItemMatchParams p = e.getValue();
		// boolean od = p.useOredict;
		// if(disableOD)
		// p.useOredict = false;
		// boolean match = e.getKey().matches(container, p);
		// p.useOredict = od;
		//
		// if(match)
		// mapped.put(e, Math.max(mapped.get(e), value));
		// if(match)
		// potential = true;
		// }
		//
		// if(!potential)
		// mapped.put(new ArrayEntry(container, defOD), value);
		// }
		// }
		// }
		
		mapped.putAll(registered);
		
		LostThaumaturgy.LOG.info("Recipes Remapped in " + (System.currentTimeMillis() - start) + " ms. Registered " + mapped.size() + " recipes.");
	}
	
	private static float getOrGenerateValue(ItemStack stack, Set<String> contained)
	{
		float value = getSmeltingValue(stack);
		if(value > 0 || !contained.add(bake(stack)) || stack.isEmpty())
			return value;
		if(value < 0)
			value = 0;
		
		// for(IRecipe r : CraftingManager.getInstance().getRecipeList())
		// if(!r.getRecipeOutput().isEmpty() &&
		// ItemContainer.create(stack).matches(r.getRecipeOutput(), new
		// ItemMatchParams().setUseNBT(true).setUseDamage(true)))
		// {
		// List<ItemStack> inputs = new ArrayList<>();
		//
		// if(r instanceof ShapedRecipes)
		// inputs.addAll(Arrays.asList(((ShapedRecipes) r).recipeItems));
		// if(r instanceof ShapelessRecipes)
		// inputs.addAll(((ShapelessRecipes) r).recipeItems);
		//
		// if(r instanceof ShapedOreRecipe)
		// {
		// Object[] data = ((ShapedOreRecipe) r).getInput();
		// for(Object o : data)
		// {
		// if(o instanceof ItemStack)
		// inputs.add((ItemStack) o);
		// if(o instanceof NonNullList && ((NonNullList<ItemStack>) o).size() >
		// 0)
		// inputs.add(((NonNullList<ItemStack>) o).get(0));
		// }
		// }
		//
		// if(r instanceof ShapelessOreRecipe)
		// {
		// NonNullList<Object> data = ((ShapelessOreRecipe) r).getInput();
		// for(Object o : data)
		// {
		// if(o instanceof ItemStack)
		// inputs.add((ItemStack) o);
		// if(o instanceof NonNullList && ((NonNullList<ItemStack>) o).size() >
		// 0)
		// inputs.add(((NonNullList<ItemStack>) o).get(0));
		// }
		// }
		//
		// float val = 0;
		//
		// for(ItemStack st : inputs)
		// val += getOrGenerateValue(st, contained);
		//
		// if(val == 0)
		// continue;
		//
		// boolean canceled = MinecraftForge.EVENT_BUS.post(new
		// CalculateCrucibleValueByRecipeEvent(stack, val, r));
		// if(canceled)
		// continue;
		//
		// value = Math.max(value, val);
		// }
		
		return value;
	}
	
	public static void reloadRecipes()
	{
		registered.clear();
		registered.clear();
		
		MinecraftForge.EVENT_BUS.post(new ReloadRegisteredCrucibleRecipesEvent());
		
		mapped.putAll(registered);
	}
	
	private static final String bake(ItemStack stack)
	{
		return stack.getItem().getUnlocalizedName() + "" + stack.getCount() + "" + stack.getItemDamage() + "" + stack.getTagCompound();
	}
	
	public static float getSmeltingValue(ItemStack container)
	{
		return getSmeltingValue(new MatcherItemStack(container));
	}
	
	public static float getSmeltingValue(String container)
	{
		return getSmeltingValue(new MatcherOreDict(container));
	}
	
	public static float getSmeltingValue(IMatcher<ItemStack> container)
	{
		float min = 0;
		
		for(IMatcher<ItemStack> e : mapped.keySet())
		{
			boolean match = e.matches(container.defaultInstance());
			if(match)
				min = Math.max(min, mapped.get(e));
		}
		
		return min;
	}
}