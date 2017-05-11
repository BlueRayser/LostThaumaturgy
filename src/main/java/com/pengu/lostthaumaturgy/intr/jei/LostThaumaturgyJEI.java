package com.pengu.lostthaumaturgy.intr.jei;

import java.util.Arrays;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

import com.pengu.lostthaumaturgy.LTInfo.JEIConstans;
import com.pengu.lostthaumaturgy.api.RecipesInfuser;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.DarkInfuserRecipe;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.InfuserRecipe;
import com.pengu.lostthaumaturgy.client.gui.GuiInfuser;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.intr.jei.infuser.InfuserRecipeCategory;
import com.pengu.lostthaumaturgy.intr.jei.infuser.InfuserRecipeHandler;

@JEIPlugin
public class LostThaumaturgyJEI implements IModPlugin
{
	@Override
	public void onRuntimeAvailable(IJeiRuntime arg0)
	{
	}
	
	@Override
	public void register(IModRegistry reg)
	{
		reg.addRecipeCategories(new InfuserRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
		reg.addRecipeHandlers(new InfuserRecipeHandler());
		
		reg.addRecipeCategoryCraftingItem(new ItemStack(BlocksLT.INFUSER), JEIConstans.INFUSER);
		reg.addRecipeClickArea(GuiInfuser.class, 160, 105, 9, 47, JEIConstans.INFUSER);
		
		//register infuser recipes
		InfuserRecipe[] normal = RecipesInfuser.InfuserRecipe.present();
		if(normal.length > 0) reg.addRecipes(Arrays.asList(normal));
		
		DarkInfuserRecipe[] dark = RecipesInfuser.DarkInfuserRecipe.present();
		if(dark.length > 0) reg.addRecipes(Arrays.asList(dark));
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration arg0)
	{
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry arg0)
	{
	}
}