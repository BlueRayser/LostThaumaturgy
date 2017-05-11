package com.pengu.lostthaumaturgy;

import com.mrdimka.hammercore.cfg.HCModConfigurations;
import com.mrdimka.hammercore.cfg.IConfigReloadListener;
import com.mrdimka.hammercore.cfg.fields.ModConfigPropertyBool;
import com.mrdimka.hammercore.cfg.fields.ModConfigPropertyInt;

@HCModConfigurations(modid = LTInfo.MOD_ID)
public class LTConfigs implements IConfigReloadListener
{
	@ModConfigPropertyBool(category = "Client", name = "Crucible Value Tooltip", defaultValue = true, comment = "Should TA add tooltip with smelting value of an item in a crucible?")
	public static boolean enableCrucibleValueTooltips;
	
	@ModConfigPropertyInt(category = "Aura", name = "Aura Capacity", comment = "The maximum taint and aura amount. Changing this setting with a world in progress can do funny stuff - you have been warned.", defaultValue = 15000, min = 5000, max = 60000)
	public static int auraMax;
	
	@ModConfigPropertyInt(category = "Taint", name = "Taint Spawn", comment = "How often tainted chunks spawn at world creation: 0 = none, 1 = default, 2 = common and with high taint levels", defaultValue = 1, min = 0, max = 2)
	public static int taintSpawn;
}