package com.pengu.lostthaumaturgy.client;

import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

public class ClientSIAuraChunk
{
	private static SIAuraChunk client_chunk;
	
	public static void setClientChunk(SIAuraChunk client_chunk)
	{
		ClientSIAuraChunk.client_chunk = client_chunk;
	}
	
	public static SIAuraChunk getClientChunk()
	{
		if(client_chunk == null)
			client_chunk = new SIAuraChunk();
		return client_chunk;
	}
}