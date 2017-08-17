package com.pengu.lostthaumaturgy.custom.aura.api;

import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;

final class AuraAttachmentLT implements IAuraAttachment
{
	@Override
	public void handle(AtmosphereChunk chunk)
	{
		chunk.monolithVibeCap = (short) Math.floor(Math.sqrt(chunk.monolithVibeCap));
		chunk.monolithVibes = (short) Math.floor(Math.sqrt(chunk.monolithVibeCap));
	}
}