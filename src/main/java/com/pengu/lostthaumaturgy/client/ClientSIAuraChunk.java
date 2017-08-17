package com.pengu.lostthaumaturgy.client;

import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;

public class ClientSIAuraChunk
{
	private static AtmosphereChunk client_chunk;
	
	public static void setClientChunk(AtmosphereChunk client_chunk)
	{
		ClientSIAuraChunk.client_chunk = client_chunk;
	}
	
	public static AtmosphereChunk getClientChunk()
	{
		if(client_chunk == null)
			client_chunk = new AtmosphereChunk();
		return client_chunk;
	}
}