package com.pengu.lostthaumaturgy.net.wisp;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.mrdimka.hammercore.proxy.ParticleProxy_Client;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;

public class PacketFXWisp_EntitySingularity_doSuckage implements IPacket, IPacketListener<PacketFXWisp_EntitySingularity_doSuckage, IPacket>
{
	double x, y, z;
	float partialTicks;
	int type;
	
	public PacketFXWisp_EntitySingularity_doSuckage(double x, double y, double z, float partialTicks, int type)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.partialTicks = partialTicks;
		this.type = type;
	}
	
	public PacketFXWisp_EntitySingularity_doSuckage()
	{
	}
	
	@Override
	public IPacket onArrived(PacketFXWisp_EntitySingularity_doSuckage packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			summon();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private void summon()
	{
		FXWisp wisp;
		ParticleProxy_Client.queueParticleSpawn(wisp = new FXWisp(Minecraft.getMinecraft().world, x, y, z, partialTicks, type));
		wisp.shrink = true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
		nbt.setFloat("p", partialTicks);
		nbt.setInteger("type", type);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		x = nbt.getDouble("x");
		y = nbt.getDouble("y");
		z = nbt.getDouble("z");
		partialTicks = nbt.getFloat("p");
		type = nbt.getInteger("type");
	}
}