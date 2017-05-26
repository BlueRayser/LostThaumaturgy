package com.pengu.lostthaumaturgy.tile;

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