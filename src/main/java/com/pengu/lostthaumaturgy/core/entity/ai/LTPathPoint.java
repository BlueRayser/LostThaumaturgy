package com.pengu.lostthaumaturgy.core.entity.ai;

import net.minecraft.util.math.MathHelper;

public class LTPathPoint
{
	public final int x;
	public final int y;
	public final int z;
	private final int hash;
	int index = -1;
	float totalPathDistance;
	float distanceToNext;
	float distanceToTarget;
	LTPathPoint previous;
	public boolean isFirst = false;
	
	public LTPathPoint(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		hash = makeHash(x, y, z);
	}
	
	public static int makeHash(int x, int y, int z)
	{
		return y & 0xFF | (x & 0x7FFF) << 8 | (z & 0x7FFF) << 24 | (x < 0 ? Integer.MIN_VALUE : 0) | (z < 0 ? 32768 : 0);
	}
	
	public float distanceTo(LTPathPoint point)
	{
		float var2 = point.x - x;
		float var3 = point.y - y;
		float var4 = point.z - z;
		return MathHelper.sqrt(var2 * var2 + var3 * var3 + var4 * var4);
	}
	
	public float distanceSqTo(LTPathPoint point)
	{
		float var2 = point.x - x;
		float var3 = point.y - y;
		float var4 = point.z - z;
		return var2 * var2 + var3 * var3 + var4 * var4;
	}
	
	public boolean equals(Object o)
	{
		if(!(o instanceof LTPathPoint))
			return false;
		LTPathPoint var2 = (LTPathPoint) o;
		return (var2.hash == hash) && (var2.x == x) && (var2.y == y) && (var2.z == z);
	}
	
	public int hashCode()
	{
		return hash;
	}
	
	public boolean isAssigned()
	{
		return index >= 0;
	}
	
	public String toString()
	{
		return x + ", " + y + ", " + z;
	}
}