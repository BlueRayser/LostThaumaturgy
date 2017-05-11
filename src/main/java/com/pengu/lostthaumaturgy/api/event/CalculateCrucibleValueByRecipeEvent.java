package com.pengu.lostthaumaturgy.api.event;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class CalculateCrucibleValueByRecipeEvent extends Event
{
	private final ItemStack output;
	public final IRecipe recipe;
	private float calculatedValue;
	
	public CalculateCrucibleValueByRecipeEvent(ItemStack output, float calculatedValue, IRecipe recipe)
    {
		this.output = output;
		this.recipe = recipe;
		this.calculatedValue = calculatedValue;
    }
	
	public ItemStack getOutput()
    {
	    return output.copy();
    }
	
	public float getCalculatedValue()
    {
	    return calculatedValue;
    }
	
	public void setCalculatedValue(float calculatedValue)
    {
	    this.calculatedValue = calculatedValue;
    }
	
	public IRecipe getRecipe()
    {
	    return recipe;
    }
}