package com.pengu.lostthaumaturgy.custom.aura.api;

import java.util.Random;

import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;

final class AtmosphereAttachmentLT implements IAtmosphereAttachment
{
	private Random rand = new Random();
	
	@Override
	public void handle(AtmosphereChunk chunk)
	{
		chunk.monolithVibeCap = (short) Math.floor(Math.sqrt(chunk.monolithVibeCap));
		chunk.monolithVibes = (short) Math.floor(Math.sqrt(chunk.monolithVibeCap));
		
		float radUnnormal = chunk.radiation - LTConfigs.aura_radMax / 2;
		
		Random rand = chunk.world != null ? chunk.world.rand : this.rand;
		
		if(radUnnormal > 0 && (rand.nextInt(1000001) / 1000000F) <= radUnnormal && chunk.primordialNodeStrength < 10)
		{
			chunk.primordialNodeStrength += .2F;
			chunk.primordialNodeStrength *= 1.5F;
			chunk.primordialNodeStrength = Math.min(10, chunk.primordialNodeStrength);
		}
	}
}