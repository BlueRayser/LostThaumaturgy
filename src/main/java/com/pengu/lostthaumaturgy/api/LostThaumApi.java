package com.pengu.lostthaumaturgy.api;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.api.research.ResearchCategories;
import com.pengu.lostthaumaturgy.api.research.ResearchCategoryList;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.api.research.ResearchManager;
import com.pengu.lostthaumaturgy.api.research.ResearchPage;
import com.pengu.lostthaumaturgy.intr.hc.rts.RecipeTypeCrucible.CrucibleRecipe;

public class LostThaumApi
{
	private static HashMap<int[], Object[]> keyCache = new HashMap();
	
	public static Object[] getCraftingRecipeKey(EntityPlayer player, ItemStack stack)
	{
		int[] key = new int[] { Item.getIdFromItem((Item) stack.getItem()), stack.getMetadata() };
		if(keyCache.containsKey(key))
		{
			if(keyCache.get(key) == null)
				return null;
			if(ResearchManager.isResearchComplete(player.getName(), (String) keyCache.get(key)[0]))
				return keyCache.get(key);
			return null;
		}
		for(ResearchCategoryList rcl : ResearchCategories.researchCategories.values())
		{
			for(ResearchItem ri : rcl.research.values())
			{
				if(ri.getPages() == null)
					continue;
				for(int a = 0; a < ri.getPages().length; ++a)
				{
					ResearchPage page = ri.getPages()[a];
					
//					if(page.recipe != null && page.recipe instanceof CrucibleRecipe[])
//					{
//						CrucibleRecipe[] crs;
//						for(CrucibleRecipe cr : crs = (CrucibleRecipe[]) page.recipe)
//						{
//							if(!cr.getRecipeOutput().isItemEqual(stack))
//								continue;
//							keyCache.put(key, new Object[] { ri.key, a });
//							if(!ResearchManager.isResearchComplete(player.getName(), ri.key))
//								continue;
//							return new Object[] { ri.key, a };
//						}
//						continue;
//					}
					
					if(page.recipeOutput == null || stack == null || !page.recipeOutput.isItemEqual(stack))
						continue;
					keyCache.put(key, new Object[] { ri.key, a });
					if(ResearchManager.isResearchComplete(player.getName(), ri.key))
						return new Object[] { ri.key, a };
					return null;
				}
			}
		}
		keyCache.put(key, null);
		return null;
	}
}