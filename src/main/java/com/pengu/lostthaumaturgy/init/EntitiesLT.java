package com.pengu.lostthaumaturgy.init;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.entity.EntitySmartZombie;
import com.pengu.lostthaumaturgy.entity.EntityThaumSlime;

public class EntitiesLT
{
	public static void registerEntities()
	{
		reg(EntityThaumSlime.class, "tslime", 0xAA00FF, 0x4C2187);
		reg(EntitySmartZombie.class, "smart_zombie", 0x1F340F, 0xBEB070);
		
		LootTableList.register(EntityThaumSlime.LOOT_TABLE);
		LootTableList.register(EntitySmartZombie.LOOT_TABLE);
	}
	
	private static int id = 0;
	
	private static void reg(Class<? extends Entity> entityClass, String name, int eggPrimary, int eggSecondary)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(LTInfo.MOD_ID, name), entityClass, LTInfo.MOD_ID + ":" + name, id++, LostThaumaturgy.instance, 64, 1, true, eggPrimary, eggSecondary);
	}
}