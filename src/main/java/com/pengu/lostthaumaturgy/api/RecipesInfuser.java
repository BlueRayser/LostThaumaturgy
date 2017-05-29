package com.pengu.lostthaumaturgy.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.pengu.lostthaumaturgy.api.tiles.IInfuser;
import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.custom.research.ResearchPredicate;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;

public class RecipesInfuser
{
	private static ArrayList<ItemStack> resultList = new ArrayList();
	private static ArrayList<ItemStack[]> componentList = new ArrayList();
	private static ArrayList<Integer> costList = new ArrayList();
	private static ArrayList<Boolean> darkList = new ArrayList();
	private static ArrayList<Predicate<IInfuser>> conditions = new ArrayList();
	
	public static void addInfusing(ItemStack result, int cost, ItemStack... components)
	{
		addInfusing(result, cost, false, components);
	}
	
	public static void addInfusing(ItemStack result, int cost, Predicate<IInfuser> craftChecker, ItemStack... components)
	{
		addInfusing(result, cost, craftChecker, false, components);
	}
	
	public static void addDarkInfusing(ItemStack result, int cost, ItemStack... components)
	{
		addInfusing(result, cost, true, components);
	}
	
	public static void addDarkInfusing(ItemStack result, int cost, Predicate<IInfuser> craftChecker, ItemStack... components)
	{
		addInfusing(result, cost, craftChecker, true, components);
	}
	
	public static Predicate<IInfuser> createPredicateFromResearches(Research... researches)
	{
		return new ResearchPredicate(researches);
	}
	
	public static void addInfusing(ItemStack result, int cost, Predicate<IInfuser> craftChecker, boolean isdark, ItemStack... components)
	{
		resultList.add(result);
		componentList.add(components);
		costList.add(cost);
		darkList.add(isdark);
		conditions.add(craftChecker);
	}
	
	public static void addInfusing(ItemStack result, int cost, boolean isdark, ItemStack... components)
	{
		resultList.add(result);
		componentList.add(components);
		costList.add(cost);
		darkList.add(isdark);
		conditions.add(Predicates.alwaysTrue());
	}
	
	public static ItemStack getInfusingResult(Object[] components, IInfuser infuser)
	{
		try
		{
			int entry = findEntry(components, infuser);
			if(infuser == null || !conditions.get(entry).apply(infuser))
				return ItemStack.EMPTY;
			ItemStack res = (ItemStack) resultList.get(entry);
			if(!darkList.get(entry).booleanValue())
				return res;
			return ItemStack.EMPTY;
		} catch(IndexOutOfBoundsException e)
		{
			return ItemStack.EMPTY;
		}
	}
	
	public static ItemStack getInfusingResult(Object[] components, boolean isdark, IInfuser infuser)
	{
		try
		{
			int entry = findEntry(components, infuser);
			if(infuser == null || !conditions.get(entry).apply(infuser))
				return ItemStack.EMPTY;
			ItemStack res = (ItemStack) resultList.get(entry);
			if(darkList.get(entry) == isdark)
				return res;
			return ItemStack.EMPTY;
		} catch(IndexOutOfBoundsException e)
		{
			return ItemStack.EMPTY;
		}
	}
	
	public static int getInfusingCost(ItemStack is)
	{
		for(int a = 0; a < resultList.size(); ++a)
		{
			if(!resultList.get(a).isItemEqual(is) || darkList.get(a).booleanValue())
				continue;
			return costList.get(a);
		}
		return -1;
	}
	
	public static int getInfusingCost(ItemStack is, boolean isdark)
	{
		for(int a = 0; a < resultList.size(); ++a)
		{
			if(!((ItemStack) resultList.get(a)).isItemEqual(is) || (Boolean) darkList.get(a) != isdark)
				continue;
			return (Integer) costList.get(a);
		}
		return -1;
	}
	
	public static boolean isComponentFor(ItemStack component, ItemStack result, boolean isdark)
	{
		for(int a = 0; a < resultList.size(); ++a)
		{
			if(!((ItemStack) resultList.get(a)).isItemEqual(result) || (Boolean) darkList.get(a) != isdark)
				continue;
			for(ItemStack comp : (ItemStack[]) componentList.get(a))
			{
				if(!component.isItemEqual(comp) && (comp.getItemDamage() != -1 || comp.getItem() != component.getItem()))
					continue;
				return true;
			}
		}
		return false;
	}
	
	public static ItemStack[] getInfusingComponents(ItemStack is, boolean isdark)
	{
		for(int a = 0; a < resultList.size(); ++a)
		{
			if(!((ItemStack) resultList.get(a)).isItemEqual(is) || (Boolean) darkList.get(a) != isdark)
				continue;
			return (ItemStack[]) componentList.get(a);
		}
		return null;
	}
	
	public static ItemStack[] getInfusingComponents(ItemStack is)
	{
		for(int a = 0; a < resultList.size(); ++a)
		{
			if(!((ItemStack) resultList.get(a)).isItemEqual(is) || ((Boolean) darkList.get(a)).booleanValue())
				continue;
			return (ItemStack[]) componentList.get(a);
		}
		return null;
	}
	
