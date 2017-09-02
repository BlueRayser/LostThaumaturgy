package com.pengu.lostthaumaturgy.core.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class LTPathFinder
{
	private IBlockAccess worldMap;
	private LTPath path = new LTPath();
	private IntHashMap pointMap = new IntHashMap();
	private LTPathPoint[] pathOptions = new LTPathPoint[32];
	private boolean isWoddenDoorAllowed;
	private boolean isMovementBlockAllowed;
	private boolean isPathingInWater;
	private boolean canEntityDrown;
	
	public LTPathFinder(IBlockAccess par1IBlockAccess, boolean par2, boolean par3, boolean par4, boolean par5)
	{
		this.worldMap = par1IBlockAccess;
		this.isWoddenDoorAllowed = par2;
		this.isMovementBlockAllowed = par3;
		this.isPathingInWater = par4;
		this.canEntityDrown = par5;
	}
	
	public LTPathEntity createEntityPathTo(Entity par1Entity, Entity par2Entity, float par3)
	{
		return this.createEntityPathTo(par1Entity, par2Entity.posX, par2Entity.getEntityBoundingBox().minY, par2Entity.posZ, par3);
	}
	
	public LTPathEntity createEntityPathTo(Entity par1Entity, int par2, int par3, int par4, float par5)
	{
		return this.createEntityPathTo(par1Entity, (float) par2 + 0.5f, (float) par3 + 0.5f, (float) par4 + 0.5f, par5);
	}
	
	private LTPathEntity createEntityPathTo(Entity par1Entity, double par2, double par4, double par6, float par8)
	{
		this.path.clearPath();
		this.pointMap.clearMap();
		boolean var9 = this.isPathingInWater;
		int var10 = MathHelper.floor((double) (par1Entity.getEntityBoundingBox().minY + 0.5));
		if(this.canEntityDrown && par1Entity.isInWater())
		{
			var10 = (int) par1Entity.getEntityBoundingBox().minY;
			Block var11 = this.worldMap.getBlockState(new BlockPos(MathHelper.floor((double) par1Entity.posX), var10, MathHelper.floor((double) par1Entity.posZ))).getBlock();
			while(var11 == Blocks.FLOWING_WATER || var11 == Blocks.WATER)
				var11 = this.worldMap.getBlockState(new BlockPos(MathHelper.floor((double) par1Entity.posX), ++var10, MathHelper.floor((double) par1Entity.posZ))).getBlock();
			var9 = this.isPathingInWater;
			this.isPathingInWater = false;
		} else
		{
			var10 = MathHelper.floor((double) (par1Entity.getEntityBoundingBox().minY + 0.5));
		}
		LTPathPoint var15 = this.openPoint(MathHelper.floor((double) par1Entity.getEntityBoundingBox().minX), var10, MathHelper.floor((double) par1Entity.getEntityBoundingBox().minZ));
		LTPathPoint var12 = this.openPoint(MathHelper.floor((double) (par2 - (double) (par1Entity.width / 2.0f))), MathHelper.floor((double) par4), MathHelper.floor((double) (par6 - (double) (par1Entity.width / 2.0f))));
		LTPathPoint var13 = new LTPathPoint(MathHelper.floor((float) (par1Entity.width + 1.0f)), MathHelper.floor((float) (par1Entity.height + 1.0f)), MathHelper.floor((float) (par1Entity.width + 1.0f)));
		LTPathEntity var14 = this.addToPath(par1Entity, var15, var12, var13, par8);
		this.isPathingInWater = var9;
		return var14;
	}
	
	private LTPathEntity addToPath(Entity par1Entity, LTPathPoint par2PathPoint, LTPathPoint par3PathPoint, LTPathPoint par4PathPoint, float par5)
	{
		par2PathPoint.totalPathDistance = 0.0f;
		par2PathPoint.distanceToTarget = par2PathPoint.distanceToNext = par2PathPoint.distanceSqTo(par3PathPoint);
		this.path.clearPath();
		this.path.addPoint(par2PathPoint);
		LTPathPoint var6 = par2PathPoint;
		while(!this.path.isPathEmpty())
		{
			LTPathPoint var7 = this.path.dequeue();
			if(var7.equals(par3PathPoint))
			{
				return this.createEntityPath(par2PathPoint, par3PathPoint);
			}
			if(var7.distanceSqTo(par3PathPoint) < var6.distanceSqTo(par3PathPoint))
			{
				var6 = var7;
			}
			var7.isFirst = true;
			int var8 = this.findPathOptions(par1Entity, var7, par4PathPoint, par3PathPoint, par5);
			for(int var9 = 0; var9 < var8; ++var9)
			{
				LTPathPoint var10 = this.pathOptions[var9];
				float var11 = var7.totalPathDistance + var7.distanceSqTo(var10);
				if(var10.isAssigned() && var11 >= var10.totalPathDistance)
					continue;
				var10.previous = var7;
				var10.totalPathDistance = var11;
				var10.distanceToNext = var10.distanceSqTo(par3PathPoint);
				if(var10.isAssigned())
				{
					this.path.changeDistance(var10, var10.totalPathDistance + var10.distanceToNext);
					continue;
				}
				var10.distanceToTarget = var10.totalPathDistance + var10.distanceToNext;
				this.path.addPoint(var10);
			}
		}
		if(var6 == par2PathPoint)
		{
			return null;
		}
		return this.createEntityPath(par2PathPoint, var6);
	}
	
	private int findPathOptions(Entity par1Entity, LTPathPoint par2PathPoint, LTPathPoint par3PathPoint, LTPathPoint par4PathPoint, float par5)
	{
		int var6 = 0;
		int var7 = 0;
		if(this.getVerticalOffset(par1Entity, par2PathPoint.x, par2PathPoint.y + 1, par2PathPoint.z, par3PathPoint) == 1)
		{
			var7 = 1;
		}
		LTPathPoint var8 = this.getSafePoint(par1Entity, par2PathPoint.x, par2PathPoint.y, par2PathPoint.z + 1, par3PathPoint, var7);
		LTPathPoint var9 = this.getSafePoint(par1Entity, par2PathPoint.x - 1, par2PathPoint.y, par2PathPoint.z, par3PathPoint, var7);
		LTPathPoint var10 = this.getSafePoint(par1Entity, par2PathPoint.x + 1, par2PathPoint.y, par2PathPoint.z, par3PathPoint, var7);
		LTPathPoint var11 = this.getSafePoint(par1Entity, par2PathPoint.x, par2PathPoint.y, par2PathPoint.z - 1, par3PathPoint, var7);
		if(var8 != null && !var8.isFirst && var8.distanceTo(par4PathPoint) < par5)
		{
			this.pathOptions[var6++] = var8;
		}
		if(var9 != null && !var9.isFirst && var9.distanceTo(par4PathPoint) < par5)
		{
			this.pathOptions[var6++] = var9;
		}
		if(var10 != null && !var10.isFirst && var10.distanceTo(par4PathPoint) < par5)
		{
			this.pathOptions[var6++] = var10;
		}
		if(var11 != null && !var11.isFirst && var11.distanceTo(par4PathPoint) < par5)
		{
			this.pathOptions[var6++] = var11;
		}
		return var6;
	}
	
	private LTPathPoint getSafePoint(Entity par1Entity, int par2, int par3, int par4, LTPathPoint par5PathPoint, int par6)
	{
		LTPathPoint var7 = null;
		int var8 = this.getVerticalOffset(par1Entity, par2, par3, par4, par5PathPoint);
		if(var8 == 2)
		{
			return this.openPoint(par2, par3, par4);
		}
		if(var8 == 1)
		{
			var7 = this.openPoint(par2, par3, par4);
		}
		if(var7 == null && par6 > 0 && var8 != -3 && var8 != -4 && this.getVerticalOffset(par1Entity, par2, par3 + par6, par4, par5PathPoint) == 1)
		{
			var7 = this.openPoint(par2, par3 + par6, par4);
			par3 += par6;
		}
		if(var7 != null)
		{
			int var9 = 0;
			int var10 = 0;
			while(par3 > 0)
			{
				var10 = this.getVerticalOffset(par1Entity, par2, par3 - 1, par4, par5PathPoint);
				if(this.isPathingInWater && var10 == -1)
					return null;
				if(var10 != 1)
					break;
				if(var9++ >= par1Entity.getMaxFallHeight())
					return null;
				if(--par3 <= 0)
					continue;
				var7 = this.openPoint(par2, par3, par4);
			}
			if(var10 == -2)
				return null;
		}
		return var7;
	}
	
	private final LTPathPoint openPoint(int par1, int par2, int par3)
	{
		int var4 = LTPathPoint.makeHash(par1, par2, par3);
		LTPathPoint var5 = (LTPathPoint) this.pointMap.lookup(var4);
		if(var5 == null)
		{
			var5 = new LTPathPoint(par1, par2, par3);
			this.pointMap.addKey(var4, (Object) var5);
		}
		return var5;
	}
	
	public int getVerticalOffset(Entity par1Entity, int par2, int par3, int par4, LTPathPoint par5PathPoint)
	{
		return LTPathFinder.func_82565_a(par1Entity, par2, par3, par4, par5PathPoint, this.isPathingInWater, this.isMovementBlockAllowed, this.isWoddenDoorAllowed);
	}
	
	public static int func_82565_a(Entity par0Entity, int par1, int par2, int par3, LTPathPoint par4PathPoint, boolean par5, boolean par6, boolean par7)
	{
		boolean var8 = false;
		for(int var9 = par1; var9 < par1 + par4PathPoint.x; ++var9)
		{
			for(int var10 = par2; var10 < par2 + par4PathPoint.y; ++var10)
			{
				for(int var11 = par3; var11 < par3 + par4PathPoint.z; ++var11)
				{
					BlockPos pos = new BlockPos(var9, var10, var11);
					IBlockState var12 = par0Entity.world.getBlockState(pos);
					Block var13 = var12.getBlock();
					if(var13 == Blocks.AIR)
						continue;
					if(var13 == Blocks.TRAPDOOR)
						var8 = true;
					else if(var13 != Blocks.FLOWING_WATER && var13 != Blocks.WATER)
					{
						if(!(par7 || !(var13 instanceof BlockDoor) && !(var13 instanceof BlockFenceGate)))
							return 0;
					} else
					{
						if(par5)
						{
							return -1;
						}
						var8 = true;
					}
					if(!var13.isPassable(par0Entity.world, new BlockPos(var9, var10, var11)) || par6 && (var13 instanceof BlockDoor || var13 instanceof BlockFenceGate))
						continue;
					EnumBlockRenderType rt = var13.getRenderType(var12);
					if(rt == EnumBlockRenderType.LIQUID || rt == EnumBlockRenderType.INVISIBLE)
						return -3;
					if(var13 == Blocks.TRAPDOOR)
						return -4;
					Material var14 = var13.getMaterial(var12);
					if(var14 != Material.LAVA)
					{
						return 0;
					}
					if(par0Entity.handleWaterMovement())
						continue;
					return -2;
				}
			}
		}
		return var8 ? 2 : 1;
	}
	
	private LTPathEntity createEntityPath(LTPathPoint par1PathPoint, LTPathPoint par2PathPoint)
	{
		int var3 = 1;
		LTPathPoint var4 = par2PathPoint;
		while(var4.previous != null)
		{
			++var3;
			var4 = var4.previous;
		}
		LTPathPoint[] var5 = new LTPathPoint[var3];
		var4 = par2PathPoint;
		var5[--var3] = par2PathPoint;
		while(var4.previous != null)
		{
			var4 = var4.previous;
			var5[--var3] = var4;
		}
		return new LTPathEntity(var5);
	}
}