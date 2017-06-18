package com.pengu.lostthaumaturgy.recipe;

import java.util.List;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import com.google.common.collect.Lists;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class RecipePaintSeal implements IRecipe
{
	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		List<ItemStack> list = Lists.<ItemStack> newArrayList();
		
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack itemstack1 = inv.getStackInSlot(i);
			
			if(!itemstack1.isEmpty())
			{
				if(itemstack1.getItem() == Item.getItemFromBlock(BlocksLT.SEAL))
					itemstack = itemstack1;
				else
				{
					if(itemstack1.getItem() != Items.DYE)
						return false;
					list.add(itemstack1);
				}
			}
		}
		
		return !itemstack.isEmpty() && !list.isEmpty();
	}
	
	/**
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		int[] aint = new int[3];
		int i = 0;
		int j = 0;
		Item itemseal = null;
		
		for(int k = 0; k < inv.getSizeInventory(); ++k)
		{
			ItemStack itemstack1 = inv.getStackInSlot(k);
			
			if(!itemstack1.isEmpty())
			{
				if(itemstack1.getItem() == Item.getItemFromBlock(BlocksLT.SEAL))
				{
					itemseal = Item.getItemFromBlock(BlocksLT.SEAL);
					itemstack = itemstack1.copy();
					itemstack.setCount(1);
					
					if(itemstack1.hasTagCompound() && itemstack1.getTagCompound().hasKey("RGB", NBT.TAG_INT_ARRAY))
					{
						int[] rgb = itemstack1.getTagCompound().getIntArray("RGB");
						int l = (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2] << 0);
						float f = (float) (l >> 16 & 255) / 255.0F;
						float f1 = (float) (l >> 8 & 255) / 255.0F;
						float f2 = (float) (l & 255) / 255.0F;
						i = (int) ((float) i + Math.max(f, Math.max(f1, f2)) * 255.0F);
						aint[0] = (int) ((float) aint[0] + f * 255.0F);
						aint[1] = (int) ((float) aint[1] + f1 * 255.0F);
						aint[2] = (int) ((float) aint[2] + f2 * 255.0F);
						++j;
					}
				} else
				{
					if(itemstack1.getItem() != Items.DYE)
					{
						return ItemStack.EMPTY;
					}
					
					float[] afloat = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(itemstack1.getMetadata()));
					int l1 = (int) (afloat[0] * 255.0F);
					int i2 = (int) (afloat[1] * 255.0F);
					int j2 = (int) (afloat[2] * 255.0F);
					i += Math.max(l1, Math.max(i2, j2));
					aint[0] += l1;
					aint[1] += i2;
					aint[2] += j2;
					++j;
				}
			}
		}
		
		if(itemseal == null)
		{
			return ItemStack.EMPTY;
		} else
		{
			int i1 = aint[0] / j;
			int j1 = aint[1] / j;
			int k1 = aint[2] / j;
			float f3 = (float) i / (float) j;
			float f4 = (float) Math.max(i1, Math.max(j1, k1));
			i1 = (int) ((float) i1 * f3 / f4);
			j1 = (int) ((float) j1 * f3 / f4);
			k1 = (int) ((float) k1 * f3 / f4);
			
			int col = (i1 << 8) + j1;
			col = (col << 8) + k1;
			
			NBTTagCompound nbt = itemstack.getTagCompound();
			if(nbt == null)
				nbt = new NBTTagCompound();
			nbt.setIntArray("RGB", new int[] { (col >> 16) & 0xFF, (col >> 8) & 0xFF, (col >> 0) & 0xFF });
			itemstack.setTagCompound(nbt);
			
			return itemstack;
		}
	}
	
	/**
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize()
	{
		return 10;
	}
	
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}
	
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack> withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		
		for(int i = 0; i < nonnulllist.size(); ++i)
		{
			ItemStack itemstack = inv.getStackInSlot(i);
			nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
		}
		
		return nonnulllist;
	}
}