package com.pengu.lostthaumaturgy.custom.thaumonomicon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiUtils;

import com.mrdimka.hammercore.bookAPI.BookCategory;
import com.mrdimka.hammercore.bookAPI.BookEntry;
import com.mrdimka.hammercore.bookAPI.BookPage;
import com.mrdimka.hammercore.bookAPI.pages.BookPageTextPlain;
import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.custom.research.client.IPage;
import com.pengu.lostthaumaturgy.custom.research.client.PageText;

public class EntryThaumonomicon extends BookEntry
{
	public final Research res;
	
	public EntryThaumonomicon(BookCategory category, String id, String title, Research res)
	{
		super(category, id, title);
		this.res = res;
		
		if(res.getPageHandler().thaumonomiconIcon != null)
			setIcon(res.getPageHandler().thaumonomiconIcon.get());
	}
	
	@Override
	public int getPageCount()
	{
		return res.getPageHandler().pageCount();
	}
	
	@Override
	public BookPage getPageAt(int index)
	{
		IPage page = res.getPageHandler().getPage(index);
		return new PageWrapper(page);
	}
	
	public class PageWrapper extends BookPage
	{
		IPage page;
		
		public PageWrapper(IPage page)
		{
			super(EntryThaumonomicon.this);
			this.page = page;
		}
		
		private List<String> tooltip = new ArrayList<>();
		
		@Override
		public void render(int mouseX, int mouseY)
		{
			page.render(mouseX, mouseY, Minecraft.getMinecraft().player);
			page.addTooltip(mouseX, mouseY, tooltip, Minecraft.getMinecraft().player);
			
			if(!tooltip.isEmpty())
			{
				GuiUtils.drawHoveringText(tooltip, mouseX, mouseY, Minecraft.getMinecraft().currentScreen.width, Minecraft.getMinecraft().currentScreen.height, Minecraft.getMinecraft().currentScreen.width, Minecraft.getMinecraft().fontRenderer);
				tooltip.clear();
			}
		}
		
		@Override
		public void prepare()
		{
			if(page != null)
				page.init(Minecraft.getMinecraft().player);
		}
	}
}