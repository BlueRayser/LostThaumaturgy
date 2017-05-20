package com.pengu.lostthaumaturgy.custom.thaumonomicon;

import com.mrdimka.hammercore.bookAPI.BookCategory;

public class CategoryThaumonomicon extends BookCategory
{
	public CategoryThaumonomicon(String id)
    {
	    super(BookThaumonomicon.instance, id);
    }
	
	@Override
	public boolean isDisabled()
	{
	    return false;
	}
}