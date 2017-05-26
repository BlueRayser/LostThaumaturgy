package com.pengu.lostthaumaturgy.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

public class PacketUpdateClientAura implements IPacket, IPacketListener<PacketUpdateClientAura, IPacket>
{
	public SIAuraChunk chunk;
	
	public PacketUpdateClientAura(SIAuraChunk chunk)
	{
		this.chunk = chunk;
	}
	
	public PacketUpdateClientAura()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(chunk != null)
			nbt.setTag("Data", chunk.serializeNBT());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		chunk = new SIAuraChunk();
		chunk.deserializeNBT(nbt.getCompoundTag("Data"));
	}
	
	@Override
	public IPacket onArrived(PacketUpdateClientAura packet, MessageContext context)
	{
		LostThaumaturgy.proxy.updateClientAuraChunk(chunk);
		return null;
	}
}