package com.pengu.lostthaumaturgy.custom.thaumonomicon.api;

import net.minecraft.item.ItemStack;

import com.mrdimka.hammercore.bookAPI.BookCategory;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.BookThaumonomicon;

public class ClientThaumonomicon implements IThaumonomicon
{
	@Override
	public void setCategoryItemIcon(ItemStack stack, String category)
	{
		BookThaumonomicon tm = BookThaumonomicon.instance;
		for(BookCategory cat : tm.categories)
			if(cat.categoryId.equals(category))
				cat.setIcon(stack);
	}
}