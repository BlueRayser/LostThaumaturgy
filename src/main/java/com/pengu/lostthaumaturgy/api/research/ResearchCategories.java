package com.pengu.lostthaumaturgy.api.research;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.LostThaumaturgy;

public class ResearchCategories
{
	public static LinkedHashMap<String, ResearchCategoryList> researchCategories = new LinkedHashMap();
	
	public static ResearchCategoryList getResearchList(String key)
	{
		return (ResearchCategoryList) researchCategories.get(key);
	}
	
	public static String getCategoryName(String key)
	{
		return I18n.format("bookapi.lostthaumaturgy:thaumonomicon:" + key);
	}
	
	public static ResearchItem getResearch(String key)
	{
		Collection<ResearchCategoryList> rc = researchCategories.values();
		Iterator<ResearchCategoryList> i$ = rc.iterator();
		
		while(i$.hasNext())
		{
			ResearchCategoryList cat = i$.next();
			Collection<ResearchItem> rl = cat.research.values();
			Iterator<ResearchItem> i$1 = rl.iterator();
			
			while(i$1.hasNext())
			{
				ResearchItem ri = i$1.next();
				if(ri.key.equals(key))
					return ri;
			}
		}
		
		return null;
	}
	
	public static void registerCategory(String key, ResourceLocation icon, ResourceLocation background)
	{
		if(getResearchList(key) == null)
		{
			ResearchCategoryList rl = new ResearchCategoryList(icon, background);
			researchCategories.put(key, rl);
		}
		
	}
	
	public static void addResearch(ResearchItem ri)
	{
		ResearchCategoryList rl = getResearchList(ri.category);
		if(rl != null && !rl.research.containsKey(ri.key))
		{
			if(!ri.isVirtual())
			{
				Iterator i$ = rl.research.values().iterator();
				
				while(i$.hasNext())
				{
					ResearchItem rr = (ResearchItem) i$.next();
					if(rr.displayColumn == ri.displayColumn && rr.displayRow == ri.displayRow)
					{
						LostThaumaturgy.LOG.error("Research [" + ri.getName() + "] not added as it overlaps with existing research [" + rr.getName() + "]");
						return;
					}
				}
			}
			
			rl.research.put(ri.key, ri);
			if(ri.displayColumn < rl.minDisplayColumn)
			{
				rl.minDisplayColumn = ri.displayColumn;
			}
			
			if(ri.displayRow < rl.minDisplayRow)
			{
				rl.minDisplayRow = ri.displayRow;
			}
			
			if(ri.displayColumn > rl.maxDisplayColumn)
			{
				rl.maxDisplayColumn = ri.displayColumn;
			}
			
			if(ri.displayRow > rl.maxDisplayRow)
			{
				rl.maxDisplayRow = ri.displayRow;
			}
		}
		
	}
}