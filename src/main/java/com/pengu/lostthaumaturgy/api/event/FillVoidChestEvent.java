package com.pengu.lostthaumaturgy.api.event;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.tile.TileVoidChest;

public class FillVoidChestEvent extends BlockEvent
{
	public final TileVoidChest chest;
	
	public FillVoidChestEvent(World world, BlockPos pos, TileVoidChest tile)
	{
		super(world, pos, world.getBlockState(pos));
		this.chest = tile;
		
		int a;
		int b;
		HashMap<Integer, ItemStack> loot = new HashMap<Integer, ItemStack>();
		int count = 0;
		boolean added = false;
		
		for(a = 0; a < 100; ++a)
		{
			for(b = 0; b < 2; ++b)
			{
				EnumMultiMaterialType type = b == 0 ? EnumMultiMaterialType.SHARD_STRANGE_METAL : EnumMultiMaterialType.ELDRITCH_MECHANISM;
				loot.put(count, type.stack());
				++count;
			}
		}
		
		for(a = 0; a < 33; ++a)
		{
			for(b = 2; b < 4; ++b)
			{
				EnumMultiMaterialType type = b == 2 ? EnumMultiMaterialType.OPALESCENT_EYE : EnumMultiMaterialType.DISTURBING_MIRROR;
				loot.put(count, type.stack());
				++count;
			}
		}
		
		for(a = 0; a < 11; ++a)
		{
			loot.put(count, EnumMultiMaterialType.GLOWING_ELDRITCH_DEVICE.stack());
			++count;
		}
		
		for(a = 0; a < 5; ++a)
		{
			loot.put(count, EnumMultiMaterialType.ELDRITCH_REPOSITORY.stack());
			++count;
		}
		
		for(a = 0; a < 17; ++a)
		{
			loot.put(count, EnumMultiMaterialType.DARKNESS_SEED.stack(1 + world.rand.nextInt(2)));
			++count;
		}
		
		for(a = 0; a < 17; ++a)
		{
			loot.put(count, EnumMultiMaterialType.VOID_INGOT.stack(1 + world.rand.nextInt(2)));
			++count;
		}
		
		for(a = 0; a < 25; ++a)
		{
			loot.put(count, new ItemStack(BlocksLT.ELDRITCH_BLOCK, 1 + world.rand.nextInt(4)));
			++count;
		}
		
		for(int a2 = 0; a2 < chest.getSizeInventory(); ++a2)
		{
			if(!chest.getStackInSlot(a2).isEmpty())
				continue;
			int chance = world.rand.nextInt(count * 6);
			chest.setInventorySlotContents(a2, loot.get(chance));
		}
	}
}