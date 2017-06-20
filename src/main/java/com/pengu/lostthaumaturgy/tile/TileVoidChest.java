package com.pengu.lostthaumaturgy.tile;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;

import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.lostthaumaturgy.client.gui.GuiVoidChest;
import com.pengu.lostthaumaturgy.inventory.ContainerVoidChest;

public class TileVoidChest extends TileSyncable implements IInventory
{
	public final InventoryNonTile inventory = new InventoryNonTile(72);
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tag = new NBTTagCompound();
		inventory.writeToNBT(tag);
		nbt.setTag("Items", tag);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("empty", isEmpty());
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiVoidChest(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerVoidChest(this, player);
	}
	
	@Override
	public String getName()
	{
		return "Void Chest";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return true;
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}
	
	@Override
	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return inventory.decrStackSize(index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return inventory.removeStackFromSlot(index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.setInventorySlotContents(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return inventory.isUsableByPlayer(player, pos);
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
		return inventory.isItemValidForSlot(index, stack);
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
		inventory.clear();
	}
}