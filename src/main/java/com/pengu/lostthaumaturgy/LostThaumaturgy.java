package com.pengu.lostthaumaturgy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mrdimka.hammercore.common.utils.WrappedLog;
import com.mrdimka.hammercore.init.SimpleRegistration;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.creative.CreativeTabLT;
import com.pengu.lostthaumaturgy.creative.CreativeTabResearches;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.EntitiesLT;
import com.pengu.lostthaumaturgy.init.InfuserLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.init.RecipesLT;
import com.pengu.lostthaumaturgy.init.ResearchesLT;
import com.pengu.lostthaumaturgy.proxy.CommonProxy;
import com.pengu.lostthaumaturgy.worldgen.WorldGenCrystals;
import com.pengu.lostthaumaturgy.worldgen.WorldGenSilverwood;

@Mod(modid = LTInfo.MOD_ID, name = LTInfo.MOD_NAME, version = LTInfo.MOD_VERSION, dependencies = "required-after:hammercore")
public class LostThaumaturgy
{
	@Instance
	public static LostThaumaturgy instance;
	
	@SidedProxy(clientSide = LTInfo.CLIENT_PROXY, serverSide = LTInfo.SERVER_PROXY)
	public static CommonProxy proxy;
	
	public static final CreativeTabs //
	        tab = new CreativeTabLT(),
	        tab_researches = new CreativeTabResearches();
	
	public static final WrappedLog LOG = new WrappedLog(LTInfo.MOD_NAME);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		MinecraftForge.EVENT_BUS.register(proxy);
		
		SimpleRegistration.registerFieldBlocksFrom(BlocksLT.class, LTInfo.MOD_ID, tab);
		SimpleRegistration.registerFieldItemsFrom(ItemsLT.class, LTInfo.MOD_ID, tab);
		ResearchesLT.registerResearches();
		
		ItemsLT.MULTI_MATERIAL.registerOD();
		
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		proxy.init();
		
		RecipesLT.registerRecipes();
		InfuserLT.registerInfuser();
		InfuserLT.registerDarkInfuser();
		EntitiesLT.registerEntities();
		
		GameRegistry.registerWorldGenerator(new WorldGenCrystals(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenSilverwood(), 4);
		
		FMLInterModComms.sendMessage("waila", "register", "com.pengu.lostthaumaturgy.intr.waila.WailaLTProvider.register");
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent evt)
	{
		RecipesCrucible.reloadRecipes();
		AuraTicker.LoadAuraData();
	}
	
	@EventHandler
	public void serverStop(FMLServerStoppingEvent evt)
	{
	}
}