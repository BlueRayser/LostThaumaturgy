package com.pengu.lostthaumaturgy.custom.thaumonomicon;

import com.mrdimka.hammercore.bookAPI.Book;
import com.pengu.lostthaumaturgy.LTInfo;

public class BookThaumonomicon extends Book
{
	public static final BookThaumonomicon instance = new BookThaumonomicon();
	
	private BookThaumonomicon()
    {
	    super(LTInfo.MOD_ID + ":thaumonomicon");
    }
}