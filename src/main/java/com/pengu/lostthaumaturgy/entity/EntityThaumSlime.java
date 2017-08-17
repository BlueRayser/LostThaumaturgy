package com.pengu.lostthaumaturgy.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import com.google.common.base.Predicate;
import com.pengu.hammercore.HammerCore;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class EntityThaumSlime extends EntityLiving implements IMob
{
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(LTInfo.MOD_ID, "entities/thaum_slime");
	private static final DataParameter<Integer> ANGER_LEVEL = EntityDataManager.createKey(EntityThaumSlime.class, DataSerializers.VARINT);
	private static final DataParameter<Float> CURRENT_VIS = EntityDataManager.createKey(EntityThaumSlime.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> CURRENT_TAINT = EntityDataManager.createKey(EntityThaumSlime.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> SLIME_JUMP_DELAY = EntityDataManager.createKey(EntityThaumSlime.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> SLIME_SIZE = EntityDataManager.createKey(EntityThaumSlime.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> IS_TAINTED = EntityDataManager.createKey(EntityThaumSlime.class, DataSerializers.BOOLEAN);
	
	public EntityThaumSlime(World worldIn)
	{
		super(worldIn);
		setSlimeSize(2);
		experienceValue = 1;
	}
	
	@Override
	protected boolean canDespawn()
	{
		return true;
	}
	
	@Override
	public void onUpdate()
	{
		List<EntityThaumSlime> slimes = world.getEntitiesWithinAABB(EntityThaumSlime.class, getEntityBoundingBox().grow(32));
		int slimesFound = 0;
		for(int i = 0; i < slimes.size(); ++i)
		{
			EntityThaumSlime slime = slimes.get(i);
			if(slime != this && !slime.isDead)
				++slimesFound;
			if(slimesFound >= 3)
				break;
		}
		
		if(!world.isRemote && (world.getDifficulty() == EnumDifficulty.PEACEFUL || slimesFound >= 3))
		{
			setDead();
			return;
		}
		
		if(dataManager.get(ANGER_LEVEL) > 0)
			dataManager.set(ANGER_LEVEL, dataManager.get(ANGER_LEVEL) - 1);
		
		despawnEntity();
		
		EntityPlayer entityplayer = world.getClosestPlayer(posX, posY, posZ, 16, new Predicate<Entity>()
		{
			@Override
			public boolean apply(Entity input)
			{
				if(input instanceof EntityPlayer && !((EntityPlayer) input).capabilities.disableDamage)
					return true;
				return false;
			}
		});
		
		boolean facingThaum = false;
		
		AtmosphereChunk ac = AuraTicker.getAuraChunkFromBlockCoords(world, getPosition());
		
		if(ac != null && ac.goodVibes > 0)
		{
			ac.goodVibes--;
			dataManager.set(CURRENT_VIS, dataManager.get(CURRENT_VIS));
		}
		
		if(ac != null && ac.badVibes > 0)
		{
			ac.badVibes--;
			dataManager.set(CURRENT_TAINT, dataManager.get(CURRENT_TAINT));
		}
		
		if(getAttackTarget() instanceof EntityPlayer && ((EntityPlayer) getAttackTarget()).capabilities.isCreativeMode)
			setAttackTarget(null);
		
		if(dataManager.get(ANGER_LEVEL) > 0 && getAttackTarget() != null && getAttackTarget() != entityplayer && !facingThaum)
			faceEntity(getAttackTarget(), 10, 20);
		
		if(entityplayer != null && !facingThaum)
			faceEntity(entityplayer, 10, 20);
		
		int slimeJumpDelay = dataManager.get(SLIME_JUMP_DELAY);
		
		if(onGround && slimeJumpDelay-- <= 0)
		{
			slimeJumpDelay = rand.nextInt(20) + 10;
			if(entityplayer != null)
				slimeJumpDelay /= 3;
			isJumping = true;
			if(getSlimeSize() > 2)
				HammerCore.audioProxy.playSoundAt(world, "entity.slime.jump", getPosition(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * .2F + 1) * .8F, SoundCategory.HOSTILE);
			moveStrafing = 1 - rand.nextFloat() * 2;
			moveForward = 1 * getSlimeSize();
			Vec3d look = getLookVec().add(getPositionVector());
			motionX = ((look.x - posX) / 5) * (getSlimeSize() * .5);
			motionZ = ((look.z - posZ) / 5) * (getSlimeSize() * .5);
			jump();
		} else
		{
			isJumping = false;
			if(onGround)
			{
				moveForward = 0.0f;
				moveStrafing = 0.0f;
			}
			
			AxisAlignedBB a1 = getEntityBoundingBox();
			AxisAlignedBB a2 = entityplayer != null ? entityplayer.getEntityBoundingBox() : null;
			
			if(a1 != null && a2 != null && a1.intersects(a2))
			{
				entityplayer.attackEntityAsMob(this);
				entityplayer.attackEntityFrom(DamageSource.causeMobDamage(this), getSlimeSize() + 1);
			}
		}
		
		dataManager.set(SLIME_JUMP_DELAY, slimeJumpDelay);
		
		{
			float juice;
			
			super.onUpdate();
			
			if(onGround)
			{
				int i = this.getSlimeSize();
				
				// if(i > 5)
				// this.k.a((nn) this, "mob.slime", this.C_(),
				// ((this.U.nextFloat() - this.U.nextFloat()) * 0.2f + 1.0f) /
				// 0.8f);
				// if(i > 14)
				// this.k.a((nn) this, this.o, this.p + 0.5, this.q, 1.0f);
			}
			
			if(getSlimeSize() < 15 && (juice = Math.max(dataManager.get(CURRENT_VIS), dataManager.get(CURRENT_TAINT))) >= (float) (50 * getSlimeSize()))
			{
				dataManager.set(IS_TAINTED, dataManager.get(CURRENT_VIS) <= dataManager.get(CURRENT_TAINT));
				dataManager.set(CURRENT_VIS, dataManager.get(CURRENT_VIS) / 2F);
				dataManager.set(CURRENT_TAINT, dataManager.get(CURRENT_TAINT) / 2F);
				setSlimeSize(getSlimeSize() + 1);
			}
		}
	}
	
	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
	{
		super.dropLoot(wasRecentlyHit, lootingModifier, source);
		
		EnumMultiMaterialType[] artifacts = { EnumMultiMaterialType.ANCIENT_POTTERY, EnumMultiMaterialType.TARNISHED_CHALICE, EnumMultiMaterialType.WORN_STATUETTE, EnumMultiMaterialType.ANCIENT_SEAL, EnumMultiMaterialType.ANCIENT_WEAPON };
		
		if(rand.nextInt(5) == 0 && !world.isRemote)
			entityDropItem(artifacts[rand.nextInt(artifacts.length)].stack(), 0);
	}
	
	public boolean isTainted()
	{
		return dataManager.get(IS_TAINTED);
	}
	
	public float getPureVis()
	{
		return dataManager.get(CURRENT_VIS);
	}
	
	public float getTaintedVis()
	{
		return dataManager.get(CURRENT_TAINT);
	}
	
	public void setPureVis(float vis)
	{
		dataManager.set(CURRENT_VIS, vis);
	}
	
	public void setTaintedVis(float vis)
	{
		dataManager.set(CURRENT_TAINT, vis);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		boolean type = dataManager.get(CURRENT_VIS) >= dataManager.get(CURRENT_TAINT);
		super.onDeath(cause);
		int i = this.getSlimeSize();
		if(!world.isRemote && i > 1)
		{
			for(int j = 0; j < 2; ++j)
			{
				int dice = rand.nextInt(4);
				if(dice >= 3)
					continue;
				float f = ((float) (j % 2) - 0.5f) * (float) i / 4F;
				float f1 = ((float) (j / 2) - 0.5f) * (float) i / 4F;
				EntityThaumSlime entityslime = new EntityThaumSlime(world);
				entityslime.setSlimeSize(Math.max(1, i / 2));
				entityslime.dataManager.set(IS_TAINTED, !type);
				entityslime.setPositionAndRotation(posX + f, posY + 0.5, posZ + f1, rand.nextFloat() * 360.0f, 0.0f);
				world.spawnEntity(entityslime);
			}
		}
	}
	
	@Override
	protected float getSoundVolume()
	{
		return .3F;
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_SLIME_DEATH;
	}
	
	@Override
	protected SoundEvent getFallSound(int heightIn)
	{
		return SoundEvents.ENTITY_SLIME_SQUISH;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_SLIME_HURT;
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		return LOOT_TABLE;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(source.getTrueSource() instanceof EntityLivingBase)
		{
			setAttackTarget((EntityLivingBase) source.getTrueSource());
			dataManager.set(ANGER_LEVEL, 300);
		}
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(ANGER_LEVEL, 0);
		dataManager.register(SLIME_JUMP_DELAY, rand.nextInt(20) + 10);
		dataManager.register(CURRENT_VIS, 0F);
		dataManager.register(CURRENT_TAINT, 0F);
		dataManager.register(SLIME_SIZE, 2);
		dataManager.register(IS_TAINTED, false);
		
		setSlimeSize(2);
	}
	
	public void setSlimeSize(int size)
	{
		dataManager.set(SLIME_SIZE, size);
		setSize(.4F * size, .4F * size);
		experienceValue = size;
	}
	
	public int getSlimeSize()
	{
		return dataManager.get(SLIME_SIZE);
	}
	
	public void faceTileEntity(TileEntity entity, float f, float f1)
	{
		double d = (double) entity.getPos().getX() - posX + 0.5;
		double d2 = (double) entity.getPos().getZ() - posZ + 0.5;
		double d1 = 0.5 - (posY + getEyeHeight());
		double d3 = MathHelper.sqrt(d * d + d2 * d2);
		float f2 = (float) (Math.atan2(d2, d) * 180.0 / 3.1415927410125732) - 90.0f;
		float f3 = (float) (-Math.atan2(d1, d3) * 180.0 / 3.1415927410125732);
		rotationPitch = -updRot(rotationPitch, f3, f1);
		rotationYaw = updRot(rotationYaw, f2, f);
	}
	
	private float updRot(float f, float f1, float f2)
	{
		float f3;
		for(f3 = f1 - f; f3 < -180.0f; f3 += 360.0f)
			;
		while(f3 >= 180.0f)
			f3 -= 360.0f;
		if(f3 > f2)
			f3 = f2;
		if(f3 < -f2)
			f3 = -f2;
		return f + f3;
	}
	
	@Override
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}
}