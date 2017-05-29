package com.pengu.lostthaumaturgy.custom.aura.api;

import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

final class AuraAttachmentLT implements IAuraAttachment
{
	@Override
	public void handle(SIAuraChunk chunk)
	{
		chunk.monolithVibeCap = (short) Math.floor(Math.sqrt(chunk.monolithVibeCap));
		chunk.monolithVibes = (short) Math.floor(Math.sqrt(chunk.monolithVibeCap));
	}
}