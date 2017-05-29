package com.pengu.lostthaumaturgy.custom.aura.api;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;

import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

public class AuraAttachments
{
	private static IAuraAttachment[] attachments = new IAuraAttachment[0];
	
	{
		registerAttachment(new AuraAttachmentLT());
	}
	
	public static void registerAttachment(IAuraAttachment attachment)
	{
		attachments = ArrayUtils.add(attachments, attachment);
	}
	
	public static void attach(SIAuraChunk chunk)
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