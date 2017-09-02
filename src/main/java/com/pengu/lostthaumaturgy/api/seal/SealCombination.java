package com.pengu.lostthaumaturgy.api.seal;

import com.pengu.lostthaumaturgy.core.tile.TileSeal;

public class SealCombination
{
	public final ItemSealSymbol[] slots = new ItemSealSymbol[3];
	
	public SealCombination(ItemSealSymbol i, ItemSealSymbol j, ItemSealSymbol k)
	{
		slots[0] = i;
		slots[1] = j;
		slots[2] = k;
	}
	
	/**
	 * Used to invoke a static method via reflection. <br>
	 * Format: com.package.RenderClass.methodName
	 */
	public String getRender(TileSeal seal, int index)
	{
		return "com.pengu.lostthaumaturgy.client.render.seal.LTSealRenders.renderNone";
	}
	
	public boolean isValid(TileSeal seal)
	{
		for(int i = 0; i < 3; ++i)
			if(slots[i] != seal.getSymbol(i))
				return false;
		return true;
	}
}