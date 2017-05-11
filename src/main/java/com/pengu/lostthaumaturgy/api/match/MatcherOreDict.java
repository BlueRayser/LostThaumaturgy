package com.pengu.lostthaumaturgy.api.match;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MatcherOreDict implements IMatcher<ItemStack>
{
	private String oredict;
	
	public MatcherOreDict(String oredict)
	{
		this.oredict = oredict;
	}
	
	@Override
	public boolean matches(ItemStack t)
	{
		if(t.isEmpty()) return false;
		int[] ids = OreDictionary.getOreIDs(t);
		if(ids != null && ids.length > 0) for(int id : ids)
		{
			String od = OreDictionary.getOreName(id);
			if(od.equals(oredict)) return true;
		}
		return false;
	}

	@Override
    public ItemStack defaultInstance()
    {
		for(ItemStack stack : OreDictionary.getOres(oredict)) return stack.copy();
	    return ItemStack.EMPTY;
    }
}