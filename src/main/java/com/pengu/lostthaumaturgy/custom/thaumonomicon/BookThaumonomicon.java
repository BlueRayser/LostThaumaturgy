package com.pengu.lostthaumaturgy.custom.thaumonomicon;

import net.minecraft.util.ResourceLocation;

import com.mrdimka.hammercore.bookAPI.Book;
import com.mrdimka.hammercore.bookAPI.BookCategory;
import com.pengu.lostthaumaturgy.LTInfo;

public class BookThaumonomicon extends Book
{
	public static final BookThaumonomicon instance = new BookThaumonomicon();
	
	private BookThaumonomicon()
    {
	    super(LTInfo.MOD_ID + ":thaumonomicon");
	    
	    customBackground = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/thaumonomicon_gui.png");
	    customEntryBackground = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/thaumonomicon_entry.png");
    }
}