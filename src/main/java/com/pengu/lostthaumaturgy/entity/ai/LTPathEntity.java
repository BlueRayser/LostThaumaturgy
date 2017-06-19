package com.pengu.lostthaumaturgy.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class LTPathEntity
{
	private final LTPathPoint[] points;
	private int currentPathIndex;
	private int pathLength;
	
	public LTPathEntity(LTPathPoint[] par1ArrayOfPathPoint)
	{
		points = par1ArrayOfPathPoint;
		pathLength = par1ArrayOfPathPoint.length;
	}
	
	public void incrementPathIndex()
	{
		currentPathIndex += 1;
	}
	
	public boolean isFinished()
	{
		return currentPathIndex >= pathLength;
	}
	
	public LTPathPoint getFinalPathPoint()
	{
		return pathLength > 0 ? points[(pathLength - 1)] : null;
	}
	
	public LTPathPoint getPathPointFromIndex(int par1)
	{
		return points[par1];
	}
	
	public int getCurrentPathLength()
	{
		return pathLength;
	}
	
	public void setCurrentPathLength(int par1)
	{
		pathLength = par1;
	}
	
	public int getCurrentPathIndex()
	{
		return currentPathIndex;
	}
	
	public void setCurrentPathIndex(int par1)
	{
		currentPathIndex = par1;
	}
	
	public Vec3d getVectorFromIndex(Entity ent, int par2)
	{
		double var3 = points[par2].x + (int) (ent.width + 1) * .5;
		double var5 = points[par2].y;
		double var7 = points[par2].z + (int) (ent.width + 1) * .5;
		return new Vec3d(var3, var5, var7);
	}
	
	public Vec3d getPosition(Entity par1Entity)
	{
		return getVectorFromIndex(par1Entity, currentPathIndex);
	}
	
	public boolean isSamePath(LTPathEntity path)
	{
		if(path == null)
			return false;
		if(points.length != path.points.length)
			return false;
		for(int var2 = 0; var2 < points.length; var2++)
			if((points[var2].x != path.points[var2].x) || (points[var2].y != path.points[var2].y) || (points[var2].z != path.points[var2].z))
				return false;
		return true;
	}
	
	public boolean isDestinationSame(Vec3d vec)
	{
		LTPathPoint var2 = getFinalPathPoint();
		return var2 != null;
	}
}