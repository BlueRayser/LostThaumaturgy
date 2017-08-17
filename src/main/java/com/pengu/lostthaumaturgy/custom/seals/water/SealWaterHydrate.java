package com.pengu.lostthaumaturgy.custom.seals.water;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.seal.SealInstance;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;
import com.pengu.lostthaumaturgy.tile.TileSeal;

public class SealWaterHydrate extends SealInstance
{
	public SealWaterHydrate(TileSeal seal)
	{
		super(seal);
	}
	
	@Override
	public void tick()
	{
		WorldLocation loc = seal.getLocation();
		Random rand = loc.getWorld().rand;
		
		if(loc.getRedstone() > 0)
			return;
		
		EnumFacing to = seal.orientation;
		EnumFacing from = to.getOpposite();
		
		float mult = .7F;
		
		float modX = (rand.nextFloat() - rand.nextFloat()) * .5F;
		float modY = (rand.nextFloat() - rand.nextFloat()) * .5F;
		float modZ = (rand.nextFloat() - rand.nextFloat()) * .5F;
		
		Vec3d pos = loc.getState().getBoundingBox(loc.getWorld(), loc.getPos()).getCenter().add(new Vec3d(loc.getPos()));
		pos = pos.addVector(from.getFrontOffsetX() * .05, from.getFrontOffsetY() * .05, from.getFrontOffsetZ() * .05);
		Vec3d end = pos.addVector(to.getFrontOffsetX() * mult + modX, to.getFrontOffsetY() * mult + modY, to.getFrontOffsetZ() * mult + modZ);
		
		BlockPos center0 = loc.getPos().offset(to, 4);
		if(to.getAxis() == Axis.Y)
			center0 = loc.getPos().offset(to, to == EnumFacing.UP ? 2 : 4);
		int rad = 5;
		int tries = 40;
		
		int driest = 7;
		WorldLocation drps = null;
		
		while(tries-- > 0)
		{
			WorldLocation l = new WorldLocation(loc.getWorld(), center0.add(rand.nextInt(rad) - rand.nextInt(rad), rand.nextInt(3) - rand.nextInt(3), rand.nextInt(rad) - rand.nextInt(rad)));
			
			if(l.getBlock() == Blocks.FARMLAND && l.getMeta() < 7)
			{
				if(driest >= l.getMeta())
				{
					driest = l.getMeta();
					drps = l;
				}
			}
		}
		
		if(drps != null)
		{
			WorldLocation l = drps;
			
			AtmosphereChunk chunk = AtmosphereTicker.getAuraChunkFromBlockCoords(l);
			
			if(rand.nextInt(100) < 5)
				++chunk.badVibes;
			
			int left = 7 - l.getMeta();
			l.setMeta(l.getMeta() + rand.nextInt(left + 1));
			HCNetwork.manager.sendToAllAround(new PacketFXWisp2(pos.x, pos.y, pos.z, end.x, end.y, end.z, .5F, 2), loc.getPointWithRad(48));
			
			int r = 8 + rand.nextInt(9);
			
			for(int i = 0; i < r; ++i)
			{
				Vec3d start = new Vec3d(l.getPos()).addVector(rand.nextDouble(), .8, rand.nextDouble());
				Vec3d end2 = start.addVector(0, .7 + rand.nextFloat() * .5F, 0);
				HCNetwork.manager.sendToAllAround(new PacketFXWisp2(start.x, start.y, start.z, end2.x, end2.y, end2.z, 1F, 2), loc.getPointWithRad(48));
			}
		}
	}
}