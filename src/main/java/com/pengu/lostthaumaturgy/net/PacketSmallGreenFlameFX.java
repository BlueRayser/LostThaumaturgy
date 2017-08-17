package com.pengu.lostthaumaturgy.net;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.lostthaumaturgy.client.fx.FXGreenFlame;

public class PacketSmallGreenFlameFX implements IPacket, IPacketListener<PacketSmallGreenFlameFX, IPacket>
{
	public Vec3d pos;
	
	public PacketSmallGreenFlameFX(Vec3d pos)
	{
		this.pos = pos;
	}
	
	public PacketSmallGreenFlameFX()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("x", pos.x);
		nbt.setDouble("y", pos.y);
		nbt.setDouble("z", pos.z);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		pos = new Vec3d(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
	}
	
	@Override
	public IPacket onArrived(PacketSmallGreenFlameFX packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		FXGreenFlame p;
		ParticleProxy_Client.queueParticleSpawn(p = new FXGreenFlame(Minecraft.getMinecraft().world, pos.x, pos.y, pos.z, 0, 0, 0));
		p.setScale(.05F);
	}
}