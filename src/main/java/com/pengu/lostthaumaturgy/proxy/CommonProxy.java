package com.pengu.lostthaumaturgy.proxy;

import javax.annotation.Nullable;

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
	};
	
	public Side getProxySide()
	{
		return Side.SERVER;
	}
	
	@Nullable
	public <T> T passThroughIfClient(T object)
	{
		return null;
	}
	
	@Nullable
	public <T> T createAndPassThroughIfClient(String clazz)
	{
		return null;
	}
	
	@Nullable
	public <T> T createAndDispatchThrough(String clientClass, String serverClass)
	{
		try
		{
			return (T) Class.forName(serverClass).newInstance();
		} catch(Throwable err)
		{
		}
		
		return null;
	}
}