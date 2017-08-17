package com.pengu.lostthaumaturgy.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp3;

public class EntityCustomSplashPotion extends EntityThrowable
{
	private static final DataParameter<Integer> DAMAGE_TYPE = EntityDataManager.createKey(EntityCustomSplashPotion.class, DataSerializers.VARINT);
	
	public EntityCustomSplashPotion(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		setHeadingFromThrower(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0, 1F, 2.5F);
	}
	
	public EntityCustomSplashPotion(World worldIn)
	{
		super(worldIn);
	}
	
	public void setType(int type)
	{
		dataManager.set(DAMAGE_TYPE, type);
		dataManager.setDirty(DAMAGE_TYPE);
	}
	
	public int getType()
	{
		return dataManager.get(DAMAGE_TYPE);
	}
	
	@Override
	protected void entityInit()
	{
		dataManager.register(DAMAGE_TYPE, 0);
	}
	
	@Override
	protected float getGravityVelocity()
	{
		return 0.05F;
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		if(!world.isRemote)
		{
			int type = getType();
			
			int x = 0, y = 0, z = 0;
			
			if(result.getBlockPos() != null)
			{
				x = result.getBlockPos().getX();
				y = result.getBlockPos().getY();
				z = result.getBlockPos().getZ();
			} else if(result.entityHit != null)
			{
				BlockPos pos = result.entityHit.getPosition();
				x = pos.getX();
				y = pos.getY();
				z = pos.getZ();
			}
			
			AtmosphereChunk ac = AtmosphereTicker.getAuraChunkFromBlockCoords(world, getPosition());
			
			if(type == 0 && ac != null)
				ac.vis += 150;
			else if(type == 1)
			{
				if(ac != null)
				{
					ac.badVibes += 25;
					ac.taint += 100;
				}
				
				for(int xx = x - 3; xx <= x + 3; ++xx)
					for(int yy2 = y - 3; yy2 <= y + 3; ++yy2)
						for(int zz = z - 3; zz <= z + 3; ++zz)
						{
							if(yy2 < 0 || getDistance((double) xx, (double) yy2, (double) zz) > 3)
								continue;
							AtmosphereTicker.increaseTaintedPlants(world, xx, yy2, zz);
						}
			} else if(type == 2)
			{
				if(ac != null)
				{
					ac.goodVibes += 25;
					ac.vis += 25;
				}
				
				for(int xx = x - 4; xx <= x + 4; ++xx)
					for(int yy3 = y - 4; yy3 <= y + 4; ++yy3)
						for(int zz = z - 4; zz <= z + 4; ++zz)
						{
							if(yy3 < 0 || getDistance((double) xx, (double) yy3, (double) zz) > 4.0)
								continue;
							AtmosphereTicker.decreaseTaintedPlants(world, xx, yy3, zz);
						}
			} else if(type == 3)
			{
				float maxShrink = ac.radiation;
				float shrink = Math.min(.25F, maxShrink);
				ac.radiation -= shrink;
				ac.goodVibes = 100;
				ac.badVibes = 50;
				ac.taint += 10 + rand.nextInt(30);
				ac.vis += 20 + rand.nextInt(60);
			}
			
			int color = type == 0 ? 0xC548C9 : type == 1 ? 0x6618CC : type == 3 ? 0x00FFFF : 0xA3DFDE;
			int diff = type == 3 ? 16 : 25;
			
			world.playEvent(2002, new BlockPos(this), color);
			
			int parts = 30 + rand.nextInt(40);
			for(int i = 0; i < parts; ++i)
			{
				int r = (color >> 16) & 0xFF;
				int g = (color >> 8) & 0xFF;
				int b = (color >> 0) & 0xFF;
				
				r = (int) MathHelper.clip(r + rand.nextInt(diff) - rand.nextInt(diff), 0, 255);
				g = (int) MathHelper.clip(g + rand.nextInt(diff) - rand.nextInt(diff), 0, 255);
				b = (int) MathHelper.clip(b + rand.nextInt(diff) - rand.nextInt(diff), 0, 255);
				
				color = (r << 16) | (g << 8) | b;
				
				int type_ = type == 2 ? rand.nextInt(5) : type == 1 ? 5 : 2;
				
				if(type == 0 || type == 3)
					HCNetwork.manager.sendToAllAround(new PacketFXWisp3(posX, posY, posZ, posX + (rand.nextGaussian() - rand.nextGaussian()) * 3D, posY + (rand.nextGaussian() - rand.nextGaussian()) * 3D, posZ + (rand.nextGaussian() - rand.nextGaussian()) * 3D, .9F + rand.nextFloat() * .6F, type_, color), new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 48));
				else
					HCNetwork.manager.sendToAllAround(new PacketFXWisp2(posX, posY, posZ, posX + (rand.nextGaussian() - rand.nextGaussian()) * 3D, posY + (rand.nextGaussian() - rand.nextGaussian()) * 3D, posZ + (rand.nextGaussian() - rand.nextGaussian()) * 3D, .9F + rand.nextFloat() * .6F, type_), new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 48));
			}
			
			setDead();
		}
	}
}