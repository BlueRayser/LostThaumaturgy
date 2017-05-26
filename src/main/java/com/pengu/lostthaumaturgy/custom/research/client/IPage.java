package com.pengu.lostthaumaturgy.custom.research.client;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPage
{
	@SideOnly(Side.CLIENT)
	void init(EntityPlayer player);
	
	@SideOnly(Side.CLIENT)
	void render(int mouseX, int mouseY, EntityPlayer player);
	
	@SideOnly(Side.CLIENT)
	void addTooltip(int mouseX, int mouseY, List<String> tooltip, EntityPlayer player);
}