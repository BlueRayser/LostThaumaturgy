package com.pengu.lostthaumaturgy.intr.hc.rts;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import com.pengu.hammercore.recipeAPI.IRecipeType;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.api.match.MatcherItemStack;
import com.pengu.lostthaumaturgy.api.match.MatcherOreDict;
import com.pengu.lostthaumaturgy.intr.hc.rts.RecipeTypeCrucible.CrucibleRecipe;

public class RecipeTypeCrucible implements IRecipeType<CrucibleRecipe>
{
	@Override
	public void addRecipe(CrucibleRecipe recipe)
	{
		try
		{
			String key = recipe.object;
			
			if(key.contains(":"))
			{
				String item = key.substring(0, key.lastIndexOf(":"));
				int meta = Integer.parseInt(key.substring(key.lastIndexOf(":") + 1));
				Item i = Item.REGISTRY.getObject(new ResourceLocation(item));
				if(meta < 0)
					meta = OreDictionary.WILDCARD_VALUE;
				
				RecipesCrucible.registerNewSmelting(new MatcherItemStack(new ItemStack(i, 1, meta)), recipe.value);
			} else
				RecipesCrucible.registerNewSmelting(new MatcherOreDict(key), recipe.value);
		} catch(Throwable er)
		{
			er.printStackTrace();
		}
	}
	
	@Override
	public CrucibleRecipe createRecipe(NBTTagCompound json) throws RecipeParseException
	{
		CrucibleRecipe r = new CrucibleRecipe();
		r.object = json.getString("Item");
		NBTBase val = json.getTag("Cost");
		r.value = val instanceof NBTTagFloat ? ((NBTTagFloat) val).getFloat() : val instanceof NBTTagDouble ? ((NBTTagDouble) val).getFloat() : 0;
		return r;
	}
	
	@Override
	public String getTypeId()
	{
		return LTInfo.MOD_ID + ":crucible";
	}
	
	@Override
	public boolean isJeiSupported(CrucibleRecipe recipe)
	{
		return false;
	}
	
	@Override
	public void removeRecipe(CrucibleRecipe recipe)
	{
	}
	
	public static class CrucibleRecipe
	{
		public String object;
		public float value;
	}
}