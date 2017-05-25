package com.pengu.lostthaumaturgy.custom.research;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.base.Predicate;
import com.pengu.lostthaumaturgy.api.tiles.IInfuser;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;

public class ResearchPredicate implements Predicate<IInfuser>
{
	private final Research[] researches;
	
	public ResearchPredicate(Research... researches)
    {
		this.researches = researches;
    }
	
	public ItemStack[] getResearchItems(EnumResearchItemType type)
	{
		ItemStack[] stacks = new ItemStack[researches.length];
		for(int i = 0; i < stacks.length; ++i) stacks[i] = ItemResearch.create(researches[i], type);
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
		for(Research r : researches)
			if(!r.isCompleted(player))
				return false;
		return true;
	}
}