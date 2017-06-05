package com.pengu.lostthaumaturgy.entity;

import net.minecraft.entity.SharedMonsterAttributes;
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
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(17);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(.23066600417232513);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.5);
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		return LOOT_TABLE;
	}
	
	@Override
	public int getMaxSpawnedInChunk()
	{
		return 2;
	}
}