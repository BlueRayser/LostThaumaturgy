package com.pengu.lostthaumaturgy.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;

public class PacketUpdateClientAura implements IPacket, IPacketListener<PacketUpdateClientAura, IPacket>
{
	public static BlockPos closestMonolith;
	
	public AtmosphereChunk chunk;
	public BlockPos monolith;
	
	public PacketUpdateClientAura(AtmosphereChunk chunk, EntityPlayerMP mp)
	{
		this.chunk = chunk;
		monolith = AuraTicker.getClosestMonolithPos(mp.getPosition());
	}
	
	public PacketUpdateClientAura()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(chunk != null)
			nbt.setTag("Data", chunk.serializeNBT());
		if(monolith != null)
			nbt.setLong("p", monolith.toLong());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		chunk = new AtmosphereChunk();
		chunk.deserializeNBT(nbt.getCompoundTag("Data"));
		if(nbt.hasKey("p", NBT.TAG_LONG))
			monolith = BlockPos.fromLong(nbt.getLong("p"));
	}
	
	@Override
	public IPacket onArrived(PacketUpdateClientAura packet, MessageContext context)
	{
		LostThaumaturgy.proxy.updateClientAuraChunk(packet.chunk);
		closestMonolith = packet.monolith;
		return null;
	}
}