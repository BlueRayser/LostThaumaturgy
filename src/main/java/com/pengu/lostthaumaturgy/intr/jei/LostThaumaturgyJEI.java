package com.pengu.lostthaumaturgy.intr.jei;

import java.util.Arrays;
import java.util.Random;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

import com.pengu.hammercore.utils.ListUtils;
import com.pengu.lostthaumaturgy.LTInfo.JEIConstans;
import com.pengu.lostthaumaturgy.api.RecipesInfuser;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.DarkInfuserRecipe;
import com.pengu.lostthaumaturgy.api.RecipesInfuser.InfuserRecipe;
import com.pengu.lostthaumaturgy.api.fuser.RecipesFuser;
import com.pengu.lostthaumaturgy.client.gui.GuiFuser;
import com.pengu.lostthaumaturgy.client.gui.GuiInfuser;
import com.pengu.lostthaumaturgy.client.gui.GuiInfuserDark;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.intr.jei.darkinfuser.DarkInfuserRecipeCategory;
import com.pengu.lostthaumaturgy.intr.jei.darkinfuser.DarkInfuserRecipeHandler;
import com.pengu.lostthaumaturgy.intr.jei.fuser.FuserRecipeCategory;
import com.pengu.lostthaumaturgy.intr.jei.fuser.FuserRecipeHandler;
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
		IGuiHelper gh = reg.getJeiHelpers().getGuiHelper();
		reg.addRecipeCategories(new InfuserRecipeCategory(gh), new DarkInfuserRecipeCategory(gh), new FuserRecipeCategory(gh));
		reg.addRecipeHandlers(new InfuserRecipeHandler(), new DarkInfuserRecipeHandler(), new FuserRecipeHandler());
		
		reg.addRecipeCategoryCraftingItem(new ItemStack(BlocksLT.INFUSER), JEIConstans.INFUSER);
		reg.addRecipeCategoryCraftingItem(new ItemStack(BlocksLT.INFUSER_DARK), JEIConstans.DARK_INFUSER);
		reg.addRecipeCategoryCraftingItem(new ItemStack(BlocksLT.FUSER_MB), JEIConstans.FUSER);
		
		reg.addRecipeClickArea(GuiInfuser.class, 160, 105, 9, 47, JEIConstans.INFUSER);
		reg.addRecipeClickArea(GuiInfuserDark.class, 158, 105, 12, 47, JEIConstans.DARK_INFUSER);
		reg.addRecipeClickArea(GuiFuser.class, 112, 28, 10, 36, JEIConstans.FUSER);
		
		// register infuser recipes
		InfuserRecipe[] normal = RecipesInfuser.InfuserRecipe.present();
		if(normal.length > 0)
			reg.addRecipes(Arrays.asList(normal));
		
		DarkInfuserRecipe[] dark = RecipesInfuser.DarkInfuserRecipe.present();
		if(dark.length > 0)
			reg.addRecipes(Arrays.asList(dark));
		
		reg.addRecipes(ListUtils.randomizeList(RecipesFuser.getInstance().getRecipes(), new Random(4L)));
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