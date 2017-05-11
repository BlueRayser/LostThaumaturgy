package com.pengu.lostthaumaturgy.custom.research;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.base.Predicate;
import com.pengu.lostthaumaturgy.api.tiles.IInfuser;

public class ResearchPredicate implements Predicate<IInfuser>
{
	private final Research[] researches;
	
	public ResearchPredicate(Research... researches)
    {
		this.researches = researches;
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