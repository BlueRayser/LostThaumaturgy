package com.pengu.lostthaumaturgy.custom.thaumonomicon.api;

import com.mrdimka.hammercore.bookAPI.BookCategory;
import com.mrdimka.hammercore.bookAPI.BookEntry;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.BookThaumonomicon;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.EntryThaumonomicon;

import net.minecraft.item.ItemStack;

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