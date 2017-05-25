package com.pengu.lostthaumaturgy.entity;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.LTInfo;

public class EntitySmartZombie extends EntityZombie
{
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(LTInfo.MOD_ID, "entities/smart_zombie");
	
	public EntitySmartZombie(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	public void setChild(boolean childZombie)
	{
		if(!childZombie)
			super.setChild(childZombie);
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		return LOOT_TABLE;
	}
}