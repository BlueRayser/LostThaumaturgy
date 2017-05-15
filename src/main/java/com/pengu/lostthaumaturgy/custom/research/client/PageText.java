package com.pengu.lostthaumaturgy.custom.research.client;

import java.util.List;

import com.mrdimka.hammercore.client.utils.TextDivider;
import com.pengu.lostthaumaturgy.util.SymbolsLT;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		if(lines == null) lines = TextDivider.divideByLenghtLimit(Minecraft.getMinecraft().fontRendererObj, text, 238);
		int y = 2;
		for(String ln : lines)
		{
			String l = ln;
//			if() 
//				l = SymbolsLT.convert(ln);
			Minecraft.getMinecraft().fontRendererObj.drawString(l, 8, y += 10, 0, false);
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