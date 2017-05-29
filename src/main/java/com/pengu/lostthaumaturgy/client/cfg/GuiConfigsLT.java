package com.pengu.lostthaumaturgy.client.cfg;

import net.minecraft.client.gui.GuiScreen;

import com.mrdimka.hammercore.cfg.gui.HCConfigGui;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.LTInfo;

public class GuiConfigsLT extends HCConfigGui
{
	public GuiConfigsLT(GuiScreen prev)
	{
		super(prev, LTConfigs.cfgs, LTInfo.MOD_ID);
	}
}