package com.pengu.lostthaumaturgy.net.wisp;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.mrdimka.hammercore.proxy.ParticleProxy_Client;
import com.pengu.lostthaumaturgy.client.fx.FXBubble;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;

public class PacketFXBubble implements IPacket, IPacketListener<PacketFXBubble, IPacket>
{
	double x, y, z;
	
	public PacketFXBubble(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public PacketFXBubble()
	{
	}
	
	@Override
	public IPacket onArrived(PacketFXBubble packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.summon();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private void summon()
	{
		ParticleProxy_Client.queueParticleSpawn(new FXBubble(Minecraft.getMinecraft().world, new Vec3d(x, y, z)));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		x = nbt.getDouble("x");
		y = nbt.getDouble("y");
		z = nbt.getDouble("z");
	}
}