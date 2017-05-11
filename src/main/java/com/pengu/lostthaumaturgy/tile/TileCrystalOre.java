package com.pengu.lostthaumaturgy.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.tile.TileSyncable;

public class TileCrystalOre extends TileSyncable
{
	public short orientation = 1;
	public short crystals = 2;
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setShort("Orientation", orientation);
		nbt.setShort("Crystals", crystals);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		orientation = nbt.getShort("Orientation");
		crystals = nbt.getShort("Crystals");
	}
	
	public static int suggestOrientationForWorldGen(World world, BlockPos pos)
	{
		List<EnumFacing> fs = new ArrayList<>();
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos p = pos.offset(facing);
			IBlockState state = world.getBlockState(p);
			if(!state.equals(Blocks.STONE.getStateFromMeta(0))) continue;
			fs.add(facing);
		}
		
		return fs.size() > 0 ? fs.get(world.rand.nextInt(fs.size())).getOpposite().ordinal() : -1;
	}
}