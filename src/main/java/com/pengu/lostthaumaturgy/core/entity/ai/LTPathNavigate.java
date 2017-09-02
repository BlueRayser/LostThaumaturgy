package com.pengu.lostthaumaturgy.core.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LTPathNavigate
{
	private EntityLiving theEntity;
	private World worldObj;
	private LTPathEntity currentPath;
	private Object pathLock = new Object();
	private float speed;
	private float pathSearchRange;
	private boolean noSunPathfind = false;
	private int totalTicks;
	private int ticksAtLastPos;
	private Vec3d lastPosCheck = Vec3d.ZERO;
	private boolean canPassOpenWoodenDoors = true;
	private boolean canPassClosedWoodenDoors = false;
	private boolean avoidsWater = false;
	private boolean canSwim = false;
	
	public LTPathNavigate(EntityLiving par1EntityLiving, World par2World, float par3)
	{
		theEntity = par1EntityLiving;
		worldObj = par2World;
		pathSearchRange = par3;
	}
	
	public void setAvoidsWater(boolean par1)
	{
		avoidsWater = par1;
	}
	
	public boolean getAvoidsWater()
	{
		return avoidsWater;
	}
	
	public void setBreakDoors(boolean par1)
	{
		canPassClosedWoodenDoors = par1;
	}
	
	public void setEnterDoors(boolean par1)
	{
		canPassOpenWoodenDoors = par1;
	}
	
	public boolean getCanBreakDoors()
	{
		return canPassClosedWoodenDoors;
	}
	
	public void setAvoidSun(boolean par1)
	{
		noSunPathfind = par1;
	}
	
	public void setSpeed(float par1)
	{
		speed = par1;
	}
	
	public void setCanSwim(boolean par1)
	{
		canSwim = par1;
	}
	
	public LTPathEntity getPathToXYZ(double x, double y, double z)
	{
		return !canNavigate() ? null : LTAI.getEntityPathToXYZ(theEntity, MathHelper.floor(x), (int) y, MathHelper.floor(z), pathSearchRange, canPassOpenWoodenDoors, canPassClosedWoodenDoors, avoidsWater, canSwim);
	}
	
	public boolean tryMoveToXYZ(double x, double y, double z, float moveSpeed)
	{
		return setPath(getPathToXYZ(MathHelper.floor(x), (int) y, MathHelper.floor(z)), moveSpeed);
	}
	
	public LTPathEntity getPathToEntityLiving(EntityLiving ent)
	{
		return !canNavigate() ? null : LTAI.getPathEntityToEntity(theEntity, ent, pathSearchRange, canPassOpenWoodenDoors, canPassClosedWoodenDoors, avoidsWater, canSwim);
	}
	
	public boolean tryMoveToEntityLiving(EntityLiving ent, float moveSpeed)
	{
		LTPathEntity var3 = getPathToEntityLiving(ent);
		return var3 != null ? setPath(var3, moveSpeed) : false;
	}
	
	public boolean setPath(LTPathEntity path, float navSpeed)
	{
		if(path == null)
		{
			currentPath = null;
			return false;
		}
		if(!path.isSamePath(currentPath))
			currentPath = path;
		if(noSunPathfind)
			removeSunnyPath();
		if(currentPath.getCurrentPathLength() == 0)
			return false;
		speed = navSpeed;
		Vec3d var3 = getEntityPosition();
		ticksAtLastPos = totalTicks;
		this.lastPosCheck = var3;
		return true;
	}
	
	public LTPathEntity getPath()
	{
		return currentPath;
	}
	
	public void onUpdateNavigation()
	{
		++this.totalTicks;
		if(!this.noPath())
		{
			Vec3d var1;
			if(this.canNavigate())
				this.pathFollow();
			if(!this.noPath() && this.theEntity != null && (var1 = this.currentPath.getPosition(theEntity)) != null)
				this.theEntity.getMoveHelper().setMoveTo(var1.x, var1.y, var1.z, speed);
		}
	}
	
	private void pathFollow()
	{
		int var4;
		Vec3d var1 = this.getEntityPosition();
		int var2 = this.currentPath.getCurrentPathLength();
		for(int var3 = this.currentPath.getCurrentPathIndex(); var3 < this.currentPath.getCurrentPathLength(); ++var3)
		{
			if(this.currentPath.getPathPointFromIndex((int) var3).y == (int) var1.y)
				continue;
			var2 = var3;
			break;
		}
		float var8 = this.theEntity.width * this.theEntity.width;
		for(var4 = this.currentPath.getCurrentPathIndex(); var4 < var2; ++var4)
		{
			if(var1.squareDistanceTo(this.currentPath.getVectorFromIndex(theEntity, var4)) >= (double) var8)
				continue;
			this.currentPath.setCurrentPathIndex(var4 + 1);
		}
		var4 = MathHelper.ceil((float) this.theEntity.width);
		int var5 = (int) this.theEntity.height + 1;
		int var6 = var4;
		for(int var7 = var2 - 1; var7 >= this.currentPath.getCurrentPathIndex(); --var7)
		{
			if(!this.isDirectPathBetweenPoints(var1, this.currentPath.getVectorFromIndex(this.theEntity, var7), var4, var5, var6))
				continue;
			this.currentPath.setCurrentPathIndex(var7);
			break;
		}
		if(this.totalTicks - this.ticksAtLastPos > 100)
		{
			if(var1.squareDistanceTo(this.lastPosCheck) < 2.25)
				clearPathEntity();
			this.ticksAtLastPos = this.totalTicks;
			this.lastPosCheck = var1;
		}
	}
	
	public boolean noPath()
	{
		return (currentPath == null) || (currentPath.isFinished());
	}
	
	public void clearPathEntity()
	{
		currentPath = null;
	}
	
	private Vec3d getEntityPosition()
	{
		return new Vec3d(theEntity.posX, getPathableYPos(), theEntity.posZ);
	}
	
	private int getPathableYPos()
	{
		if(theEntity.isInWater() && canSwim)
		{
			int var1 = (int) theEntity.getEntityBoundingBox().minY;
			IBlockState state = worldObj.getBlockState(new BlockPos(MathHelper.floor(theEntity.posX), var1, MathHelper.floor(theEntity.posZ)));
			int var3 = 0;
			do
			{
				if(state.getBlock() != Blocks.WATER && state.getBlock() != Blocks.FLOWING_WATER)
					return var1;
				var1++;
				state = worldObj.getBlockState(new BlockPos(MathHelper.floor(theEntity.posX), var1, MathHelper.floor(theEntity.posZ)));
				var3++;
			} while(var3 <= 16);
			return (int) theEntity.getEntityBoundingBox().minY;
		}
		return (int) (theEntity.getEntityBoundingBox().minY + 0.5D);
	}
	
	private boolean canNavigate()
	{
		return theEntity.onGround || (canSwim && isInFluid());
	}
	
	private boolean isInFluid()
	{
		return theEntity.isInWater() || theEntity.handleWaterMovement();
	}
	
	private void removeSunnyPath()
	{
		if(!worldObj.canBlockSeeSky(new BlockPos(MathHelper.floor(theEntity.posX), (int) (theEntity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(theEntity.posZ))))
		{
			for(int var1 = 0; var1 < currentPath.getCurrentPathLength(); var1++)
			{
				LTPathPoint var2 = currentPath.getPathPointFromIndex(var1);
				if(worldObj.canBlockSeeSky(new BlockPos(var2.x, var2.y, var2.z)))
				{
					currentPath.setCurrentPathLength(var1 - 1);
					return;
				}
			}
		}
	}
	
	private boolean isDirectPathBetweenPoints(Vec3d par1Vec3, Vec3d par2Vec3, int par3, int par4, int par5)
	{
		int var6 = MathHelper.floor((double) par1Vec3.x);
		int var7 = MathHelper.floor((double) par1Vec3.z);
		double var8 = par2Vec3.x - par1Vec3.x;
		double var10 = par2Vec3.z - par1Vec3.z;
		double var12 = var8 * var8 + var10 * var10;
		if(var12 < 1.0E-8)
			return false;
		double var14 = 1.0 / Math.sqrt(var12);
		if(!this.isSafeToStandAt(var6, (int) par1Vec3.y, var7, par3 += 2, par4, par5 += 2, par1Vec3, var8 *= var14, var10 *= var14))
			return false;
		par3 -= 2;
		par5 -= 2;
		double var16 = 1.0 / Math.abs(var8);
		double var18 = 1.0 / Math.abs(var10);
		double var20 = (double) (var6 * 1) - par1Vec3.x;
		double var22 = (double) (var7 * 1) - par1Vec3.z;
		if(var8 >= 0.0)
		{
			var20 += 1.0;
		}
		if(var10 >= 0.0)
		{
			var22 += 1.0;
		}
		var20 /= var8;
		var22 /= var10;
		int var24 = var8 < 0.0 ? -1 : 1;
		int var25 = var10 < 0.0 ? -1 : 1;
		int var26 = MathHelper.floor((double) par2Vec3.x);
		int var27 = MathHelper.floor((double) par2Vec3.z);
		int var28 = var26 - var6;
		int var29 = var27 - var7;
		do
		{
			if(var28 * var24 <= 0 && var29 * var25 <= 0)
			{
				return true;
			}
			if(var20 < var22)
			{
				var20 += var16;
				var28 = var26 - (var6 += var24);
				continue;
			}
			var22 += var18;
			var29 = var27 - (var7 += var25);
		} while(this.isSafeToStandAt(var6, (int) par1Vec3.y, var7, par3, par4, par5, par1Vec3, var8, var10));
		return false;
	}
	
	private boolean isSafeToStandAt(int par1, int par2, int par3, int par4, int par5, int par6, Vec3d par7Vec3, double par8, double par10)
	{
		int var12 = par1 - par4 / 2;
		int var13 = par3 - par6 / 2;
		if(!this.isPositionClear(var12, par2, var13, par4, par5, par6, par7Vec3, par8, par10))
			return false;
		for(int var14 = var12; var14 < var12 + par4; ++var14)
		{
			for(int var15 = var13; var15 < var13 + par6; ++var15)
			{
				EnumBlockRenderType rt;
				double var16 = (double) var14 + 0.5 - par7Vec3.x;
				double var18 = (double) var15 + 0.5 - par7Vec3.z;
				if(var16 * par8 + var18 * par10 < 0.0)
					continue;
				IBlockState par19 = this.worldObj.getBlockState(new BlockPos(var14, par2, var15));
				Block var20 = par19.getBlock();
				if(var20 != Blocks.AIR && ((rt = var20.getRenderType(null)) == EnumBlockRenderType.LIQUID || rt == EnumBlockRenderType.INVISIBLE))
					return false;
				par19 = worldObj.getBlockState(new BlockPos(var14, par2 - 1, var15));
				var20 = par19.getBlock();
				if(var20 == Blocks.AIR)
					return false;
				Material var21 = var20.getMaterial(par19);
				if(var21 == Material.WATER && !this.theEntity.isInWater())
					return false;
				if(var21 != Material.LAVA)
					continue;
				return false;
			}
		}
		return true;
	}
	
	private boolean isPositionClear(int par1, int par2, int par3, int par4, int par5, int par6, Vec3d par7Vec3, double par8, double par10)
	{
		for(int var12 = par1; var12 < par1 + par4; ++var12)
			for(int var13 = par2; var13 < par2 + par5; ++var13)
				for(int var14 = par3; var14 < par3 + par6; ++var14)
				{
					Block var19;
					double var15 = (double) var12 + 0.5 - par7Vec3.x;
					double var17 = (double) var14 + 0.5 - par7Vec3.z;
					if(var15 * par8 + var17 * par10 < 0.0 || (var19 = this.worldObj.getBlockState(new BlockPos(var12, var13, var14)).getBlock()) == Blocks.AIR || var19.isPassable(worldObj, new BlockPos(var12, var13, var14)))
						continue;
					return false;
				}
		return true;
	}
}