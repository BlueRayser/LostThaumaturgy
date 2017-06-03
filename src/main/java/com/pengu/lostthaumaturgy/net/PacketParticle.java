package com.pengu.lostthaumaturgy.net;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

public class PacketParticle implements IPacket, IPacketListener<PacketParticle, IPacket>
{
	public int world;
	public Vec3d pos, motion;
	public EnumParticleTypes particle;
	public int[] params;
	
	public PacketParticle(World world, EnumParticleTypes particle, Vec3d pos, Vec3d motion, int... params)
	{
		this.world = world.provider.getDimension();
		this.pos = pos;
		this.motion = motion;
		this.particle = particle;
		this.params = params;
	}
	
	public PacketParticle()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("p0", particle.ordinal());
		nbt.setInteger("p1", world);
		nbt.setDouble("p2", pos.xCoord);
		nbt.setDouble("p3", pos.yCoord);
		nbt.setDouble("p4", pos.zCoord);
		nbt.setDouble("p5", motion.xCoord);
		nbt.setDouble("p6", motion.yCoord);
		nbt.setDouble("p7", motion.zCoord);
		nbt.setIntArray("p8", params);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		particle = EnumParticleTypes.values()[nbt.getInteger("p0")];
		world = nbt.getInteger("p1");
		pos = new Vec3d(nbt.getDouble("p2"), nbt.getDouble("p3"), nbt.getDouble("p4"));
		motion = new Vec3d(nbt.getDouble("p5"), nbt.getDouble("p6"), nbt.getDouble("p7"));
		params = nbt.getIntArray("p8");
	}
	
	@Override
	public IPacket onArrived(PacketParticle packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		if(world != Minecraft.getMinecraft().world.provider.getDimension())
			return;
		Minecraft.getMinecraft().world.spawnParticle(particle, pos.xCoord, pos.yCoord, pos.zCoord, motion.xCoord, motion.yCoord, motion.zCoord, params);
	}
}