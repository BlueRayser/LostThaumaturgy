package com.pengu.lostthaumaturgy.core.tile;

public class TileReinforcedVisTank extends TileVisTank
{
	{
		breakchance = 3333;
		canBreak = false;
	}
	
	@Override
	public float getMaxVis()
	{
		return 1000;
	}
}