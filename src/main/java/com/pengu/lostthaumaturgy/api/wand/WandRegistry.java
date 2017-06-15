package com.pengu.lostthaumaturgy.api.wand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.pengu.hammercore.utils.IndexedMap;
import com.pengu.lostthaumaturgy.items.ItemWand;

public enum WandRegistry
{
	;
	
	private static final IndexedMap<String, WandCap> CAPS = new IndexedMap<>();
	private static final IndexedMap<String, WandRod> RODS = new IndexedMap<>();
	private static final List<ItemStack> creativeItems = new ArrayList<>();
	
	public static void registerCreativeSubitem(WandRod rod, WandCap cap)
	{
		ItemStack stack = ItemWand.makeWand(rod, cap, cap);
		ItemWand.setVis(stack, ItemWand.getMaxVis(stack));
		ItemWand.setTaint(stack, ItemWand.getMaxTaint(stack));
		creativeItems.add(stack);
	}
	
	public static void addSubitems(NonNullList<ItemStack> creativeTabItems)
	{
		// Old, non-lambda code
		// for(ItemStack i : creativeItems)
		// if(!i.isEmpty())
		// creativeTabItems.add(i.copy());
		
		creativeItems.stream().filter(i -> !i.isEmpty()).forEach(i -> creativeTabItems.add(i.copy()));
	}
	
	public static void registerCap(WandCap cap)
	{
		if(CAPS.containsKey(cap.id))
			throw new IllegalArgumentException("Wand cap with id \"" + cap.id + "\" (" + cap.getClass().getName() + ") was already registered!");
		CAPS.put(cap.id, cap);
	}
	
	public static void registerRod(WandRod rod)
	{
		if(RODS.containsKey(rod.id))
			throw new IllegalArgumentException("Wand rod with id \"" + rod.id + "\" (" + rod.getClass().getName() + ") was already registered!");
		RODS.put(rod.id, rod);
	}
	
	public static WandCap getCap(String id)
	{
		return CAPS.get(id);
	}
	
	public static WandRod getRod(String id)
	{
		return RODS.get(id);
	}
	
	public static Stream<WandCap> getCaps()
	{
		return CAPS.values().stream();
	}
	
	public static Stream<WandRod> getRods()
	{
		return RODS.values().stream();
	}
}