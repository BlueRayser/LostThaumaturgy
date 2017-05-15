package com.pengu.lostthaumaturgy.items;

import com.mrdimka.hammercore.bookAPI.ItemBook;
import com.pengu.lostthaumaturgy.LTInfo;

public class ItemThaumonomicon extends ItemBook
{
	public ItemThaumonomicon()
    {
	    setUnlocalizedName("thaumonomicon");
	    setMaxStackSize(1);
    }

	@Override
    public String getBookId()
    {
	    return LTInfo.MOD_ID + ":thaumonomicon";
    }
}