package com.pengu.lostthaumaturgy.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.emote.EmoteData;

public class PacketEmote implements IPacket, IPacketListener<PacketEmote, IPacket>
{
	protected EmoteData data;
	
	public PacketEmote(EmoteData data)
	{
		this.data = data;
	}
	
	public PacketEmote()
	{
	}
	
	@Override
	public IPacket onArrived(PacketEmote packet, MessageContext context)
	{
		LostThaumaturgy.proxy.spawnEmote(packet.data, context);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		data.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		data = EmoteData.readFromNBT(nbt);
	}
}