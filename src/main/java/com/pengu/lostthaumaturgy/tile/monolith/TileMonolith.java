package com.pengu.lostthaumaturgy.tile.monolith;

import java.awt.Color;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.net.pkt.PacketSpawnZap;
import com.mrdimka.hammercore.tile.IMalfunctionable;
import com.mrdimka.hammercore.tile.TileSyncable;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;

public class TileMonolith extends TileSyncableTickable
{
	public int soundDelay = 0;
	
	@Override
	public void tick()
	{
		double yOff = getYOffset(-1);
		double yOffNT = getYOffset(0);
		
		if(yOffNT - yOff > 0)
		{
			int m = yOffNT - yOff >= getYOffset(1) - getYOffset(0) ? 6 : 3;
			List<Entity> inBlock = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX(), pos.getY() + yOff, pos.getZ(), pos.getX() + 1, pos.getY() + 1 + yOff, pos.getZ() + 1));
			for(Entity e : inBlock)
				e.move(MoverType.SHULKER, 0, (yOffNT - yOff) * m, 0);
		}
		
		if(world.isRemote)
			return;
		
		if(soundDelay > 0)
			soundDelay--;
		
		SIAuraChunk si = AuraTicker.getAuraChunkFromBlockCoords(world, pos);
		
		if(si != null && si.monolithVibes < si.monolithVibeCap)
			si.monolithVibes++;
		
		if(world.rand.nextInt(100) == 0)
		{
			double x = (world.rand.nextDouble() - world.rand.nextDouble()) * .5D;
			double y = (world.rand.nextDouble() - world.rand.nextDouble()) * .5D;
			double z = (world.rand.nextDouble() - world.rand.nextDouble()) * .5D;
			
			double x2 = (world.rand.nextDouble() - world.rand.nextDouble()) * 3D;
			double y2 = (world.rand.nextDouble() - world.rand.nextDouble()) * 3D;
			double z2 = (world.rand.nextDouble() - world.rand.nextDouble()) * 3D;
			
			PacketSpawnZap zap = new PacketSpawnZap();
			zap.color = new Color(0x310A5E);
			zap.start = new Vec3d(pos).addVector(x + .5, y + .5 + yOff, z + .5);
			zap.end = new Vec3d(pos).addVector(x2 + .5, y2 + .5 + yOff, z2 + .5);
			zap.world = world.provider.getDimension();
			HCNetwork.manager.sendToAllAround(zap, getSyncPoint(16));
		}
		
		if(si != null && si.badVibes < 25 && world.rand.nextInt(250) == 0)
		{
			si.badVibes++;
			
			double x = (world.rand.nextDouble() - world.rand.nextDouble()) * .5D;
			double y = (world.rand.nextDouble() - world.rand.nextDouble()) * .5D;
			double z = (world.rand.nextDouble() - world.rand.nextDouble()) * .5D;
			
			double x2 = (world.rand.nextDouble() - world.rand.nextDouble()) * 5D;
			double y2 = (world.rand.nextDouble() - world.rand.nextDouble()) * 5D;
			double z2 = (world.rand.nextDouble() - world.rand.nextDouble()) * 5D;
			
			HCNetwork.getManager("particles").sendToAllAround(new PacketFXWisp2(pos.getX() + .5 + x, pos.getY() + yOff + .5 + y, pos.getZ() + .5 + z, pos.getX() + .5 + x2, pos.getY() + yOff + .5 + y2, pos.getZ() + .5 + z2, 4F, 5), getSyncPoint(48));
		}
		
		if(soundDelay == 0 || atTickRate(120))
			for(int x = -5; x < 6; ++x)
				for(int y = -5; y < 6; ++y)
					for(int z = -5; z < 6; ++z)
					{
						if(x == y && y == z && x == 0)
							continue;
						
						BlockPos pos = this.pos.add(x, y, z);
						IMalfunctionable mal1 = WorldUtil.cast(world.getTileEntity(pos), IMalfunctionable.class);
						IMalfunctionable mal2 = WorldUtil.cast(world.getBlockState(pos).getBlock(), IMalfunctionable.class);
						
						int to = (int) Math.round(Math.sqrt(x * x + y * y + z * z));
						if(to < 1 || rand.nextInt(to + 1) == 0)
						{
							boolean flag = false;
							if(mal1 != null)
							{
								mal1.causeGeneralMalfunction();
								if(mal1 instanceof TileSyncable)
									((TileSyncable) mal1).sync();
								flag = true;
							}
							if(mal2 != null)
							{
								mal2.causeGeneralMalfunction();
								flag = true;
							}
							if(flag)
								HammerCore.particleProxy.spawnZap(world, new Vec3d(pos), new Vec3d(this.pos), new Color(0x310A5E));
						}
					}
		
		if(soundDelay == 0)
		{
			if(world.getBlockState(pos.down()).getBlock() != world.getBlockState(pos).getBlock())
				HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":monolith", pos, .6F, 1F, SoundCategory.BLOCKS);
			soundDelay = 450 + world.rand.nextInt(150);
		}
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