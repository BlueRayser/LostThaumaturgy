package com.pengu.lostthaumaturgy.net;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.api.research.ClientResearchData;

public class PacketUpdateClientRD implements IPacket, IPacketListener<PacketUpdateClientRD, IPacket>
{
	public NBTTagCompound nbt;
	
	public PacketUpdateClientRD(ArrayList<String> researches)
	{
		nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(String r : researches)
			list.appendTag(new NBTTagString(r));
		nbt.setTag("Research", list);
	}
	
	public PacketUpdateClientRD()
	{
	}
	
	@Override
	public IPacket onArrived(PacketUpdateClientRD packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			ClientResearchData.fromNBT(packet.nbt);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Data", this.nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getCompoundTag("Data");
	}
}