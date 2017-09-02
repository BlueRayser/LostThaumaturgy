package com.pengu.lostthaumaturgy;

import java.util.Objects;

import net.minecraftforge.common.config.Configuration;

import com.pengu.hammercore.cfg.HCModConfigurations;
import com.pengu.hammercore.cfg.IConfigReloadListener;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyBool;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyFloat;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyInt;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyStringList;
import com.pengu.hammercore.var.IVariable;
import com.pengu.hammercore.var.types.VariableString;
import com.pengu.lostthaumaturgy.core.Info;

@HCModConfigurations(modid = Info.MOD_ID)
public class LTConfigs implements IConfigReloadListener
{
	@ModConfigPropertyBool(category = "Client", name = "Crucible Value Tooltip", defaultValue = true, comment = "Should we add tooltip with smelting value of an item in a crucible?")
	public static boolean enableCrucibleValueTooltips;
	
	@ModConfigPropertyBool(category = "Client", name = "Use Shaders", defaultValue = true, comment = "Should we use shaders? This only works if your hardware supports them.")
	public static boolean client_useShaders;
	
	@ModConfigPropertyInt(category = "Aura", name = "Aura Capacity", comment = "The maximum taint and aura amount. Changing this setting with a world in progress can do funny stuff - you have been warned.", defaultValue = 15000, min = 5000, max = 32000)
	public static int aura_max;
	
	@ModConfigPropertyFloat(category = "Aura", name = "Max Radiation", comment = "The maximal radiation in the chunk.", defaultValue = 12F, min = 7F, max = 10000F)
	public static float aura_radMax;
	
	@ModConfigPropertyInt(category = "Aura", name = "Taint Spawn", comment = "How often tainted chunks spawn at world creation: 0 = none, 1 = default, 2 = common and with high taint levels", defaultValue = 1, min = 0, max = 2)
	public static int taint_spawn;
	
	@ModConfigPropertyStringList(category = "Aura", name = "Taintable Blocks", comment = "What blocks could get tainted by tainted soil?\nFormatting:\nmodid:blockname\nIf it is from vanilla, you don't have to use minecraft prefix.\nTaint DOES store TileEntity so you can add something like \"furnace\" and it is going to work fine.", defaultValue = { "dirt", "grass", "sand", "gravel", "stone", "cobblestone", "coal_ore", "iron_ore", "lapis_ore", "gold_ore", "diamond_ore", "emerald_ore", "redstone_ore", "lit_redstone_ore" }, allowedValues = {})
	public static String[] taint_taintableBlocks = {};
	
	@ModConfigPropertyBool(category = "Effects", name = "AFK", comment = "Should Lost Thaumatugy spawn AFK particles for players that are AFK for more than 1 minute?", defaultValue = true)
	public static boolean effects_AFK;
	
	@ModConfigPropertyBool(category = "Effects", name = "Damage", comment = "Should Lost Thaumatugy spawn Damage particles for players that is getting attacked?", defaultValue = true)
	public static boolean effects_Damage;
	
	public static Configuration cfgs;
	
	public static final IVariable<String> var_aura_max_str = new VariableString(Info.MOD_ID + ":aura");
	public static short sync_aura_max;
	public static float sync_aura_rad_max;
	
	public static void updateAura()
	{
		String our = sync_aura_max + "/" + sync_aura_rad_max;
		if(!Objects.equals(our, var_aura_max_str.get()))
		{
			try
			{
				String[] ss = var_aura_max_str.get().split("/");
				sync_aura_max = Short.parseShort(ss[0]);
				sync_aura_rad_max = Float.parseFloat(ss[1]);
			} catch(Throwable err)
			{
			}
		}
	}
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		LTConfigs.cfgs = cfgs;
		var_aura_max_str.set(aura_max + "/" + aura_radMax);
	}
}