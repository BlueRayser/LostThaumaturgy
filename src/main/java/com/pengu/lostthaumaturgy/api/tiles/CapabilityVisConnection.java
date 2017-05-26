package com.pengu.lostthaumaturgy.api.tiles;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityVisConnection
{
	@CapabilityInject(IConnection.class)
	public static Capability<IConnection> VIS = null;
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IConnection.class, new IStorage<IConnection>()
		{
			@Override
			public NBTBase writeNBT(Capability<IConnection> capability, IConnection instance, EnumFacing side)
			{
				NBTTagCompound c = new NBTTagCompound();
				c.setFloat("PureVis", instance.getPureVis());
				c.setFloat("TaintedVis", instance.getTaintedVis());
				c.setFloat("MaxVis", instance.getMaxVis());
				return c;
			}
			
			@Override
			public void readNBT(Capability<IConnection> capability, IConnection instance, EnumFacing side, NBTBase nbt)
			{
				if(nbt instanceof NBTTagCompound)
				{
					instance.setPureVis(((NBTTagCompound) nbt).getFloat("PureVis"));
					instance.setTaintedVis(((NBTTagCompound) nbt).getFloat("TaintedVis"));
				}
			}
		}, new Callable<IConnection>()
		{
			@Override
			public IConnection call() throws Exception
			{
				return null;
			}
		});
	}
}