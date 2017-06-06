package com.pengu.lostthaumaturgy.api.seal;

import java.util.ArrayList;
import java.util.List;

import com.pengu.lostthaumaturgy.tile.TileSeal;

public class SealManager
{
	private static final List<SealCombination> combinations = new ArrayList<>();
	
	public static void registerCombination(SealCombination combination)
	{
		combinations.add(combination);
	}
	
	public static SealCombination getCombination(TileSeal seal)
	{
		for(int i = 0; i < combinations.size(); ++i)
			if(combinations.get(i).isValid(seal))
				return combinations.get(i);
		return null;
	}
}