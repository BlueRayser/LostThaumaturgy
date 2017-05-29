package com.pengu.lostthaumaturgy.events;

import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mrdimka.hammercore.annotations.MCFBus;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.entity.EntitySmartZombie;

@MCFBus
public class MobSpawnEvents
{
	@SubscribeEvent
	public void mobSpawnCheck(LivingSpawnEvent.CheckSpawn evt)
	{
		
	}
	
	@SubscribeEvent
	public void mobSpawnCheck(LivingSpawnEvent.SpecialSpawn evt)
	{
		EntitySmartZombie zombie = WorldUtil.cast(evt.getEntity(), EntitySmartZombie.class);
		if(zombie != null)
			zombie.setChild(zombie.getRNG().nextFloat() < ForgeModContainer.zombieBabyChance);
	}
	
	@SubscribeEvent
	public void getPotentialSpawns(WorldEvent.PotentialSpawns evt)
	{
		
	}
}