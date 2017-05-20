package com.pengu.lostthaumaturgy.client.extpart;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import com.pengu.hammercore.client.particle.api.common.ExtendedParticle;

public class EPFlyingCrystal extends ExtendedParticle
{
	public double tx, ty, tz;
	
	public EPFlyingCrystal(World world)
	{
		super(world);
		setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1));
		particleAgeMax = 40;
		
		posX.set(0D);
		posY.set(0D);
		posZ.set(0D);
		motionX.set(0D);
		motionY.set(0D);
		motionZ.set(0D);
		particleGravity.set(0.01D);
	}
	
	@Override
	public void update()
	{
		motionX.set(Math.min((posX.get() - tx) * .5D, .05D));
		motionY.set(Math.min((posY.get() - ty) * .5D, .05D));
		motionZ.set(Math.min((posZ.get() - tz) * .5D, .05D));
		
//		double v = .9800000190734863;
//		motionX.set(motionX.get() * v);
//		motionY.set(motionY.get() * v);
//		motionZ.set(motionZ.get() * v);
		
		posX.set(posX.get() + motionX.get());
		posY.set(posY.get() + motionY.get());
		posZ.set(posZ.get() + motionZ.get());
		
		if(particleAge++ >= particleAgeMax)
		{
			isDead.set(true);
			return;
		}
		
//		 if(!world.isRemote)
//			 super.update();
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setDouble("TargetX", tx);
		nbt.setDouble("TargetY", ty);
		nbt.setDouble("TargetZ", tz);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		tx = nbt.getDouble("TargetX");
		ty = nbt.getDouble("TargetY");
		tz = nbt.getDouble("TargetZ");
	}
	
	@Override
	public void sync()
	{
		if(!world.isRemote)
			super.sync();
	}
}