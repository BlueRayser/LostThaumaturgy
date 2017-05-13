package com.pengu.lostthaumaturgy.custom.aura.taint;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class TaintHandlerLostThaumaturgy implements ITaintHandler
{
	@Override
    public boolean canTaintBlock(World world, BlockPos pos)
    {
		if(!world.isBlockLoaded(pos)) return false;
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT)
			return true;
		if(state.getBlock() == BlocksLT.SHIMMERLEAF)
			return true;
		if(state.getBlock() instanceof BlockOreCrystal)
			return true;
		
	    return false;
    }

	@Override
    public boolean taintBlock(World world, BlockPos pos)
    {
		if(!world.isBlockLoaded(pos)) return false;
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT)
		{
			world.setBlockState(pos, Blocks.DIRT.getStateFromMeta(1));
			return true;
		}
		
		if(state.getBlock() == BlocksLT.SHIMMERLEAF)
		{
			world.setBlockState(pos, BlocksLT.TAINTEDLEAF.getDefaultState());
			return true;
		}
		
		if(state.getBlock() instanceof BlockOreCrystal)
		{
			world.setBlockState(pos, BlocksLT.CRYSTAL_ORE_TAINTED.getDefaultState());
		}
		
//		if(ta >= 0 && (isAdjacentTo(x + xx, y + yy, z + zz, 0) || isAdjacentToOpenBlock(x + xx, y + yy, z + zz)))
//		{
//			world.d(x + xx, y + yy, z + zz, mod_ThaumCraft.blockTaint.bO, ta);
//			badSound(world, (float) x + (float) xx, (float) y + (float) yy, (float) z + (float) zz);
//			return true;
//		}
//		
//		if(k3 == mod_ThaumCraft.blockOreCrystals.bO)
//		{
//			world.f(x + xx, y + yy, z + zz, 5);
//			badSound(world, (float) x + (float) xx, (float) y + (float) yy, (float) z + (float) zz);
//			return true;
//		}
//		
//		if(k3 == pb.J.bO || k3 == mod_ThaumCraft.blockCustomWood.bO && m3 >= 5 && m3 <= 9)
//		{
//			world.d(x + xx, y + yy, z + zz, mod_ThaumCraft.blockCustomWood.bO, 1);
//			world.a(x + xx, y + yy, z + zz, (kw) new TileMemory(k3, m3, 0, null));
//			badSound(world, (float) x + (float) xx, (float) y + (float) yy, (float) z + (float) zz);
//			return true;
//		}
//		
//		if((k3 == pb.K.bO || k3 == mod_ThaumCraft.blockCustomLeaves.bO && (m3 & 3) == 0) && isNear(x + xx, y + yy, z + zz, 2, mod_ThaumCraft.blockCustomWood.bO))
//		{
//			world.d(x + xx, y + yy, z + zz, mod_ThaumCraft.blockCustomLeaves.bO, 2);
//			world.a(x + xx, y + yy, z + zz, (kw) new TileMemory(k3, m3, 0, null));
//			badSound(world, (float) x + (float) xx, (float) y + (float) yy, (float) z + (float) zz);
//			return true;
//		}
//		
//		if((float) taint > (float) TAConfigs.auraMax * 0.66f && world.r.nextInt(50) == 1 && k3 == mod_ThaumCraft.blockTaint.bO && m3 < 10 && world.i(x + xx, y + yy + 1, z + zz) && !isNear(x + xx, y + yy + 1, z + zz, 10, new int[] { mod_ThaumCraft.blockTaint.bO }, new int[] { 10 }))
//		{
//			world.d(x + xx, y + yy + 1, z + zz, mod_ThaumCraft.blockTaint.bO, 10);
//			badSound(world, (float) x + (float) xx, (float) y + (float) yy + 1.0f, (float) z + (float) zz);
//			return true;
//		}
//		
//		if(k3 != mod_ThaumCraft.blockTaint.bO || m3 >= 10 || !world.i(x + xx, y + yy + 1, z + zz) || isNear(x + xx, y + yy + 1, z + zz, 3, new int[] { mod_ThaumCraft.blockCustomPlants.bO, mod_ThaumCraft.blockCustomPlants.bO }, new int[] { 2, 4 }))
//			continue;
//		
//		world.d(x + xx, y + yy + 1, z + zz, mod_ThaumCraft.blockCustomPlants.bO, world.r.nextInt(4) == 0 ? 2 : 4);
		
	    return false;
    }

	@Override
    public boolean canCureBlock(World world, BlockPos pos)
    {
		if(!world.isBlockLoaded(pos)) return false;
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() == BlocksLT.CRYSTAL_ORE_TAINTED)
			return true;
		if(state.getBlock() == BlocksLT.TAINTEDLEAF)
			return true;
		if(state.getBlock() == Blocks.DIRT && Blocks.DIRT.getMetaFromState(state) == 1)
			return true;
		
	    return false;
    }

	@Override
    public boolean cureBlock(World world, BlockPos pos)
    {
		if(!world.isBlockLoaded(pos)) return false;
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() == BlocksLT.CRYSTAL_ORE_TAINTED)
			world.setBlockState(pos, new ArrayList<>(BlockOreCrystal.crystals).get(world.rand.nextInt(BlockOreCrystal.crystals.size())).getDefaultState());
		
		if(state.getBlock() == Blocks.DIRT && Blocks.DIRT.getMetaFromState(state) == 1)
			world.setBlockState(pos, world.isAirBlock(pos.up()) ? Blocks.GRASS.getDefaultState() : Blocks.DIRT.getDefaultState());
		
		if(state.getBlock() == BlocksLT.TAINTEDLEAF)
			world.setBlockState(pos, BlocksLT.SHIMMERLEAF.getDefaultState());
		
		// int k3 = world.a(x, y, z);
		// int m3 = world.e(x, y, z);
		// if(k3 == mod_ThaumCraft.blockTaint.bO && m3 < 10)
		// {
		// world.g(x, y, z, restoreTaintBlock(m3));
		// return true;
		// }
		// if(k3 == mod_ThaumCraft.blockTaint.bO && m3 == 10 &&
		// world.r.nextInt(3) == 0 || k3 == mod_ThaumCraft.blockCustomPlants.bO
		// && (m3 == 2 || m3 == 4))
		// {
		// world.g(x, y, z, 0);
		// return true;
		// }
		// if(m3 == 1 && k3 == mod_ThaumCraft.blockCustomWood.bO || (m3 & 3) ==
		// 2 && k3 == mod_ThaumCraft.blockCustomLeaves.bO)
		// {
		// kw te = world.b(x, y, z);
		// if(te != null && te instanceof TileMemory)
		// {
		// world.d(x, y, z, ((TileMemory) te).oldblock, ((TileMemory)
		// te).oldmeta);
		// } else
		// {
		// world.g(x, y, z, 0);
		// }
		// return true;
		// }
		
	    return false;
    }

	@Override
    public String handlerId()
    {
	    return LTInfo.MOD_ID;
    }
}