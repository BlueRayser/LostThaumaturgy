package com.pengu.lostthaumaturgy.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerDataEvent extends PlayerEvent
{
	public final NBTTagCompound nbt;
	
	public PlayerDataEvent(EntityPlayer player, NBTTagCompound nbt)
	{
		super(player);
		this.nbt = nbt;
	}
	
	public static class Read extends PlayerDataEvent
	{
		public Read(EntityPlayer player, NBTTagCompound nbt)
		{
			super(player, nbt);
		}
	}
	
	public static class Write extends PlayerDataEvent
	{
		public Write(EntityPlayer player, NBTTagCompound nbt)
		{
			super(player, nbt);
		}
	}
}