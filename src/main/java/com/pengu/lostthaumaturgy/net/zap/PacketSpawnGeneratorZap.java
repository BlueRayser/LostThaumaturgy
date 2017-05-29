package com.pengu.lostthaumaturgy.net.zap;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.pengu.hammercore.client.particle.def.ParticleZap;

public class PacketSpawnGeneratorZap implements IPacket, IPacketListener<PacketSpawnGeneratorZap, IPacket>
{
	public Vec3d start, end;
	
	public PacketSpawnGeneratorZap(Vec3d start, Vec3d end)
	{
		this.start = start;
		this.end = end;
	}
	
	public PacketSpawnGeneratorZap()
	{
	}
	
	@Override
	public IPacket onArrived(PacketSpawnGeneratorZap packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.spawn();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void spawn()
	{
		ParticleZap zap = (ParticleZap) HammerCore.particleProxy.spawnZap(Minecraft.getMinecraft().world, start, end, Color.CYAN);
		zap.setMaxAge(9);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("sx", start.xCoord);
		nbt.setDouble("sy", start.yCoord);
		nbt.setDouble("sz", start.zCoord);
		nbt.setDouble("ex", end.xCoord);
		nbt.setDouble("ey", end.yCoord);
		nbt.setDouble("ez", end.zCoord);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		start = new Vec3d(nbt.getDouble("sx"), nbt.getDouble("sy"), nbt.getDouble("sz"));
		end = new Vec3d(nbt.getDouble("ex"), nbt.getDouble("ey"), nbt.getDouble("ez"));
	}
}