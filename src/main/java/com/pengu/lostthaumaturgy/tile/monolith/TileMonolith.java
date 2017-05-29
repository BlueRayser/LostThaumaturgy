package com.pengu.lostthaumaturgy.tile.monolith;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

public class TileMonolith extends TileSyncableTickable
{
	public int soundDelay = 0;
	
	@Override
	public void tick()
	{
		double yOff = getYOffset(0);
		double yOffNT = getYOffset(1);
		
		if(yOffNT - yOff > 0)
		{
			List<Entity> inBlock = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).addCoord(0, yOff, 0));
			for(Entity e : inBlock)
				e.move(MoverType.SHULKER, 0, yOffNT - yOff, 0);
		}
		
		if(world.isRemote)
			return;
		
		if(soundDelay > 0)
			soundDelay--;
		if(soundDelay == 0 && world.getBlockState(pos.down()).getBlock() != world.getBlockState(pos).getBlock())
		{
			HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":monolith", pos, 1.3F, 1F, SoundCategory.BLOCKS);
			soundDelay = 450 + world.rand.nextInt(150);
		}
		SIAuraChunk si = AuraTicker.getAuraChunkFromBlockCoords(world, pos);
		if(si != null && si.monolithVibes < si.monolithVibeCap)
			si.monolithVibes++;
	}
	
	public double getYOffset(long additionalTicks)
	{
		return (Math.cos(((pos.getX() + "|" + pos.getZ()).hashCode() + world.getTotalWorldTime() + additionalTicks) / 16D) - .5) * .25 + .25;
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
}