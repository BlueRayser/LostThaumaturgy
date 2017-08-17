package com.pengu.lostthaumaturgy.tile;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.asm.WorldHooks;
import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.client.gui.GuiDarkGenerator;
import com.pengu.lostthaumaturgy.inventory.ContainerDarkGenerator;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.tile.monolith.TileMonolith;

public class TileDarknessGenerator extends TileSyncableTickable
{
	public static final NonNullList<ItemStack> VALID_SEEDS = NonNullList.<ItemStack> create();
	public int zapColor = 0x7700C1;
	public final NetPropertyNumber<Integer> progress;
	public InventoryNonTile inv = new InventoryNonTile(2);
	private BlockPos spos = BlockPos.ORIGIN;
	private boolean valid = false;
	
	{
		progress = new NetPropertyNumber<Integer>(this, 0);
	}
	
	static
	{
		VALID_SEEDS.add(new ItemStack(Items.WHEAT_SEEDS));
		VALID_SEEDS.add(new ItemStack(Items.MELON_SEEDS));
		VALID_SEEDS.add(new ItemStack(Items.PUMPKIN_SEEDS));
		VALID_SEEDS.add(new ItemStack(Items.BEETROOT_SEEDS));
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiDarkGenerator(this, player.inventory);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerDarkGenerator(this, player.inventory);
	}
	
	public static boolean isValidSeed(ItemStack stack)
	{
		for(ItemStack vs : VALID_SEEDS)
			if(vs.isItemEqual(stack))
				return true;
		return false;
	}
	
	@Override
	public void tick()
	{
		if(atTickRate(20))
		{
			spos = pos;
			valid = false;
			block0: for(int a = -5; a <= 5; ++a)
				for(int b = -5; b <= 5; ++b)
					for(int c = -5; c <= 5; ++c)
					{
						TileEntity te;
						if(pos.getY() + b < 0 || (te = world.getTileEntity(pos.add(a, b, c))) == null || !(te instanceof TileMonolith))
							continue;
						int count = 1;
						while((te = world.getTileEntity(pos.add(a, b - count, c))) != null && te instanceof TileMonolith)
							;
						this.valid = true;
						spos = pos.add(a, b - count + 1, c);
						break block0;
					}
		}
		
		if(valid && !inv.getStackInSlot(0).isEmpty() && isValidSeed(inv.getStackInSlot(0)))
		{
			int moon = WorldHooks.getMoonPhase(world) * 4;
			int light = world.getLight(pos);
			int penalty = moon + light;
			progress.set(progress.get() + (32 - penalty));
			if(world.rand.nextInt(50 + penalty) == 0)
				HammerCore.particleProxy.spawnSlowZap(world, new Vec3d(spos.getX() + .5, spos.getY() + .75 + world.rand.nextFloat() * 4, spos.getZ() + 0.5), new Vec3d(pos.getX() + .5, pos.getY() + .75, pos.getZ() + .5), zapColor, 25, .3F);
		} else
			progress.set(0);
		if(valid && progress.get() >= 90000 && !inv.getStackInSlot(0).isEmpty() && isValidSeed(inv.getStackInSlot(0)))
		{
			if(!inv.getStackInSlot(1).isEmpty() && inv.getStackInSlot(1).isItemEqual(EnumMultiMaterialType.DARKNESS_SEED.stack()))
			{
				if(inv.getStackInSlot(1).getCount() >= inv.getInventoryStackLimit())
				{
					progress.set(89999);
					return;
				}
				inv.getStackInSlot(1).grow(1);
			} else if(inv.getStackInSlot(1).isEmpty())
				inv.setInventorySlotContents(1, EnumMultiMaterialType.DARKNESS_SEED.stack());
			inv.getStackInSlot(0).shrink(1);
			progress.set(0);
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagCompound items = new NBTTagCompound();
		inv.writeToNBT(items);
		nbt.setTag("Items", items);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		inv.readFromNBT(nbt.getCompoundTag("Items"));
	}
}