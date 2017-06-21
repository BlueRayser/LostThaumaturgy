package com.pengu.lostthaumaturgy.tile;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.util.BlockSnapshot;

import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.net.utils.NetPropertyAbstract;
import com.pengu.hammercore.net.utils.NetPropertyBool;
import com.pengu.hammercore.net.utils.NetPropertyNBT;

public class TileTaintedSoil extends TileSyncable
{
	public final NetPropertyNBT<NBTTagCompound> BLOCK_SNAPSHOT;
	public final NetPropertyBool DROP_CONGEALED_TAINT;
	protected String tainted;
	
	public TileTaintedSoil()
	{
		BLOCK_SNAPSHOT = new NetPropertyNBT<NBTTagCompound>(this);
		DROP_CONGEALED_TAINT = new NetPropertyBool(this, false);
	}
	
	public TileTaintedSoil(BlockSnapshot snapshot)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		snapshot.writeToNBT(nbt);
		BLOCK_SNAPSHOT = new NetPropertyNBT<NBTTagCompound>(this, nbt);
		DROP_CONGEALED_TAINT = new NetPropertyBool(this, false);
	}
	
	public BlockSnapshot getSnapshot()
	{
		NBTTagCompound nbt = BLOCK_SNAPSHOT.get();
		if(nbt != null)
			return BlockSnapshot.readFromNBT(nbt);
		return null;
	}
	
	public void setSnapshot(BlockSnapshot snapshot)
	{
		if(world != null && world.isRemote)
			return;
		NBTTagCompound nbt = new NBTTagCompound();
		snapshot.writeToNBT(nbt);
		BLOCK_SNAPSHOT.set(nbt);
		DROP_CONGEALED_TAINT.set(snapshot.getReplacedBlock().getBlock() instanceof BlockOre);
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("tainted", tainted);
		properties.put("drop_congealed", DROP_CONGEALED_TAINT.get());
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void notifyOfChange(NetPropertyAbstract prop)
	{
		BlockSnapshot snap = getSnapshot();
		Block block = snap.getReplacedBlock().getBlock();
		tainted = block.getLocalizedName();
		if(Item.getItemFromBlock(block) != null)
			tainted = new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(snap.getReplacedBlock())).getDisplayName();
	}
}