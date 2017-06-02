package com.pengu.lostthaumaturgy;

import java.util.Iterator;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mrdimka.hammercore.common.blocks.tesseract.TileTesseract;
import com.mrdimka.hammercore.common.utils.WrappedLog;
import com.mrdimka.hammercore.init.SimpleRegistration;
import com.mrdimka.hammercore.world.WorldGenRegistry;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.api.tiles.CapabilityVisConnection;
import com.pengu.lostthaumaturgy.creative.CreativeTabLT;
import com.pengu.lostthaumaturgy.creative.CreativeTabResearches;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.entity.EntitySmartZombie;
import com.pengu.lostthaumaturgy.entity.EntityThaumSlime;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.EntitiesLT;
import com.pengu.lostthaumaturgy.init.InfuserLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.init.RecipesLT;
import com.pengu.lostthaumaturgy.init.ResearchesLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.proxy.CommonProxy;
import com.pengu.lostthaumaturgy.worldgen.WorldGenCinderpearl;
import com.pengu.lostthaumaturgy.worldgen.WorldGenCrystals;
import com.pengu.lostthaumaturgy.worldgen.WorldGenLostArtifacts;
import com.pengu.lostthaumaturgy.worldgen.WorldGenMonoliths;
import com.pengu.lostthaumaturgy.worldgen.WorldGenSilverwood;

@Mod(modid = LTInfo.MOD_ID, name = LTInfo.MOD_NAME, version = LTInfo.MOD_VERSION, dependencies = "required-after:hammercore", guiFactory = "com.pengu.lostthaumaturgy.client.cfg.GuiFactoryLT")
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
		ProgressBar bar = ProgressManager.push("Adding Contents...", 5);
		
		bar.step("Registering Vis Capability");
		CapabilityVisConnection.register();
		MinecraftForge.EVENT_BUS.register(proxy);
		proxy.preInit();
		
		bar.step("Registering Blocks");
		SimpleRegistration.registerFieldBlocksFrom(BlocksLT.class, LTInfo.MOD_ID, tab);
		
		bar.step("Registering Items");
		SimpleRegistration.registerFieldItemsFrom(ItemsLT.class, LTInfo.MOD_ID, tab);
		
		bar.step("Registering Researches");
		ResearchesLT.registerResearches();
		
		bar.step("Registering Tesseract API");
		TileTesseract.registerTesseractCapability(CapabilityVisConnection.VIS, LTInfo.MOD_ID + ":vis", EnumMultiMaterialType.VIS_CRYSTAL.stack());
		
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		ProgressBar bar = ProgressManager.push("Adding Recipes...", 3);
		
		bar.step("Registering Crafting");
		RecipesLT.registerRecipes();
		proxy.init();
		
		bar.step("Registering Infuser");
		InfuserLT.registerInfuser();
		
		bar.step("Registering Dark Infuser");
		InfuserLT.registerDarkInfuser();
		
		ProgressManager.pop(bar);
		bar = ProgressManager.push("Adding Contents...", 6);
		
		bar.step("Registering Entities");
		EntitiesLT.registerEntities();
		
		bar.step("Registering Crystal WorldGen");
		GameRegistry.registerWorldGenerator(new WorldGenCrystals(), 0);
		
		bar.step("Registering Silverwood WorldGen");
		GameRegistry.registerWorldGenerator(new WorldGenSilverwood(), 4);
		
		bar.step("Registering Cinderpearl WorldGen");
		GameRegistry.registerWorldGenerator(new WorldGenCinderpearl(), 6);
		
		bar.step("Registering Underground Artifacts WorldGen");
		WorldGenRegistry.registerFeature(new WorldGenLostArtifacts());
		
		bar.step("Registering Monolith WorldGen");
		WorldGenRegistry.registerFeature(new WorldGenMonoliths());
		
		ProgressManager.pop(bar);
		bar = ProgressManager.push("Sending IMC", 1);
		
		bar.step("Waila...");
		FMLInterModComms.sendMessage("waila", "register", "com.pengu.lostthaumaturgy.intr.waila.WailaLTProvider.register");
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		ProgressBar bar = ProgressManager.push("Registering mob spawns", 2);
		
		bar.step("Smart Zombie");
		makeSpawn(EntityZombie.class, EntitySmartZombie.class, 1, 1, 128);
		bar.step("Thaum Slime");
		makeSpawn(EntitySkeleton.class, EntityThaumSlime.class, 1, 1, 256);
		
		ProgressManager.pop(bar);
	}
	
	private void makeSpawn(Class<? extends EntityLiving> search, Class<? extends EntityLiving> add, int minGC, int maxGC, int weightMult)
	{
		Iterator<Biome> biomes = Biome.REGISTRY.iterator();
		while(biomes.hasNext())
		{
			Biome b = biomes.next();
			List<SpawnListEntry> spawns = b.getSpawnableList(EnumCreatureType.MONSTER);
			boolean contains = false;
			
			int weight = -1;
			for(SpawnListEntry sle : spawns)
				if(search.isAssignableFrom(sle.entityClass))
				{
					weight = sle.itemWeight;
					contains = true;
					break;
				}
			if(contains)
				spawns.add(new SpawnListEntry(add, weight * weightMult, minGC, maxGC));
		}
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent evt)
	{
		RecipesCrucible.reloadRecipes();
		AuraTicker.LoadAuraData();
	}
	
	@EventHandler
	public void serverStop(FMLServerStoppedEvent evt)
	{
		AuraTicker.AuraHM.clear();
	}
}