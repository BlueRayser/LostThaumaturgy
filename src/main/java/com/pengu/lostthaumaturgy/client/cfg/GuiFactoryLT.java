package com.pengu.lostthaumaturgy.client.cfg;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;

public class GuiFactoryLT implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		LostThaumaturgy.LOG.info("Created " + LTInfo.MOD_NAME + " Gui Config Factory!");
	}
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return GuiConfigsLT.class;
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}
}