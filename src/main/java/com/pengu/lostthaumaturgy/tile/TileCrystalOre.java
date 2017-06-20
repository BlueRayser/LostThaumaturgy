package com.pengu.lostthaumaturgy.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.lostthaumaturgy.LostThaumaturgy;

public class TileCrystalOre extends TileSyncable
{
	public final NetPropertyNumber<Short> orientation;
	public final NetPropertyNumber<Short> crystals;
	
	{
		orientation = new NetPropertyNumber<Short>(this, (short) 1);
		crystals = new NetPropertyNumber<Short>(this, (short) 2);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Orientation"))
		{
			orientation.set(nbt.getShort("Orientation"));
			LostThaumaturgy.LOG.warn("TileEntity " + this + " tried to load old NBT Key: \"Orientation\"=" + orientation.get() + ". It is going to be refactored to properties!");
		}
		
		if(nbt.hasKey("Crystals"))
		{
			crystals.set(nbt.getShort("Crystals"));
			LostThaumaturgy.LOG.warn("TileEntity " + this + " tried to load old NBT Key: \"Crystals\"=" + crystals.get() + ". It is going to be refactored to properties!");
		}
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("facing", EnumFacing.VALUES[orientation.get()]);
		properties.put("crystals", crystals.get());
	}
	
	public static int suggestOrientationForWorldGen(World world, BlockPos pos)
	{
		List<EnumFacing> fs = new ArrayList<>();
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos p = pos.offset(facing);
			IBlockState state = world.getBlockState(p);
			if(!state.equals(Blocks.STONE.getStateFromMeta(0)))
				continue;
			fs.add(facing);
		}
		
		return fs.size() > 0 ? fs.get(world.rand.nextInt(fs.size())).getOpposite().ordinal() : -1;
	}
	
	private double renderDistance = 64;
	private long lastCheck = 0L;
	
	@Override
	public double getMaxRenderDistanceSquared()
	{
		if(System.currentTimeMillis() - lastCheck < 0)
			lastCheck = 0;
		if(System.currentTimeMillis() - lastCheck >= 1000L)
		{
			int solids = 0;
			for(EnumFacing f : EnumFacing.VALUES)
			{
				BlockPos tpos = pos.offset(f);
				if(!world.isBlockLoaded(tpos))
					solids++;
				else if(world.getBlockState(tpos).isSideSolid(world, tpos, f.getOpposite()))
					solids++;
			}
			renderDistance = solids == 6 ? 8 : 64;
			lastCheck = System.currentTimeMillis();
		}
		
		return renderDistance * renderDistance;
	}
}