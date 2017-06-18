package com.pengu.lostthaumaturgy.api.fuser;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IFuserRecipe
{
	/**
	 * Used to check if this pattern matches. Also checks for wand and vis
	 * and/or taint in it.
	 **/
	boolean matches(FuserInventory inv, EntityPlayer player);
	
	@Nonnull
	ItemStack getCraftResult(FuserInventory inv, EntityPlayer player);
	
	@Nonnull
	default NonNullList<ItemStack> getRemainingItems(FuserInventory inv, EntityPlayer player)
	{
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.craftingInv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0; i < ret.size(); i++)
			ret.set(i, ForgeHooks.getContainerItem(inv.craftingInv.getStackInSlot(i)));
		return ret;
	}
	
	/**
	 * Used to consume vis and/or taint.
	 **/
	void consumeWandVis(FuserInventory inv, EntityPlayer player, ItemStack wandStack);
	
	@Nonnull
	ItemStack getOutput();
	
	@Nonnull
	@SideOnly(Side.CLIENT)
	default ItemStack getJEIOutput()
	{
		return getOutput();
	}
	
	/** Returns a 9-sized NonNullList with lists that contain all possible items */
	NonNullList<List<ItemStack>> bakeInputItems();
	
	float getVisUsage();
	
	float getTaintUsage();
}