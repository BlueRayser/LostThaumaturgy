package com.pengu.lostthaumaturgy.custom.aura;

import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class SIAuraChunk implements Serializable, INBTSerializable<NBTTagCompound>
{
	public short vis;
	public short taint;
	public short previousVis;
	public short previousTaint;
	public short goodVibes;
	public short badVibes;
	public short boost;
	public int x;
	public int z;
	public boolean updated;
	public int dimension;
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setShort("vis", vis);
		nbt.setShort("taint", taint);
		nbt.setShort("previousVis", previousVis);
		nbt.setShort("previousTaint", previousTaint);
		nbt.setShort("goodVibes", goodVibes);
		nbt.setShort("badVibes", badVibes);
		nbt.setShort("boost", boost);
		nbt.setInteger("x", x);
		nbt.setInteger("z", z);
		nbt.setBoolean("updated", updated);
		nbt.setInteger("dimension", dimension);
	    return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		vis = nbt.getShort("vis");
		taint = nbt.getShort("taint");
		previousVis = nbt.getShort("previousVis");
		previousTaint = nbt.getShort("previousTaint");
		goodVibes = nbt.getShort("goodVibes");
		badVibes = nbt.getShort("badVibes");
		boost = nbt.getShort("boost");
		x = nbt.getInteger("x");
		z = nbt.getInteger("z");
		updated = nbt.getBoolean("updated");
		dimension = nbt.getInteger("dimension");
	}
	
	@Override
	public int hashCode()
	{
		ArrayList list = new ArrayList<>();
		list.add(x);
		list.add(z);
		list.add(dimension);
	    return list.hashCode();
	}
}