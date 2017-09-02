package com.pengu.lostthaumaturgy.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.core.emote.EmoteData;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.net.PacketEmote;

public class CommonProxy
{
	public void updateClientAuraChunk(AtmosphereChunk chunk)
	{
	}
	
	public void preInit()
	{
	}
	
	public void init()
	{
	}
	
	public void spawnEmote(EmoteData data, MessageContext ctx)
	{
		if(data == null)
			return;
		HCNetwork.manager.sendToAllAround(new PacketEmote(data), new TargetPoint(data.getWorld(), data.getX(), data.getY(), data.getZ(), 64));
	}
	
	public boolean shadersSupported()
	{
		return false;
	}
	
	public Side getProxySide()
	{
		return Side.SERVER;
	}
	
	public World getClientWorld()
	{
		return null;
	}
	
	public void openThaumonomicon()
	{
		
	}
}