package com.pengu.lostthaumaturgy.api.fuser.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.pengu.lostthaumaturgy.api.fuser.FuserInventory;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;
import com.pengu.lostthaumaturgy.items.ItemWand;

public class TheoryToDiscovery implements IFuserRecipe
{
	@Override
	public boolean matches(FuserInventory inv, EntityPlayer player)
	{
		if(player.experienceLevel < 2)
			return false;
		
		ItemStack theory = null;
		int found = 0;
		
		for(int i = 0; i < inv.craftingInv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.craftingInv.getStackInSlot(i);
			if(stack.getItem() instanceof ItemResearch)
			{
				if(ItemResearch.getType(stack) != EnumResearchItemType.THEORY)
					return false;
				if(theory == null)
					theory = stack.copy();
				else if(ItemResearch.getFromStack(theory) != ItemResearch.getFromStack(stack))
					return false;
				found++;
			}
		}
		
		return theory != null && found == 1;
	}
	
	@Override
	public ItemStack getCraftResult(FuserInventory inv, EntityPlayer player)
	{
		if(player.experienceLevel < 2)
			return ItemStack.EMPTY;
		
		ItemStack theory = null;
		int found = 0;
		
		for(int i = 0; i < inv.craftingInv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.craftingInv.getStackInSlot(i);
			if(stack.getItem() instanceof ItemResearch)
			{
				if(ItemResearch.getType(stack) != EnumResearchItemType.THEORY)
					return ItemStack.EMPTY;
				if(theory == null)
					theory = stack.copy();
				else if(ItemResearch.getFromStack(theory) != ItemResearch.getFromStack(stack))
					return ItemStack.EMPTY;
				found++;
			}
		}
		
		if(theory != null && found == 1)
			return ItemResearch.create(ItemResearch.getFromStack(theory), EnumResearchItemType.DISCOVERY);
		return ItemStack.EMPTY;
	}
	
	@Override
	public void consumeWandVis(FuserInventory inv, EntityPlayer player, ItemStack wandStack)
	{
		if(wandStack.isEmpty() || !(wandStack.getItem() instanceof ItemWand))
			return;
		float eff = ItemWand.getVisUsage(wandStack);
		if(ItemWand.getVis(wandStack) < 5 * eff)
			return;
		if(ItemWand.getTaint(wandStack) < 5 * eff)
			return;
		ItemWand.removeVis(wandStack, 5 * eff);
		ItemWand.removeTaint(wandStack, 5 * eff);
		player.experienceLevel -= 2;
	}
	
	@Override
	public ItemStack getOutput()
	{
		return ItemResearch.create(null, EnumResearchItemType.DISCOVERY);
	}
	
	@Override
	public ItemStack getJEIOutput()
	{
		return ItemResearch.create(null, EnumResearchItemType.DISCOVERY);
	}
	
	@Override
	public NonNullList<List<ItemStack>> bakeInputItems()
	{
		NonNullList<List<ItemStack>> stacks = NonNullList.<List<ItemStack>> withSize(9, new ArrayList<>());
		List<ItemStack> theory = new ArrayList<>();
		theory.add(ItemResearch.create(null, EnumResearchItemType.THEORY));
		stacks.set(0, theory);
		return stacks;
	}
	
	@Override
	public float getVisUsage()
	{
		return 5;
	}
	
	@Override
	public float getTaintUsage()
	{
		return 2.5F;
	}
}