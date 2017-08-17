package com.pengu.lostthaumaturgy.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp_EntitySingularity_doSuckage;

public class EntitySingularity extends EntityThrowable
{
	public int fuse = 60;
	public List<BlockPos> positions = new ArrayList<>();
	
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	
	private float currentVis;
	
	public EntitySingularity(World worldIn)
	{
		super(worldIn);
	}
	
	public EntitySingularity(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		setPositionAndUpdate(throwerIn.posX, throwerIn.posY, throwerIn.posZ);
		setHeadingFromThrower(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0, 1, 1);
	}
	
	@Override
	protected float getGravityVelocity()
	{
		return .07F;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("Fuse", fuse);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		fuse = compound.getInteger("Fuse");
		super.readFromNBT(compound);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		fuse--;
		
		if(fuse == 0)
			doExplosion();
		else if(fuse < 0 && !world.isRemote)
			doSuckage();
		
		if(fuse <= -170)
			doAfterExplosion();
	}
	
	public void doExplosion()
	{
		setNoGravity(true);
		explosionX = posX;
		explosionY = posY;
		explosionZ = posZ;
		
		if(!world.isRemote)
		{
			HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":singularity", getPosition(), 2F, 1F, SoundCategory.AMBIENT);
			HCNetwork.spawnParticle(world, EnumParticleTypes.EXPLOSION_LARGE, explosionX, explosionY, explosionZ, 0, 0, 0);
		}
		
