package com.pengu.lostthaumaturgy.core.tile;

import java.util.List;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.hammercore.tile.TileSyncableTickable;

public class TileSingularityJar extends TileSyncableTickable
{
	public final NetPropertyNumber<Integer> storedXP;
	public final NetPropertyNumber<Integer> absorbCooldown;
	
	{
		storedXP = new NetPropertyNumber<Integer>(this, 0);
		absorbCooldown = new NetPropertyNumber<Integer>(this, 15);
	}
	
	@Override
	public void tick()
	{
		if(absorbCooldown.get() > 0)
			absorbCooldown.set(absorbCooldown.get() - 1);
		
		if(absorbCooldown.get() <= 0)
		{
			List<EntityXPOrb> orbs = world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(pos).expand(8, 3, 8));
			for(EntityXPOrb orb : orbs)
			{
				double acceleration = .1;
				orb.motionX = MathHelper.clip(acceleration / (pos.getX() - orb.posX + .5), -.1, .1);
				orb.motionY = MathHelper.clip(acceleration / (pos.getY() - orb.posY + .5), -.1, .1);
				orb.motionZ = MathHelper.clip(acceleration / (pos.getZ() - orb.posZ + .5), -.1, .1);
			}
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
	}
}