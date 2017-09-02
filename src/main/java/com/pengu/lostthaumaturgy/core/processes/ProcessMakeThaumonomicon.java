package com.pengu.lostthaumaturgy.core.processes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.IUpdatable;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp1;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;

public class ProcessMakeThaumonomicon implements IUpdatable
{
	private static List<ProcessMakeThaumonomicon> ps = new ArrayList<>();
	
	public static boolean isRunningAt(WorldLocation loc)
	{
		for(ProcessMakeThaumonomicon t : ps)
			if(t.pos.getWorld().provider.getDimension() == loc.getWorld().provider.getDimension() && t.pos.getPos().equals(loc.getPos()))
				return true;
		return false;
	}
	
	public static boolean start(WorldLocation pos, EnumFacing hitFace, double hx, double hy, double hz)
	{
		if(isRunningAt(pos))
			return false;
		
		ProcessMakeThaumonomicon t;
		ps.add(t = new ProcessMakeThaumonomicon(pos, hitFace, hx, hy, hz));
		HammerCore.updatables.add(t);
		
		return true;
	}
	
	public final WorldLocation pos;
	public final EnumFacing hitFace;
	public final double[] hitXYZ;
	public int ticksExisted = 0;
	public TargetPoint point;
	public List<Vec3d> dots = new ArrayList<>();
	
	public ProcessMakeThaumonomicon(WorldLocation pos, EnumFacing hitFace, double hx, double hy, double hz)
	{
		this.pos = pos;
		point = pos.getPointWithRad(48);
		this.hitFace = hitFace;
		this.hitXYZ = new double[] { hx, hy, hz };
	}
	
	@Override
	public boolean isAlive()
	{
		return ticksExisted <= 350 && pos.getBlock() == Blocks.BOOKSHELF;
	}
	
	public List<Vec3d> calcFacePoints(EnumFacing face, float progress, double x, double z)
	{
		List<Vec3d> d = new ArrayList<>();
		double y = face.getAxis() == Axis.X ? face.getFrontOffsetX() : face.getAxis() == Axis.Y ? face.getFrontOffsetY() : face.getFrontOffsetZ();
		int ps = 1 + (int) (progress * 32);
		
		for(int i = 0; i < ps; ++i)
		{
			double pg = (i / (double) ps) * progress;
			
			double xa = x * pg;
			double xb = (1 - x) * pg;
			
			double za = z * pg;
			double zb = (1 - z) * pg;
			
			double xt = xa + xb;
			double zt = za + zb;
			
			d.add(new Vec3d(pos.getPos().getX() + xt, pos.getPos().getY() + y, pos.getPos().getZ() + zt));
		}
		
		return d;
	}
	
	public double[] applyMatrix(EnumFacing face, double x, double y, double z)
	{
		double a = x;
		double b = y;
		double c = z;
		
		if(face.getAxis() == Axis.X)
		{
			a = z;
			b = y;
			c = x;
		}
		
		if(face.getAxis() == Axis.Y)
		{
			a = x;
			b = z;
			c = y;
		}
		
		if(face.getAxis() == Axis.Z)
		{
			a = x;
			b = y;
			c = z;
		}
		
		return new double[] { a, b, c };
	}
	
	@Override
	public void update()
	{
		List<Vec3d> points = null;
		
		if(ticksExisted < 150)
		{
			float prog = ticksExisted / 150F;
			points = new ArrayList<>();
			
			float cx = 0;
			float cz = 0;
			
			for(int i = 0; i < 16; ++i)
			{
				int segm = (i % 4) + 1;
				float y =  prog;
				
				if(segm == 1)
					cx += 1;
				
				if(segm == 2)
					cz += 1;
				
				if(segm == 3)
					cx -= 1;
				
				if(segm == 4)
					cz -= 1;
				
				points.add(new Vec3d(pos.getPos().getX() + cx, pos.getPos().getY() + y, pos.getPos().getZ() + cz));
				points.add(new Vec3d(pos.getPos().getX() + cx, pos.getPos().getY() + 1 - prog, pos.getPos().getZ() + cz));
				
				points.add(new Vec3d(pos.getPos().getX() + y, pos.getPos().getY() + cz, pos.getPos().getZ() + cx));
				points.add(new Vec3d(pos.getPos().getX() + 1 - y, pos.getPos().getY() + cz, pos.getPos().getZ() + cx));
				
				points.add(new Vec3d(pos.getPos().getX() + cz, pos.getPos().getY() + cx, pos.getPos().getZ() + y));
				points.add(new Vec3d(pos.getPos().getX() + cz, pos.getPos().getY() + cx, pos.getPos().getZ() + 1 - y));
			}
		}
		
		Random rand = pos.getWorld().rand;
		
		if(ticksExisted >= 150 && ticksExisted < 300)
		{
			float prog = (ticksExisted - 150) / 150F;
			points = new ArrayList<>();
			
			float cx = 0;
			float cz = 0;
			
			for(int i = 0; i < 64; ++i)
			{
				int segm = MathHelper.ceil((i + 1) / 16D);
				float y = prog;
				
				if(segm == 1)
					cx += 1 / 16D;
				
				if(segm == 2)
					cz += 1 / 16D;
				
				if(segm == 3)
					cx -= 1 / 16D;
				
				if(segm == 4)
					cz -= 1 / 16D;
				
				points.add(new Vec3d(pos.getPos().getX() + cx, pos.getPos().getY() + y, pos.getPos().getZ() + cz));
				points.add(new Vec3d(pos.getPos().getX() + cx, pos.getPos().getY() + 1 - prog, pos.getPos().getZ() + cz));
			}
		}
		
		if(points != null && !points.isEmpty())
			for(Vec3d p : points)
				if(rand.nextInt(20) == 0)
					HCNetwork.manager.sendToAllAround(new PacketFXWisp1(p.x, p.y, p.z, .3F, pos.getWorld().rand.nextInt(5)), point);
		
		++ticksExisted;
		if(!isAlive())
		{
			if(pos.getBlock() == Blocks.BOOKSHELF)
			{
				pos.destroyBlock(false);
				WorldUtil.spawnItemStack(pos, new ItemStack(ItemsLT.THAUMONOMICON));
			} else
			{
				for(int i = 0; i < 64; ++i)
				{
					double x = pos.getPos().getX() + rand.nextDouble();
					double y = pos.getPos().getY() + rand.nextDouble();
					double z = pos.getPos().getZ() + rand.nextDouble();
					
					double tx = x + rand.nextDouble() * 4 - rand.nextDouble() * 4;
					double ty = y + rand.nextDouble() * 4 - rand.nextDouble() * 4;
					double tz = z + rand.nextDouble() * 4 - rand.nextDouble() * 4;
					
					HCNetwork.manager.sendToAllAround(new PacketFXWisp2(x, y, z, tx, ty, tz, .6F, 4), point);
				}
			}
			
			ps.remove(this);
		}
	}
}