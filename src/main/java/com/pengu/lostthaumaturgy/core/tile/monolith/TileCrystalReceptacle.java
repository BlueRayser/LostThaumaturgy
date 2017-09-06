package com.pengu.lostthaumaturgy.core.tile.monolith;

import java.security.SecureRandom;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

import com.pengu.hammercore.net.utils.NetPropertyBool;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.hammercore.tile.TileSyncable;
import com.pengu.lostthaumaturgy.core.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.init.BlocksLT;

public class TileCrystalReceptacle extends TileSyncable
{
	public final NetPropertyNumber<Integer> EXPECTED_CRYSTAL;
	public final NetPropertyBool INSERTED;
	public final Random rand = new Random(0L);
	
	public long msSinceChange = 0L;
	public boolean lastInserted = false;
	
	{
		EXPECTED_CRYSTAL = new NetPropertyNumber<Integer>(this, new SecureRandom().nextInt(6));
		INSERTED = new NetPropertyBool(this, false);
	}
	
	public BlockOreCrystal getOre()
	{
		int cr = EXPECTED_CRYSTAL.get();
		
		switch(cr)
		{
		case 0:
			return (BlockOreCrystal) BlocksLT.CRYSTAL_ORE_VAPOROUS;
		case 1:
			return (BlockOreCrystal) BlocksLT.CRYSTAL_ORE_AQUEOUS;
		case 2:
			return (BlockOreCrystal) BlocksLT.CRYSTAL_ORE_EARTHEN;
		case 3:
			return (BlockOreCrystal) BlocksLT.CRYSTAL_ORE_FIERY;
		case 4:
			return (BlockOreCrystal) BlocksLT.CRYSTAL_ORE_VIS;
		case 5:
			return (BlockOreCrystal) BlocksLT.CRYSTAL_ORE_TAINTED;
		default:
			return null;
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
	}
}