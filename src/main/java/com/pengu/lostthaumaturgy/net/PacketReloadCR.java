package com.pengu.lostthaumaturgy.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;

public class PacketReloadCR implements IPacket, IPacketListener<PacketReloadCR, IPacket>
{
	@Override
	public IPacket onArrived(PacketReloadCR packet, MessageContext context)
	{
		LostThaumaturgy.LOG.info("Reloading crucible recipes, from server...");
		RecipesCrucible.reloadRecipes();
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
	}
}