	public static int getInfusingCost(Object[] components, boolean isdark, IInfuser infuser)
	{
		try
		{
			int entry = findEntry(components, infuser);
			if(infuser == null || !conditions.get(entry).apply(infuser))
				return 0;
			int res = (Integer) costList.get(entry);
			if(darkList.get(entry) == isdark)
				return res;
			return 0;
		} catch(IndexOutOfBoundsException e)
		{
			return 0;
		}
	}
	
	public static int getInfusingCost(Object[] components, IInfuser infuser)
	{
		try
		{
			int entry = findEntry(components, infuser);
			if(infuser == null || !conditions.get(entry).apply(infuser))
				return 0;
			int res = (Integer) costList.get(entry);
			if(!((Boolean) darkList.get(entry)).booleanValue())
				return res;
			return 0;
		} catch(IndexOutOfBoundsException e)
		{
			return 0;
		}
	}
	
	public static Predicate<IInfuser> getPredicate(int entry)
	{
		return conditions.get(entry);
	}
	
	public static int findEntry(Object[] components, IInfuser infuser)
	{
		block0: for(int a = 0; a < componentList.size(); ++a)
		{
			if(infuser == null || !conditions.get(a).apply(infuser))
				continue;
			
			ItemStack[] cl = (ItemStack[]) componentList.get(a);
			ArrayList<Integer> exclude = new ArrayList<Integer>();
			int cFound = cl.length;
			for(Object cI : components)
			{
				boolean foundsomething = false;
				if(cFound == 0)
					return -1;
				for(int q = 0; q < cl.length; ++q)
				{
					if(exclude.contains(q) || ((ItemStack) cI).getItem() != cl[q].getItem() || ((ItemStack) cI).getItemDamage() != cl[q].getItemDamage() && cl[q].getItemDamage() == -1)
						continue;
					--cFound;
					exclude.add(q);
					foundsomething = true;
					break;
				}
				if(!foundsomething)
					continue block0;
			}
			if(cFound != 0)
				continue;
			return a;
		}
		return -1;
	}
	
	/** Do not use --- this is used ONLY in JEI */
	public static class InfuserRecipe
	{
		public final ItemStack result;
		public final ItemStack[] components;
		public final ItemStack[] discoveries;
		public final Predicate<IInfuser> predicate;
		public final int cost;
		public final int depletedShards;
		
		public InfuserRecipe(int id)
		{
			if(darkList.get(id) == Boolean.TRUE)
				throw new RuntimeException("Unable to compile dark infuser recipe!");
			result = resultList.get(id);
			predicate = conditions.get(id);
			components = componentList.get(id);
			cost = costList.get(id);
			
			int dep = 0;
			for(ItemStack stack : components)
				if(isCrystal(stack))
					dep++;
			depletedShards = dep;
			
			if(predicate instanceof ResearchPredicate)
			{
				ResearchPredicate pred = (ResearchPredicate) predicate;
				discoveries = pred.getResearchItems(EnumResearchItemType.DISCOVERY);
			} else
				discoveries = new ItemStack[0];
		}
		
		public static InfuserRecipe[] present()
		{
			List<InfuserRecipe> recipes = new ArrayList<>();
			
			for(int i = 0; i < darkList.size(); ++i)
			{
				boolean isDark = darkList.get(i) == Boolean.FALSE;
				if(isDark)
					recipes.add(new InfuserRecipe(i));
			}
			
			return recipes.toArray(new InfuserRecipe[recipes.size()]);
		}
	}
	
	/** Do not use --- this is used ONLY in JEI */
	public static class DarkInfuserRecipe
	{
		public final ItemStack result;
		public final ItemStack[] components;
		public final ItemStack[] discoveries;
		public final Predicate<IInfuser> predicate;
		public final int cost;
		
		public DarkInfuserRecipe(int id)
		{
			if(darkList.get(id) == Boolean.FALSE)
				throw new RuntimeException("Unable to compile normal infuser recipe!");
			result = resultList.get(id).copy();
			predicate = conditions.get(id);
			components = componentList.get(id);
			cost = costList.get(id);
			
			if(predicate instanceof ResearchPredicate)
			{
				ResearchPredicate pred = (ResearchPredicate) predicate;
				discoveries = pred.getResearchItems(EnumResearchItemType.DISCOVERY);
			} else
				discoveries = new ItemStack[0];
		}
		
		public static DarkInfuserRecipe[] present()
		{
			List<DarkInfuserRecipe> recipes = new ArrayList<>();
			
			for(int i = 0; i < darkList.size(); ++i)
			{
				boolean isDark = darkList.get(i) == Boolean.TRUE;
				if(isDark)
					recipes.add(new DarkInfuserRecipe(i));
			}
			
			return recipes.toArray(new DarkInfuserRecipe[recipes.size()]);
		}
	}
	
	private static boolean isCrystal(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItem() == ItemsLT.MULTI_MATERIAL && (stack.getItemDamage() == EnumMultiMaterialType.AQUEOUS_CRYSTAL.getDamage() || stack.getItemDamage() == EnumMultiMaterialType.EARTHEN_CRYSTAL.getDamage() || stack.getItemDamage() == EnumMultiMaterialType.FIERY_CRYSTAL.getDamage() || stack.getItemDamage() == EnumMultiMaterialType.TAINTED_CRYSTAL.getDamage() || stack.getItemDamage() == EnumMultiMaterialType.VAPOROUS_CRYSTAL.getDamage() || stack.getItemDamage() == EnumMultiMaterialType.VIS_CRYSTAL.getDamage());
	}
}