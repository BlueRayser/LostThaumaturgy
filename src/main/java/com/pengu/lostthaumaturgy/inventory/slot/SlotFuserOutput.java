package com.pengu.lostthaumaturgy.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.pengu.lostthaumaturgy.api.fuser.FuserInventory;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;
import com.pengu.lostthaumaturgy.items.ItemWand;

public class SlotFuserOutput extends SlotOutput
{
	public SlotFuserOutput(FuserInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player)
	{
		FuserInventory inv = (FuserInventory) inventory;
		IFuserRecipe recipe = inv.findRecipe(player);
		if(recipe != null)
		{
			ItemStack wand = inv.wandInv.getStackInSlot(0);
			float cost = !wand.isEmpty() && wand.getItem() instanceof ItemWand ? ItemWand.getVisUsage(wand) : 12000F;
			float vis = !wand.isEmpty() && wand.getItem() instanceof ItemWand ? ItemWand.getVis(wand) : 0;
			float taint = !wand.isEmpty() && wand.getItem() instanceof ItemWand ? ItemWand.getTaint(wand) : 0;
			if(vis >= recipe.getVisUsage() * cost && taint >= recipe.getTaintUsage() * cost)
				return true;
		}
		return false;
	}
	
	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack)
	{
		FuserInventory inv = (FuserInventory) inventory;
		IFuserRecipe recipe = inv.findRecipe(player);
		for(int i = 0; i < inv.craftingInv.getSizeInventory(); ++i)
			inv.craftingInv.getStackInSlot(i).shrink(1);
		NonNullList<ItemStack> remainings = recipe.getRemainingItems(inv, player);
		for(int i = 0; i < inv.craftingInv.getSizeInventory(); ++i)
			if(inv.craftingInv.getStackInSlot(i).isEmpty())
				inv.craftingInv.setInventorySlotContents(i, remainings.get(i));
		recipe.consumeWandVis(inv, player, inv.wandInv.getStackInSlot(0));
		return stack;
	}
}