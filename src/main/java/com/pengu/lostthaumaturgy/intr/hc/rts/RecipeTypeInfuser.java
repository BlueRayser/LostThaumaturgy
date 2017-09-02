package com.pengu.lostthaumaturgy.intr.hc.rts;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

import com.google.common.base.Predicates;
import com.pengu.hammercore.recipeAPI.IRecipeType;
import com.pengu.lostthaumaturgy.api.RecipesInfuser;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.InfuserList;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.RecipeInfuserDummy;
import com.pengu.lostthaumaturgy.api.tiles.IInfuser;
import com.pengu.lostthaumaturgy.core.Info;

public class RecipeTypeInfuser implements IRecipeType<RecipeInfuserDummy>
{
	
	@Override
	public boolean isJeiSupported(RecipeInfuserDummy recipe)
	{
		return true;
	}
	
	@Override
	public String getTypeId()
	{
		return Info.MOD_ID + ":infuser";
	}
	
	@Override
	public RecipeInfuserDummy createRecipe(NBTTagCompound json) throws RecipeParseException
	{
		boolean dark = json.getBoolean("dark");
		ItemStack result = loadStack(json.getCompoundTag("result"));
		ItemStack[] inputs = loadStacks(json.getTag("input"), "input").toArray(new ItemStack[0]);
		int cost = Math.max(json.getInteger("cost"), 1);
		
		NBTTagList conds = json.getTagList("conditions", NBT.TAG_COMPOUND);
		Predicate<IInfuser> predicate = Predicates.alwaysTrue();
		
		if(conds != null && !conds.hasNoTags())
			for(int i = 0; i < conds.tagCount(); ++i)
			{
				NBTTagCompound nbt = conds.getCompoundTagAt(i);
				if(nbt.getString("type").equals("research"))
					predicate = predicate.and(RecipesInfuser.createPredicateFromResearches(nbt.getString("value")));
				else
					throw new RecipeParseException("Unknown condition type \'" + nbt.getString("type") + "\'");
			}
		
		return new RecipeInfuserDummy(result, inputs, cost, dark, predicate);
	}
	
	@Override
	public void addRecipe(RecipeInfuserDummy recipe)
	{
		InfuserList list = RecipesInfuser.listRecipes();
		list.add(recipe);
		list.close();
	}
	
	@Override
	public void removeRecipe(RecipeInfuserDummy recipe)
	{
		InfuserList list = RecipesInfuser.listRecipes();
		list.remove(recipe);
		list.close();
	}
	
	@Override
	public Object getJeiRecipeFor(RecipeInfuserDummy recipe)
	{
		return recipe.isDark() ? new RecipesInfuser.DarkInfuserRecipe(recipe) : new RecipesInfuser.InfuserRecipe(recipe);
	}
}