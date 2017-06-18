package com.pengu.lostthaumaturgy.api.fuser;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.items.ItemWand;

public class FuserInventory implements IInventory
{
	public WorldLocation location;
	public final InventoryNonTile craftingInv = new InventoryNonTile(9);
	public final InventoryNonTile wandInv = new InventoryNonTile(1);
	public final InventoryNonTile outputInv = new InventoryNonTile(1);
	
	public IFuserRecipe findRecipe(EntityPlayer player)
	{
		Iterator<IFuserRecipe> it = RecipesFuser.getInstance().getRecipes().stream().filter(t -> t.matches(this, player)).iterator();
		if(it.hasNext())
			return it.next();
		return null;
	}
	
	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		return row >= 0 && row < 3 && column >= 0 && column <= 3 ? craftingInv.getStackInSlot(row + column * 3) : ItemStack.EMPTY;
	}
	
	@Override
	public String getName()
	{
		return "Fuser";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(getName());
	}
	
	@Override
	public int getSizeInventory()
	{
		return 11;
	}
	
	@Override
	public boolean isEmpty()
	{
		return craftingInv.isEmpty() && wandInv.isEmpty() && outputInv.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		if(index == 9)
			return wandInv.getStackInSlot(0);
		if(index == 10)
			return outputInv.getStackInSlot(0);
		return craftingInv.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if(index == 9)
			return wandInv.decrStackSize(0, count);
		if(index == 10)
			return outputInv.decrStackSize(0, count);
		return craftingInv.decrStackSize(index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if(index == 9)
			return wandInv.removeStackFromSlot(0);
		if(index == 10)
			return outputInv.removeStackFromSlot(0);
		return craftingInv.removeStackFromSlot(index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if(index == 9)
			wandInv.setInventorySlotContents(0, stack);
		if(index == 10)
			outputInv.setInventorySlotContents(0, stack);
		else
			craftingInv.setInventorySlotContents(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public void markDirty()
	{
		if(location != null)
			location.markDirty();
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return location != null && craftingInv.isUsableByPlayer(player, location.getPos());
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
		
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
		
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if(index == 9)
			return stack.getItem() instanceof ItemWand;
		return index != 10;
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		craftingInv.clear();
		outputInv.clear();
		wandInv.clear();
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tag = new NBTTagCompound();
		craftingInv.writeToNBT(tag);
		nbt.setTag("ItemsCrafting", tag);
		
		tag = new NBTTagCompound();
		wandInv.writeToNBT(tag);
		nbt.setTag("ItemsWand", tag);
		
		tag = new NBTTagCompound();
		outputInv.writeToNBT(tag);
		nbt.setTag("ItemsOutput", tag);
		
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		craftingInv.readFromNBT(nbt.getCompoundTag("ItemsCrafting"));
		wandInv.readFromNBT(nbt.getCompoundTag("ItemsWand"));
		outputInv.readFromNBT(nbt.getCompoundTag("ItemsOutput"));
	}
	
	public void drop(World world, BlockPos pos)
	{
		craftingInv.drop(world, pos);
		wandInv.drop(world, pos);
	}
}