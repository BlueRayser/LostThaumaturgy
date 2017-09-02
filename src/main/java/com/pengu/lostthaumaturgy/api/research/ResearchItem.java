package com.pengu.lostthaumaturgy.api.research;

import java.util.Random;

import com.pengu.lostthaumaturgy.LTConfigs;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ResearchItem
{
	public final String key;
	public final String category;
	public int color;
	public String[] parents = null;
	public String[] parentsHidden = null;
	public String[] siblings = null;
	public final int displayColumn;
	public final int displayRow;
	public final ItemStack icon_item;
	public final ResourceLocation icon_resource;
	private float complexity;
	private boolean isSpecial;
	private boolean isSecondary;
	private boolean isRound;
	private boolean isStub;
	private boolean isVirtual;
	private boolean isConcealed;
	private boolean isHidden;
	private boolean isLost;
	private boolean isAutoUnlock;
	private ResearchPage[] pages = null;
	
	public ResearchItem(String key, String category)
	{
		this.key = key;
		this.category = category;
		this.icon_resource = null;
		this.icon_item = null;
		this.displayColumn = 0;
		this.displayRow = 0;
		this.setVirtual();
	}
	
	public ResearchItem(String key, String category, int col, int row, float complex, ResourceLocation icon)
	{
		this.key = key;
		this.category = category;
		this.icon_resource = icon;
		this.icon_item = null;
		this.displayColumn = col;
		this.displayRow = row;
		this.complexity = complex;
		if(this.complexity < 0)
			this.complexity = 0;
		if(this.complexity > 100)
			this.complexity = 100;
	}
	
	public ResearchItem(String key, String category, int col, int row, float complex, ItemStack icon)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icon;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
		this.complexity = complex;
		if(this.complexity < 0)
			this.complexity = 0;
		if(this.complexity > 100)
			this.complexity = 100;
	}
	
	public ResearchItem setSpecial()
	{
		this.isSpecial = true;
		return this;
	}
	
	public ResearchItem setStub()
	{
		this.isStub = true;
		return this;
	}
	
	public ResearchItem setLost()
	{
		this.isLost = true;
		return this;
	}
	
	public ResearchItem setConcealed()
	{
		this.isConcealed = true;
		return this;
	}
	
	public ResearchItem setHidden()
	{
		this.isHidden = true;
		return this;
	}
	
	public ResearchItem setVirtual()
	{
		this.isVirtual = true;
		return this;
	}
	
	public ResearchItem setParents(String... par)
	{
		this.parents = par;
		return this;
	}
	
	public ResearchItem setParentsHidden(String... par)
	{
		this.parentsHidden = par;
		return this;
	}
	
	public ResearchItem setSiblings(String... sib)
	{
		this.siblings = sib;
		return this;
	}
	
	public ResearchItem setPages(ResearchPage... par)
	{
		this.pages = par;
		return this;
	}
	
	public ResearchPage[] getPages()
	{
		return this.pages;
	}
	
	public ResearchItem registerResearch()
	{
		ResearchCategories.addResearch(this);
		return this;
	}
	
	public String getName()
	{
		return I18n.format("lt.research_name." + this.key);
	}
	
	public String getText()
	{
		return I18n.format("lt.research_text." + this.key);
	}
	
	public boolean isSpecial()
	{
		return this.isSpecial;
	}
	
	public boolean isStub()
	{
		return this.isStub;
	}
	
	public boolean isLost()
	{
		return this.isLost;
	}
	
	public boolean isConcealed()
	{
		return this.isConcealed;
	}
	
	public boolean isHidden()
	{
		return this.isHidden;
	}
	
	public boolean isVirtual()
	{
		return this.isVirtual;
	}
	
	public boolean isAutoUnlock()
	{
		return this.isAutoUnlock;
	}
	
	public ResearchItem setAutoUnlock()
	{
		this.isAutoUnlock = true;
		return this;
	}
	
	public boolean isRound()
	{
		return this.isRound;
	}
	
	public ResearchItem setRound()
	{
		this.isRound = true;
		return this;
	}
	
	public boolean isSecondary()
	{
		return this.isSecondary;
	}
	
	public ResearchItem setSecondary()
	{
		this.isSecondary = true;
		return this;
	}
	
	public float getComplexity()
	{
		return this.complexity;
	}
	
	public String getComplexityLabel()
	{
		GetComplexityEvent e = new GetComplexityEvent(getComplexity());
		MinecraftForge.EVENT_BUS.post(e);
		return e.getComplexityLabel();
	}
	
	public ResearchItem setComplexity(int complexity)
	{
		this.complexity = complexity;
		return this;
	}
	
	private Random rand = new Random();
	
	public int getColor()
	{
		if(color == 0)
		{
			rand.setSeed(key.hashCode() + category.hashCode() + displayColumn + displayRow + (int) (complexity * 100));
			return rand.nextInt(0xFFFFFF);
		}
		
		return color;
	}
	
	public ResearchItem setColor(int color)
	{
		this.color = color;
		return this;
	}
	
	public static class GetComplexityEvent extends Event
	{
		public String label;
		public float complexity;
		
		public GetComplexityEvent(float complexity)
		{
			this.complexity = complexity;
		}
		
		public String getComplexityLabel()
		{
			if(label != null)
				return label;
			if(complexity <= 5)
				return "Trivial";
			if(complexity <= 10)
				return "Easy";
			if(complexity <= 30)
				return "Moderate";
			if(complexity <= 50)
				return "Hard";
			if(complexity <= 75)
				return "Tricky";
			if(complexity <= 99)
				return "Tortuous";
			if(complexity <= 100)
				return "Almost Impossible";
			return "Impossible";
		}
	}
	
	public boolean canObtainFrom(ItemStack baseStack, EntityPlayer initiator)
	{
		return true;
	}
}