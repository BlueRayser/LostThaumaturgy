package com.pengu.lostthaumaturgy.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.mrdimka.hammercore.tile.ITileDroppable;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;
import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.api.wand.WandRegistry;
import com.pengu.lostthaumaturgy.api.wand.WandRod;
import com.pengu.lostthaumaturgy.client.gui.GuiWandConstructor;
import com.pengu.lostthaumaturgy.inventory.ContainerWandConstructor;
import com.pengu.lostthaumaturgy.items.ItemWand;

public class TileWandConstructor extends TileVisUser implements ITileDroppable
{
	public final InventoryNonTile inventory = new InventoryNonTile(4);
	public final NetPropertyNumber<Integer> totalCost;
	public final NetPropertyNumber<Float> currentVis;
	
	{
		totalCost = new NetPropertyNumber<Integer>(this, 0);
		currentVis = new NetPropertyNumber<Float>(this, 0F);
		inventory.inventoryStackLimit = 1;
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiWandConstructor(this, player.inventory);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerWandConstructor(this, player.inventory);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		WandCap up = WandRegistry.selectCap(inventory.getStackInSlot(0));
		WandCap down = WandRegistry.selectCap(inventory.getStackInSlot(1));
		WandRod rod = WandRegistry.selectRod(inventory.getStackInSlot(2));
		
		boolean canCraft = up != null && down != null && rod != null && inventory.getStackInSlot(3).isEmpty();
		
		if(canCraft)
		{
			int cost = 45 + (up.getCraftCost() + down.getCraftCost()) * 2 + rod.getCraftCost() * 3;
			totalCost.set(cost);
			
			if(currentVis.get() < cost)
			{
				float maxAccept = cost - currentVis.get();
				float sucked;
				currentVis.set(currentVis.get() + (sucked = getAvailablePureVis(Math.min(maxAccept, 1))));
				
				if(atTickRate(40) && sucked > 0F && !world.isRemote)
					HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":tinkering", pos, 1F, 1F, SoundCategory.BLOCKS);
				
				if(currentVis.get() >= cost)
				{
					totalCost.set(0);
					currentVis.set(0F);
					for(int i = 0; i < inventory.getSizeInventory() - 1; ++i)
						inventory.getStackInSlot(i).shrink(1);
					inventory.setInventorySlotContents(3, ItemWand.makeWand(rod, up, down));
				}
			}
		} else
		{
			float c = currentVis.get();
			totalCost.set(0);
			currentVis.set(0F);
			if(c > 0F)
				HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":fizz", pos, 1F, 1F, SoundCategory.BLOCKS);
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tag = new NBTTagCompound();
		inventory.writeToNBT(tag);
		nbt.setTag("Items", tag);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
	}
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		inventory.drop(world, pos);
	}
}