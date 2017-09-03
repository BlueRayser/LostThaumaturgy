package com.pengu.lostthaumaturgy.api.research;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.base.Predicate;
import com.pengu.lostthaumaturgy.api.tiles.IInfuser;
import com.pengu.lostthaumaturgy.core.items.ItemResearch;
import com.pengu.lostthaumaturgy.core.items.ItemResearch.EnumResearchItemType;

public class ResearchPredicate implements Predicate<IInfuser>
{
	private ResearchItem[] researches;
	
	public ResearchPredicate(ResearchItem... researches)
	{
		this.researches = researches;
	}
	
	public ResearchPredicate(String... researches)
	{
		List<ResearchItem> ris = new ArrayList<>();
		for(String r : researches)
		{
			ResearchItem ri = ResearchManager.getById(r);
			if(ri != null)
				ris.add(ri);
		}
		
		this.researches = new ResearchItem[ris.size()];
		for(int i = 0; i < this.researches.length; ++i)
			this.researches[i] = ris.get(i);
	}
	
	public ItemStack[] getResearchItems(EnumResearchItemType type)
	{
		ItemStack[] stacks = new ItemStack[researches.length];
		for(int i = 0; i < stacks.length; ++i)
			stacks[i] = ItemResearch.create(researches[i], type);
		return stacks;
	}
	
	@Override
	public boolean apply(IInfuser input)
	{
		if(input == null)
			return false;
		EntityPlayer player = input.getInitiator();
		if(player == null)
			return false;
		for(ResearchItem r : researches)
			if(!ResearchManager.isResearchComplete(player.getName(), r.key))
				return false;
		return true;
	}
}