		float f = 4;
		int i = 16;
		for(int j = 0; j < i; ++j)
		{
			for(int l = 0; l < i; ++l)
			{
				for(int j1 = 0; j1 < i; ++j1)
				{
					if(j != 0 && j != i - 1 && l != 0 && l != i - 1 && j1 != 0 && j1 != i - 1)
						continue;
					double d = (float) j / ((float) i - 1.0f) * 2.0f - 1.0f;
					double d1 = (float) l / ((float) i - 1.0f) * 2.0f - 1.0f;
					double d2 = (float) j1 / ((float) i - 1.0f) * 2.0f - 1.0f;
					double d3 = Math.sqrt(d * d + d1 * d1 + d2 * d2);
					d /= d3;
					d1 /= d3;
					d2 /= d3;
					double d5 = this.explosionX;
					double d7 = this.explosionY;
					double d9 = this.explosionZ;
					float f2 = 0.3f;
					for(float f1 = 4 * (0.7f + rand.nextFloat() * 0.6f); f1 > 0.0f; f1 -= f2 * 0.75f)
					{
						int k4;
						int l4;
						int j4 = (int) Math.round((double) d5);
						BlockPos pos = new BlockPos(j4, k4 = (int) Math.round((double) d7), l4 = (int) Math.round((double) d9));
						IBlockState state = world.getBlockState(pos);
						if(state.getBlock().isAir(state, world, pos))
							f1 -= (state.getBlock().getExplosionResistance(this) + 0.3f) * f2;
						if(f1 > 0)
							positions.add(pos);
						d5 += d * (double) f2;
						d7 += d1 * (double) f2;
						d9 += d2 * (double) f2;
					}
				}
			}
		}
		int k = (int) Math.round((double) (this.explosionX - (double) 4 - 1.0));
		int i1 = (int) Math.round((double) (this.explosionX + (double) 4 + 1.0));
		int k1 = (int) Math.round((double) (this.explosionY - (double) 4 - 1.0));
		int l1 = (int) Math.round((double) (this.explosionY + (double) 4 + 1.0));
		int i2 = (int) Math.round((double) (this.explosionZ - (double) 4 - 1.0));
		int j2 = (int) Math.round((double) (this.explosionZ + (double) 4 + 1.0));
		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) k, (double) k1, (double) i2, (double) i1, (double) l1, (double) j2));
		Vec3d vec3d = new Vec3d((double) this.explosionX, (double) this.explosionY, (double) this.explosionZ);
		for(int k2 = 0; k2 < list.size(); ++k2)
		{
			Entity entity = list.get(k2);
			double d4 = entity.getDistanceSq(explosionX, explosionY, explosionZ) / 4;
			if(d4 > 1.0)
				continue;
			double d6 = entity.posX - this.explosionX;
			double d8 = entity.posY - this.explosionY;
			double d10 = entity.posZ - this.explosionZ;
			double d11 = Math.floor((double) (d6 * d6 + d8 * d8 + d10 * d10));
			double d12 = world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
			double d13 = (1.0 - d4) * d12;
			entity.attackEntityFrom(DamageSource.causeExplosionDamage(getThrower()), (int) ((d13 * d13 + d13) / 2.0 * 8.0 * (double) 4 + 1.0));
			double d14 = d13;
			entity.motionX += (d6 /= d11) * d14;
			entity.motionY += (d8 /= d11) * d14;
			entity.motionZ += (d10 /= d11) * d14;
		}
	}
	
	public void doSuckage()
	{
		HCNetwork.manager.sendToAllAround(new PacketFXWisp_EntitySingularity_doSuckage(posX, posY, posZ, .9F, 5), new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 48));
		
		Entity entity;
		int a;
		int auraSize = 10;
		int lastdistance = 999;
		int lastx = 0;
		int lasty = 0;
		int lastz = 0;
		int mx = rand.nextBoolean() ? 1 : -1;
		int my2 = rand.nextBoolean() ? 1 : -1;
		int mz = rand.nextBoolean() ? 1 : -1;
		for(int a2 = -auraSize; a2 < auraSize + 1; ++a2)
			for(int b = -auraSize; b < auraSize + 1; ++b)
				for(int c = -auraSize; c < auraSize + 1; ++c)
				{
					BlockPos pos = new BlockPos((int) posX + a2 * mx, (int) posY + b * my2, (int) posZ + c * mz);
					int distance;
					if(world.getBlockState(pos).getBlock() == Blocks.AIR || (distance = (int) getDistanceSq(posX + (double) (a2 * mx) + 0.5, posY + (double) (b * my2) + 0.5, posZ + (double) (c * mz) + 0.5)) >= lastdistance || distance > auraSize)
						continue;
					lastdistance = distance;
					lastx = (int) (posX + a2 * mx);
					lasty = (int) (posY + b * my2);
					lastz = (int) (posZ + c * mz);
				}
		
		int sel = 5 + rand.nextInt(8);
		for(int i = 0; i < sel && !positions.isEmpty(); ++i)
			bL:
			{
				BlockPos chunkposition = positions.remove(rand.nextInt(positions.size()));
				int jj = chunkposition.getX();
				int kk = chunkposition.getY();
				int ll = chunkposition.getZ();
				IBlockState state = world.getBlockState(chunkposition);
				double d = (float) jj + rand.nextFloat();
				double d1 = (float) kk + rand.nextFloat();
				double d2 = (float) ll + rand.nextFloat();
				double d3 = d - this.explosionX;
				double d4 = d1 - this.explosionY;
				double d5 = d2 - this.explosionZ;
				double d6 = Math.sqrt((double) (d3 * d3 + d4 * d4 + d5 * d5));
				d3 /= d6;
				d4 /= d6;
				d5 /= d6;
				double d7 = 0.5 / (d6 / (double) 4 + 0.1);
				
				if(rand.nextInt(5) == 0)
					HCNetwork.spawnParticle(world, EnumParticleTypes.SMOKE_NORMAL, d, d1, d2, d3 *= -(d7 *= (double) (rand.nextFloat() * rand.nextFloat() + 0.3f)), d4 *= -d7, d5 *= -d7);
				HCNetwork.manager.sendToAllAround(new PacketFXWisp_EntitySingularity_doSuckage(d, d1, d2, .9F, 5), new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 48));
				
				if(state.getBlock().isAir(state, world, chunkposition) || state.getBlock().getBlockHardness(state, world, chunkposition) == -1)
					break bL;
				
				world.setBlockState(chunkposition, Blocks.AIR.getDefaultState());
			}
		
		if(lastdistance < 999)
		{
			int jj = lastx;
			int kk = lasty;
			int ll = lastz;
			BlockPos pos = new BlockPos(jj, kk, ll);
			IBlockState state = world.getBlockState(pos);
			
			if(state.getBlock().getBlockHardness(state, world, pos) < 100 && state.getBlock().getBlockHardness(state, world, pos) != -1)
				// pb.m[id].a(this.k, lastx, lasty, lastz, this.k.e(lastx,
				// lasty, lastz), 0.9f, 0);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
		
		int xm = (int) Math.floor((double) (posX - (double) auraSize - 1.0));
		int xp = (int) Math.floor((double) (posX + (double) auraSize + 1.0));
		int ym = (int) Math.floor((double) (posY - (double) auraSize - 1.0));
		int yp = (int) Math.floor((double) (posY + (double) auraSize + 1.0));
		int zm = (int) Math.floor((double) (posZ - (double) auraSize - 1.0));
		int zp = (int) Math.floor((double) (posZ + (double) auraSize + 1.0));
		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) xm, (double) ym, (double) zm, (double) xp, (double) yp, (double) zp));
		for(a = 0; a < list.size(); ++a)
		{
			entity = list.get(a);
			double d4 = entity.getDistanceSq(posX, posY, posZ) / (double) auraSize;
			double dx = entity.posX - posX;
			double dy = entity.posY - posY;
			double dz = entity.posZ - posZ;
			double d11 = Math.sqrt((double) (dx * dx + dy * dy + dz * dz));
			dx /= d11;
			dy /= d11;
			dz /= d11;
			double d13 = (1.0 - d4) * 0.1;
			if(d13 < 0.0)
			{
				d13 = 0.0;
			}
			double d14 = d13;
			entity.motionX -= dx * d14;
			entity.motionY -= dy * d14;
			entity.motionZ -= dz * d14;
			if(entity instanceof EntityLivingBase)
				continue;
			
			PacketFXWisp_EntitySingularity_doSuckage pkt = new PacketFXWisp_EntitySingularity_doSuckage(entity.posX, entity.posY, entity.posZ, .9F, 5);
			HCNetwork.manager.sendToAllAround(pkt, new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 48));
		}
		xm = (int) Math.floor((double) (posX - 0.75));
		xp = (int) Math.floor((double) (posX + 0.75));
		ym = (int) Math.floor((double) (posY - 0.75));
		yp = (int) Math.floor((double) (posY + 0.75));
		zm = (int) Math.floor((double) (posZ - 0.75));
		zp = (int) Math.floor((double) (posZ + 0.75));
		list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) xm, (double) ym, (double) zm, (double) xp, (double) yp, (double) zp));
		for(a = 0; a < list.size(); ++a)
		{
			entity = list.get(a);
			if(entity instanceof EntitySingularity)
				continue;
			if(entity instanceof EntityItem)
			{
				int val = (int) RecipesCrucible.getSmeltingValue(((EntityItem) entity).getItem());
				this.currentVis += (float) val;
				entity.setDead();
				
				PacketFXWisp_EntitySingularity_doSuckage pkt = new PacketFXWisp_EntitySingularity_doSuckage(entity.posX, entity.posY, entity.posZ, 1.2F, 5);
				HCNetwork.manager.sendToAllAround(pkt, new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 48));
				
				continue;
			}
			
			entity.attackEntityFrom(DamageSource.MAGIC, 3);
			
			if(!(entity instanceof EntityLivingBase))
				continue;
			
			this.currentVis += 1;
		}
	}
	
	public void doAfterExplosion()
	{
		AtmosphereChunk ac = AuraTicker.getAuraChunkFromBlockCoords(world, getPosition());
		
		if(ac != null)
		{
			ac.badVibes += currentVis / 4F;
			ac.goodVibes += currentVis / 3F;
		}
		
		setDead();
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		if(result != null && result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			if(face.getAxis() == Axis.X)
				motionX = -motionX * .25;
			if(face.getAxis() == Axis.Y)
				motionY = -motionY * .25;
			if(face.getAxis() == Axis.Z)
				motionZ = -motionZ * .25;
			fuse /= 2;
		}
	}
}