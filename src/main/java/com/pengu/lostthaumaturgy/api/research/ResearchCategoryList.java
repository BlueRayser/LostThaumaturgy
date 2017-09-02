package com.pengu.lostthaumaturgy.api.research;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class ResearchCategoryList
{
	public int minDisplayColumn;
	public int minDisplayRow;
	public int maxDisplayColumn;
	public int maxDisplayRow;
	public ResourceLocation icon;
	public ResourceLocation background;
	public Map<String, ResearchItem> research = new HashMap<>();
	
	public ResearchCategoryList(ResourceLocation icon, ResourceLocation background)
	{
		this.icon = icon;
		this.background = background;
	}
}