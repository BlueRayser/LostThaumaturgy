package com.pengu.lostthaumaturgy;

import net.minecraftforge.common.config.Configuration;

import com.mrdimka.hammercore.cfg.HCModConfigurations;
import com.mrdimka.hammercore.cfg.IConfigReloadListener;
import com.mrdimka.hammercore.cfg.fields.ModConfigPropertyBool;
import com.mrdimka.hammercore.cfg.fields.ModConfigPropertyFloat;
import com.mrdimka.hammercore.cfg.fields.ModConfigPropertyInt;
import com.mrdimka.hammercore.cfg.fields.ModConfigPropertyStringList;

@HCModConfigurations(modid = LTInfo.MOD_ID)
public class LTConfigs implements IConfigReloadListener
{
	@ModConfigPropertyBool(category = "Client", name = "Crucible Value Tooltip", defaultValue = true, comment = "Should we add tooltip with smelting value of an item in a crucible?")
	public static boolean enableCrucibleValueTooltips;
	
	@ModConfigPropertyBool(category = "Client", name = "Use Shaders", defaultValue = true, comment = "Should we use shaders? This only works if your hardware supports them.")
	public static boolean client_useShaders;
	
	@ModConfigPropertyInt(category = "Aura", name = "Aura Capacity", comment = "The maximum taint and aura amount. Changing this setting with a world in progress can do funny stuff - you have been warned.", defaultValue = 15000, min = 5000, max = 60000)
	public static int aura_max;
	
	@ModConfigPropertyFloat(category = "Aura", name = "Max Radiation", comment = "The maximal radiation in the chunk.", defaultValue = 12F, min = 7F, max = 10000F)
	public static float aura_radMax;
	
	@ModConfigPropertyInt(category = "Taint", name = "Taint Spawn", comment = "How often tainted chunks spawn at world creation: 0 = none, 1 = default, 2 = common and with high taint levels", defaultValue = 1, min = 0, max = 2)
	public static int taint_spawn;
	
	@ModConfigPropertyStringList(category = "Taint", name = "Taintable Blocks", comment = "What blocks could get tainted by tainted soil?\nFormatting:\nmodid:blockname\nIf it is from vanilla, you don't have to use minecraft prefix.\nTaint DOES store TileEntity so you can add something like \"furnace\" and it is going to work fine.", defaultValue = { "dirt", "grass", "sand", "gravel", "stone", "cobblestone", "coal_ore", "iron_ore", "lapis_ore", "gold_ore", "diamond_ore", "emerald_ore", "redstone_ore", "lit_redstone_ore" }, allowedValues = {})
	public static String[] taint_taintableBlocks = {};
	
	public static Configuration cfgs;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		LTConfigs.cfgs = cfgs;
	}
}