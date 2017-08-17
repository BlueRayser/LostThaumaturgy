package com.pengu.lostthaumaturgy.custom.aura.api;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;

import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;

public class AtmosphereAttachments
{
	private static IAtmosphereAttachment[] attachments = new IAtmosphereAttachment[0];
	
	{
		registerAttachment(new AtmosphereAttachmentLT());
	}
	
	public static void registerAttachment(IAtmosphereAttachment attachment)
	{
		attachments = ArrayUtils.add(attachments, attachment);
	}
	
	public static void attach(AtmosphereChunk chunk)
	{
		for(int i = 0; i < attachments.length; ++i)
			attachments[i].handle(chunk);
	}
	
	private static final ThreadLocal<ByteBuffer> byte4 = ThreadLocal.withInitial(() ->
	{
		return ByteBuffer.allocate(4);
	});
	
	private static final ThreadLocal<ByteBuffer> byte8 = ThreadLocal.withInitial(() ->
	{
		return ByteBuffer.allocate(8);
	});
	
	public static byte[] longToBytes(long num)
	{
		ByteBuffer buf = byte8.get();
		buf.position(0);
		buf.putLong(num);
		return buf.array();
	}
	
	public static byte[] intToBytes(int num)
	{
		ByteBuffer buf = byte4.get();
		buf.position(0);
		buf.putInt(num);
		return buf.array();
	}
}