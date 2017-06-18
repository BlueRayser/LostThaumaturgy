package com.pengu.lostthaumaturgy.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.tile.ITileDroppable;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.net.utils.NetPropertyItemStack;
import com.pengu.hammercore.net.utils.NetPropertyString;
import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.api.seal.SealCombination;
import com.pengu.lostthaumaturgy.api.seal.SealManager;

public class TileSeal extends TileSyncableTickable implements ITileDroppable
{
	public final NetPropertyItemStack stack;
	private final NetPropertyString[] slots = new NetPropertyString[3];
	public SealCombination combination;
	
	public boolean dirty = false;
	
	{
		stack = new NetPropertyItemStack(this, ItemStack.EMPTY);
		slots[0] = new NetPropertyString(this, null);
		slots[1] = new NetPropertyString(this, null);
		slots[2] = new NetPropertyString(this, null);
	}
	
	public ItemSealSymbol getSymbol(int index)
	{
		String v = slots[index].get();
		if(v == null)
			return null;
		return WorldUtil.cast(Item.REGISTRY.getObject(new ResourceLocation(v)), ItemSealSymbol.class);
	}
	
	public void setSymbol(int index, ItemSealSymbol symbol)
	{
		if(symbol == null)
			slots[index].set(null);
		else
			slots[index].set(symbol.getRegistryName().toString());
		dirty = true;
	}
	
	@Override
	public void tick()
	{
		if(dirty)
		{
			combination = SealManager.getCombination(this);
			dirty = false;
		}
		
		if(combination != null)
		{
			combination.update(this);
			if(!combination.isValid(this))
			{
				combination = null;
				dirty = true;
			}
		} else if(atTickRate(20))
			combination = SealManager.getCombination(this);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		dirty = true;
	}
	
	@Override
	public void markDirty()
	{
		dirty = true;
		super.markDirty();
	}
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		if(!world.isRemote)
		{
			EntityItem ent = new EntityItem(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack.get().copy());
			double mod = .10000000149011612;
			ent.motionX = (player.posX - ent.posX) * mod;
			ent.motionY = (player.posY - ent.posY) * mod;
			ent.motionZ = (player.posZ - ent.posZ) * mod;
			world.spawnEntity(ent);
		}
	}
}