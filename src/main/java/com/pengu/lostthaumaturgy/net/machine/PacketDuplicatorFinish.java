package com.pengu.lostthaumaturgy.net.machine;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.mrdimka.hammercore.proxy.ParticleProxy_Client;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;

public class PacketDuplicatorFinish implements IPacket, IPacketListener<PacketDuplicatorFinish, IPacket>
{
	public BlockPos pos;
	
	public PacketDuplicatorFinish(BlockPos pos)
	{
		this.pos = pos;
	}
	
	public PacketDuplicatorFinish()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("p", pos.toLong());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		pos = BlockPos.fromLong(nbt.getLong("p"));
	}
	
	@Override
	public IPacket onArrived(PacketDuplicatorFinish packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.executeClient();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void executeClient()
	{
		World world = Minecraft.getMinecraft().world;
		
		for(int a = 0; a < 25; ++a)
		{
			float xx = (float) pos.getX() + .5F - (world.rand.nextFloat() - world.rand.nextFloat()) * 1.5F;
			float yy2 = (float) pos.getY() + .5F - (world.rand.nextFloat() - world.rand.nextFloat()) * .1F;
			float zz = (float) pos.getZ() + .5F - (world.rand.nextFloat() - world.rand.nextFloat()) * 1.5F;
			
			FXWisp ef = new FXWisp(world, (float) pos.getX() + .5F, (float) pos.getY() + .5F, (float) pos.getZ() + .5F, xx, yy2, zz, 1.25F, world.rand.nextInt(5));
			ParticleProxy_Client.queueParticleSpawn(ef);
		}
		
		HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":stomp", pos, .1F, 1, SoundCategory.BLOCKS);
	}
}