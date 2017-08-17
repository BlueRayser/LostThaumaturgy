package com.pengu.lostthaumaturgy.inventory.slot;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.fuser.FuserInventory;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;
import com.pengu.lostthaumaturgy.items.ItemWand;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;

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
		
		WorldLocation loc = inv.location;
		Random rand = loc.getWorld().rand;
		
		for(int i = 0; i < 64; ++i)
		{
			double x1 = loc.getPos().getX() + .4 + rand.nextFloat() * 1.2;
			double y1 = loc.getPos().getY() + 1;
			double z1 = loc.getPos().getZ() + .3 + rand.nextFloat() * 1.2;
			
			double x2 = loc.getPos().getX() + .4 + rand.nextFloat() * 1.2;
			double y2 = loc.getPos().getY() + 1 + rand.nextFloat() * 2;
			double z2 = loc.getPos().getZ() + .3 + rand.nextFloat() * 1.4;
			
			HCNetwork.manager.sendToAllAround(new PacketFXWisp2(x1, y1, z1, x2, y2, z2, .4F, rand.nextInt(5)), loc.getPointWithRad(48));
		}
		
		return stack;
	}
}