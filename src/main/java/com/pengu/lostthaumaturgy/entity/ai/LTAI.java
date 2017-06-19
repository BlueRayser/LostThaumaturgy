package com.pengu.lostthaumaturgy.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;

public class LTAI
{
	public static LTPathEntity getPathEntityToEntity(Entity par1Entity, Entity par2Entity, float par3, boolean par4, boolean par5, boolean par6, boolean par7)
	{
		par1Entity.world.profiler.startSection("pathfind");
		int var8 = MathHelper.floor((double) par1Entity.posX);
		int var9 = MathHelper.floor((double) (par1Entity.posY + 1.0));
		int var10 = MathHelper.floor((double) par1Entity.posZ);
		int var11 = (int) (par3 + 16.0f);
		int var12 = var8 - var11;
		int var13 = var9 - var11;
		int var14 = var10 - var11;
		int var15 = var8 + var11;
		int var16 = var9 + var11;
		int var17 = var10 + var11;
		ChunkCache var18 = new ChunkCache(par1Entity.world, new BlockPos(var12, var13, var14), new BlockPos(var15, var16, var17), 0);
		LTPathEntity var19 = new LTPathFinder((IBlockAccess) var18, par4, par5, par6, par7).createEntityPathTo(par1Entity, par2Entity, par3);
		par1Entity.world.profiler.endSection();
		return var19;
	}
	
	public static LTPathEntity getEntityPathToXYZ(Entity ent, int targetX, int targetY, int par4, float par5, boolean par6, boolean par7, boolean par8, boolean par9)
	{
		ent.world.profiler.startSection("pathfind");
		int var10 = MathHelper.floor((double) ent.posX);
		int var11 = MathHelper.floor((double) ent.posY);
		int var12 = MathHelper.floor((double) ent.posZ);
		int var13 = (int) (par5 + 8.0f);
		int var14 = var10 - var13;
		int var15 = var11 - var13;
		int var16 = var12 - var13;
		int var17 = var10 + var13;
		int var18 = var11 + var13;
		int var19 = var12 + var13;
		ChunkCache var20 = new ChunkCache(ent.world, new BlockPos(var14, var15, var16), new BlockPos(var17, var18, var19), 0);
		LTPathEntity var21 = new LTPathFinder((IBlockAccess) var20, par6, par7, par8, par9).createEntityPathTo(ent, targetX, targetY, par4, par5);
		ent.world.profiler.endSection();
		return var21;
	}
}
