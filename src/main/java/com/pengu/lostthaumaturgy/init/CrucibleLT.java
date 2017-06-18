package com.pengu.lostthaumaturgy.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import com.mrdimka.hammercore.annotations.MCFBus;
import com.mrdimka.hammercore.common.utils.IOUtils;
import com.mrdimka.hammercore.json.JSONObject;
import com.mrdimka.hammercore.json.JSONTokener;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.api.event.ReloadRegisteredCrucibleRecipesEvent;
import com.pengu.lostthaumaturgy.api.match.MatcherItemStack;
import com.pengu.lostthaumaturgy.api.match.MatcherOreDict;

@MCFBus
public class CrucibleLT
{
	@SubscribeEvent
	public void reload(ReloadRegisteredCrucibleRecipesEvent evt)
	{
		addCrucibleSmeltings();
	}
	
	public static String json = "";
	
	public static void addCrucibleSmeltings()
	{
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.STICK), .25F);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.CLAY_BALL), 1);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.BRICK), 2);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.FLINT), 1);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.COAL), 2);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.SNOWBALL), .25F);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.IRON_INGOT), 5);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.BEETROOT), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.CARROT), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.POTATO), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.BAKED_POTATO), 5);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.BEETROOT_SEEDS), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.WHEAT_SEEDS), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.FEATHER), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.BONE), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.MELON), 2);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.COOKED_BEEF), 5);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.BEEF), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.COOKED_CHICKEN), 5);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.CHICKEN), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.COOKED_PORKCHOP), 5);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.MUTTON), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.COOKED_MUTTON), 5);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.PORKCHOP), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.COOKED_FISH), 5);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.FISH), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.NETHER_WART), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.ROTTEN_FLESH), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.STRING), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.LEATHER), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.WHEAT), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.REEDS), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.SUGAR), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.GUNPOWDER), 10);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.GLOWSTONE_DUST), 9);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.REDSTONE), 6);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.MILK_BUCKET), 18);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.WATER_BUCKET), 16);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.LAVA_BUCKET), 17);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.EGG), 5);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.GOLD_INGOT), 27);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.GOLD_NUGGET), 3);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.SLIME_BALL), 25);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.APPLE), 4);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.DIAMOND), 64);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.EMERALD), 128);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.ENDER_PEARL), 32);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_13), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_CAT), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_11), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_CAT), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_CHIRP), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_FAR), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_MALL), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_MELLOHI), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_STAL), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_STRAD), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.RECORD_WARD), 100);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.BLAZE_ROD), 36);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.BLAZE_POWDER), 18);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.GHAST_TEAR), 64);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.SPIDER_EYE), 9);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.SADDLE), 64);
		RecipesCrucible.registerNewSmelting(new ItemStack(Items.FERMENTED_SPIDER_EYE), 17);
		
		try
		{
			LostThaumaturgy.LOG.info("Loading Crucible Recipe Jsons...");
			long start = System.currentTimeMillis();
			
			String load = json != null && !json.isEmpty() ? json : new String(IOUtils.pipeOut(LostThaumaturgy.class.getResourceAsStream("/assets/" + LTInfo.MOD_ID + "/crucible.json")));
			JSONObject obj = (JSONObject) new JSONTokener(load).nextValue();
			
			for(String key : obj.keySet())
			{
				try
				{
					if(key.contains(":"))
					{
						String item = key.substring(0, key.lastIndexOf(":"));
						int meta = Integer.parseInt(key.substring(key.lastIndexOf(":") + 1));
						Item i = Item.REGISTRY.getObject(new ResourceLocation(item));
						if(meta < 0)
							meta = OreDictionary.WILDCARD_VALUE;
						
						RecipesCrucible.registerNewSmelting(new MatcherItemStack(new ItemStack(i, 1, meta)), (float) obj.getDouble(key));
					} else
						RecipesCrucible.registerNewSmelting(new MatcherOreDict(key), (float) obj.getDouble(key));
				} catch(Throwable er)
				{
					er.printStackTrace();
				}
			}
			
			LostThaumaturgy.LOG.info("Default Crucible Recipes Loaded! Took " + (System.currentTimeMillis() - start) + " ms.");
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		
		json = null;
	}
}