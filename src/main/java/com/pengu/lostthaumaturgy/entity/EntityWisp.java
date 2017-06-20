package com.pengu.lostthaumaturgy.entity;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.init.SoundEventsLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp1;

public class EntityWisp extends EntityFlying implements IMob
{
	private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityWisp.class, DataSerializers.VARINT);
	
	private int aggroCooldown = 0;
	public int courseChangeCooldown = 0;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	public int prevAttackCounter = 0;
	public int attackCounter = 0;
	
	public EntityWisp(World worldIn)
	{
		super(worldIn);
		setSize(.9F, .9F);
		experienceValue = 5;
		setNoGravity(true);
	}
	
	public void setType(int type)
	{
		dataManager.set(TYPE, type);
	}
	
	public int getType()
	{
		SoundEvent se = getAmbientSound();
		return dataManager.get(TYPE);
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_PLAYER_LEVELUP;
	}
	
	@Override
	protected SoundEvent getHurtSound()
	{
		return SoundEventsLT.FIZZ.sound;
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PLAYER_BREATH;
	}
	
	@Override
	protected float getSoundVolume()
	{
		return .25F;
	}
	
	@Override
	protected float getSoundPitch()
	{
		return .9F;
	}
	
	@Override
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}
	
	@Override
	protected boolean canDespawn()
	{
		return true;
	}
	
	@Override
	protected Item getDropItem()
	{
		return ItemsLT.MULTI_MATERIAL;
	}
	
	@Override
	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
	{
		if(lootingModifier < 0)
			lootingModifier = 0;
		++lootingModifier;
		
		if(rand.nextInt(9) < 5)
		{
			EnumMultiMaterialType type = EnumMultiMaterialType.VIS_CRYSTAL;
			if(getType() == 1)
				type = EnumMultiMaterialType.VAPOROUS_CRYSTAL;
			if(getType() == 2)
				type = EnumMultiMaterialType.AQUEOUS_CRYSTAL;
			if(getType() == 3)
				type = EnumMultiMaterialType.EARTHEN_CRYSTAL;
			if(getType() == 4)
				type = EnumMultiMaterialType.FIERY_CRYSTAL;
			if(getType() == 5)
				type = EnumMultiMaterialType.TAINTED_CRYSTAL;
			
			ItemStack stack = type.get();
			stack.setCount(1 + rand.nextInt(lootingModifier));
			entityDropItem(stack, 0);
		} else
			entityDropItem(EnumMultiMaterialType.CRACKED_WISP_SHELL.stack(), 0);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(TYPE, 0);
	}
	
	@Override
	public void onEntityUpdate()
	{
		List<EntityWisp> wisps = world.getEntitiesWithinAABB(EntityWisp.class, getEntityBoundingBox().grow(32));
		int wispsFound = 0;
		for(int i = 0; i < wisps.size(); ++i)
		{
			EntityWisp wisp = wisps.get(i);
			if(wisp != this && !wisp.isDead)
				++wispsFound;
			if(wispsFound >= 5)
				break;
		}
		
		if(!world.isRemote && (world.getDifficulty() == EnumDifficulty.PEACEFUL || wispsFound >= 5))
		{
			setDead();
			return;
		}
		
		if(ticksExisted == 1 && !world.isRemote)
		{
			int type = 0;
			
			Biome bid = world.getBiome(getPosition());
			SIAuraChunk ac = AuraTicker.getAuraChunkFromBlockCoords(world, getPosition());
			
			if(AuraTicker.BIOME_EARTH.contains(bid))
				type = 3;
			
			if(AuraTicker.BIOME_WATER.contains(bid) || world.canSnowAt(getPosition(), true) || world.isRaining())
				type = 2;
			
			if(AuraTicker.BIOME_AIR.contains(bid) || world.canSnowAt(getPosition(), true) || world.isRaining())
				type = 1;
			
			if(AuraTicker.BIOME_FIRE.contains(bid) || AuraTicker.BIOME_FIREFLOWER.contains(bid) || nearLava())
			{
				isImmuneToFire = true;
				type = 4;
			}
			
			if(ac != null && (float) ac.taint > (float) LTConfigs.aura_max * .5)
				type = 5;
			else if(ac != null && (float) ac.vis > (float) LTConfigs.aura_max * .5)
				type = 0;
			
			setType(type);
		}
		
		super.onEntityUpdate();
		
		despawnEntity();
		
		if(getType() == 4)
			isImmuneToFire = true;
		
		if(world.isRemote)
			return;
		
		HCNetwork.manager.sendToAllAround(new PacketFXWisp1(posX + (double) rand.nextFloat() - (double) rand.nextFloat(), posY + (double) rand.nextFloat() - (double) rand.nextFloat(), posZ + (double) rand.nextFloat() - (double) rand.nextFloat(), .8F, dataManager.get(TYPE)), new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 48));
		
		prevAttackCounter = attackCounter;
		double attackrange = 12.0;
		double d = waypointX - posX;
		double d1 = waypointY - posY;
		double d2 = waypointZ - posZ;
		double d3 = MathHelper.sqrt((double) (d * d + d1 * d1 + d2 * d2));
		if(getAttackTarget() == null)
		{
			if(d3 < 1 || d3 > 60 || onGround)
			{
				waypointX = posX + (double) ((rand.nextFloat() * 2 - 1) * 16);
				waypointY = posY + (double) ((rand.nextFloat() * 2 - .25F) * 16F);
				waypointZ = posZ + (double) ((rand.nextFloat() * 2 - 1) * 16);
			}
		} else if(!canEntityBeSeen(getAttackTarget()))
		{
			waypointX = posX + (double) ((rand.nextFloat() * 2 - 1) * 4);
			waypointY = posY + (double) ((rand.nextFloat() * 2 - .25F) * 4);
			waypointZ = posZ + (double) ((rand.nextFloat() * 2 - 1) * 4);
		} else if(getAttackTarget().getDistanceSqToEntity(this) > attackrange * attackrange * .75)
		{
			waypointX = getAttackTarget().posX;
			waypointY = getAttackTarget().posY + 1;
			waypointZ = getAttackTarget().posZ;
		} else
		{
			waypointX = posX + (double) ((rand.nextFloat() * 2 - 1) * 2);
			waypointY = posY + (double) ((rand.nextFloat() * 2 - .25F) * 2);
			waypointZ = posZ + (double) ((rand.nextFloat() * 2 - 1) * 2);
		}
		
		if(courseChangeCooldown-- <= 0)
		{
			courseChangeCooldown += rand.nextInt(5) + 2;
			if(isCourseTraversable(waypointX, waypointY, waypointZ, d3))
			{
				motionX += d / d3 * 0.1;
				motionY += d1 / d3 * 0.1;
				motionZ += d2 / d3 * 0.1;
			} else
			{
				waypointX = posX;
				waypointY = posY;
				waypointZ = posZ;
			}
		}
		
		if(getAttackTarget() != null && getAttackTarget().isDead)
			setAttackTarget(null);
		
		--aggroCooldown;
		
		if(getType() == 5 && (getAttackTarget() == null || aggroCooldown-- <= 0))
		{
			setAttackTarget(world.getClosestPlayer(posX, posY, posZ, 16, new Predicate<Entity>()
			{
				@Override
				public boolean apply(Entity input)
				{
					if(input instanceof EntityPlayer)
						return !((EntityPlayer) input).capabilities.disableDamage;
					return input.canBeAttackedWithItem();
				}
			}));
			
			if(getAttackTarget() != null)
				aggroCooldown = 50;
		}
		
		if(getAttackTarget() != null && getAttackTarget().getDistanceSqToEntity(this) < attackrange * attackrange)
		{
			double d5 = getAttackTarget().posX - posX;
			double d6 = getAttackTarget().getEntityBoundingBox().maxY + (double) (getAttackTarget().height / 2.0f) - (posY + (double) (height / 2.0f));
			double d7 = getAttackTarget().posY - posZ;
			renderYawOffset = rotationYaw = (-(float) Math.atan2(d5, d7)) * 180 / (float) Math.PI;
			if(canEntityBeSeen(getAttackTarget()))
			{
				++attackCounter;
				if(attackCounter == 20)
				{
					HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":zap", posX, posY, posZ, 1F, 1.1F, SoundCategory.HOSTILE);
					getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), 1 + rand.nextFloat() * world.getDifficulty().ordinal());
					
					int rgb = 0xFFFFFF;
					
					int byte0 = 0;
					
					if(world.getDifficulty() != EnumDifficulty.PEACEFUL)
					{
						if(world.getDifficulty() == EnumDifficulty.EASY)
							byte0 = 1;
						else if(world.getDifficulty() == EnumDifficulty.NORMAL)
							byte0 = 3;
						else if(world.getDifficulty() == EnumDifficulty.HARD)
							byte0 = 6;
					}
					
					if(getType() == 0)
					{
						rgb = 0x570379;
						getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.NAUSEA, byte0 * 40));
					}
					
					if(getType() == 1)
					{
						rgb = 0xFFCC57;
					}
					
					if(getType() == 2)
					{
						rgb = 0xAA9EFF;
						getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, byte0 * 40));
					}
					
					if(getType() == 3)
					{
						rgb = 0x59FF6C;
						getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.POISON, byte0 * 40));
					}
					
					if(getType() == 4)
					{
						rgb = 0xFF5959;
						getAttackTarget().setFire(2);
					}
					
					if(getType() == 5)
					{
						rgb = 0x4E0E8A;
						getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, byte0 * 40));
					}
					
					HammerCore.particleProxy.spawnZap(world, getPositionVector().addVector(width / 4, height / 4, width / 4), getAttackTarget().getPositionVector(), new Color(rgb));
					attackCounter = -30 + world.rand.nextInt(30);
				}
			} else if(attackCounter > 0)
			{
				--attackCounter;
			}
		} else
		{
			renderYawOffset = rotationYaw = (-(float) Math.atan2(motionX, motionZ)) * 180 / (float) Math.PI;
			if(attackCounter > 0)
			{
				--attackCounter;
			}
		}
		
		double d0 = (double) width / 2.0D;
		this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + (double) this.height, this.posZ + d0));
	}
	
	private boolean isCourseTraversable(double d, double d1, double d2, double d3)
	{
		double d4 = (waypointX - posX) / d3;
		double d5 = (waypointY - posY) / d3;
		double d6 = (waypointZ - posZ) / d3;
		AxisAlignedBB axisalignedbb = getEntityBoundingBox();
		int i = 1;
		while((double) i < d3)
		{
			axisalignedbb = axisalignedbb.offset(d4, d5, d6);
			if(world.getEntitiesInAABBexcluding(this, axisalignedbb, Predicates.alwaysTrue()).size() > 0)
				return false;
			++i;
		}
		int x = (int) waypointX;
		int y = (int) waypointY;
		int z = (int) waypointZ;
		IBlockState state = world.getBlockState(new BlockPos(x, y, z));
		if(state.getBlock() instanceof BlockFluidBase || state.getBlock() instanceof BlockLiquid)
			return false;
		for(int a = 0; a < 11; ++a)
		{
			if(world.isAirBlock(new BlockPos(x, y - a, z)))
				continue;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(source.getTrueSource() instanceof EntityLivingBase)
		{
			setAttackTarget((EntityLivingBase) source.getTrueSource());
			aggroCooldown = 200;
		}
		
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setType(nbt.getInteger("Type"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Type", getType());
		return super.writeToNBT(nbt);
	}
	
	private boolean nearLava()
	{
		for(int x = -5; x <= 5; ++x)
		{
			for(int y = -5; y <= 5; ++y)
			{
				for(int z = -5; z <= 5; ++z)
				{
					BlockPos pos = new BlockPos((int) posX + x, (int) posY + y, (int) posZ + z);
					
					if(pos.getY() < 0)
						continue;
					
					if(world.getBlockState(pos).getBlock() == Blocks.LAVA || world.getBlockState(pos).getBlock() == Blocks.FLOWING_LAVA)
						return true;
				}
			}
		}
		return false;
	}
}