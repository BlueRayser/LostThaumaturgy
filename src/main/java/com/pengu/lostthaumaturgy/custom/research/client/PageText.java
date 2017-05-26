package com.pengu.lostthaumaturgy.custom.research.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.client.utils.TextDivider;

public class PageText implements IPage
{
	protected String text;
	protected String[] lines;
	
	public PageText(String text)
	{
		this.text = text;
	}
	
	@SideOnly(Side.CLIENT)
	public void render(int mouseX, int mouseY, EntityPlayer player)
	{
		if(lines == null)
			lines = TextDivider.divideByLenghtLimit(Minecraft.getMinecraft().fontRenderer, text, 238);
		int y = 2;
		for(String ln : lines)
		{
			String l = ln;
			// if()
			// l = SymbolsLT.convert(ln);
			Minecraft.getMinecraft().fontRenderer.drawString(l, 8, y += 10, 0, false);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void init(EntityPlayer player)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void addTooltip(int mouseX, int mouseY, List<String> tooltip, EntityPlayer player)
	{
		
	}
}