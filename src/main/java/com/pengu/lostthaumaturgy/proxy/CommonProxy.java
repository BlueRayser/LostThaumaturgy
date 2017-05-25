package com.pengu.lostthaumaturgy.proxy;

import net.minecraftforge.fml.relauncher.Side;

import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

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
	
	public boolean shadersSupported()
	{
		return false;
	}
	
	public Side getProxySide()
	{
		return Side.SERVER;
	}
}