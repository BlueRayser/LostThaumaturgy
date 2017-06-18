package com.pengu.lostthaumaturgy.proxy;

import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.emote.EmoteData;
import com.pengu.lostthaumaturgy.net.PacketEmote;

public class CommonProxy
{
	public void updateClientAuraChunk(SIAuraChunk chunk)
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
}