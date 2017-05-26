package com.pengu.lostthaumaturgy.net.wisp;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class PacketFXWisp_AuraTicker_spillTaint implements IPacket, IPacketListener<PacketFXWisp_AuraTicker_spillTaint, IPacket>
{
	double x, y, z, tx, ty, tz;
	float partialTicks;
	int type;
	
	public PacketFXWisp_AuraTicker_spillTaint(double x, double y, double z, double tx, double ty, double tz, float partialTicks, int type)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.partialTicks = partialTicks;
		this.type = type;
	}
	
	public PacketFXWisp_AuraTicker_spillTaint()
	{
	}
	
	@Override
	public IPacket onArrived(PacketFXWisp_AuraTicker_spillTaint packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			summon();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private void summon()
	{
		FXWisp wisp;
		ClientProxy.queueParticle(wisp = new FXWisp(Minecraft.getMinecraft().world, x, y, z, partialTicks, type));
		wisp.tinkle = false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
		nbt.setDouble("tx", tx);
		nbt.setDouble("ty", ty);
		nbt.setDouble("tz", tz);
		nbt.setFloat("p", partialTicks);
		nbt.setInteger("type", type);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		x = nbt.getDouble("x");
		y = nbt.getDouble("y");
		z = nbt.getDouble("z");
		tx = nbt.getDouble("tx");
		ty = nbt.getDouble("ty");
		tz = nbt.getDouble("tz");
		partialTicks = nbt.getFloat("p");
		type = nbt.getInteger("type");
	}
}