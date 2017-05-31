package com.pengu.lostthaumaturgy.net.wisp;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.proxy.ParticleProxy_Client;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;

public class PacketMonolithWisp implements IPacket, IPacketListener<PacketMonolithWisp, IPacket>
{
	public BlockPos pos;
	
	public PacketMonolithWisp(BlockPos pos)
    {
		this.pos = pos;
    }
	
	public PacketMonolithWisp()
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
	public IPacket onArrived(PacketMonolithWisp packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
		{
			new Thread(()->
			{
				Thread.currentThread().setName("MonolithOpenParticleSpawnThread");
				try
				{
					packet.client();
					Thread.sleep(500L);
					packet.client();
					Thread.sleep(500L);
					packet.client();
					Thread.sleep(450L);
					packet.client();
				}
				catch(Throwable err) {}
			}).start();
		}
	    return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		Random rand = Minecraft.getMinecraft().world.rand;
		int amt = 90 + rand.nextInt(90);
		for(int i = 0; i < amt; ++i)
		{
			double offX = (rand.nextDouble() - rand.nextDouble());
			double offZ = (rand.nextDouble() - rand.nextDouble());
			
			double offX2 = (rand.nextDouble() - rand.nextDouble());
			double offZ2 = (rand.nextDouble() - rand.nextDouble());
			
			ParticleProxy_Client.queueParticleSpawn(new FXWisp(Minecraft.getMinecraft().world, pos.getX() + .5 + offX, pos.getY(), pos.getZ() + .5 + offZ, pos.getX() + .5 + offX2, pos.getY() + 3 + rand.nextDouble() * .5, pos.getZ() + .5 + offZ2, 2F, 5));
		}
	}
}