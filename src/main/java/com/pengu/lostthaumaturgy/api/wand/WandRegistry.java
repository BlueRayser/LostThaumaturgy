package com.pengu.lostthaumaturgy.api.wand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

import com.pengu.hammercore.utils.IndexedMap;
import com.pengu.lostthaumaturgy.items.ItemWand;

/**
 * Used to register wand rods and cores to the registry.<br>
 * Please register everything until post-init!
 */
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
	
	public static WandCap selectCap(ItemStack stack)
	{
		Iterator<WandCap> sels = CAPS.values().stream().filter(i -> i != null && i.isValid(stack)).iterator();
		if(sels.hasNext())
			return sels.next();
		return null;
	}
	
	public static WandRod selectRod(ItemStack stack)
	{
		Iterator<WandRod> sels = RODS.values().stream().filter(i -> i != null && i.isValid(stack)).iterator();
		if(sels.hasNext())
			return sels.next();
		return null;
	}
	
	public static void registerCap(WandCap cap)
	{
		if(Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION))
			throw new RuntimeException("Attempted to register Wand Cap (" + cap.id + ") too late!");
		
		if(CAPS.containsKey(cap.id))
			throw new IllegalArgumentException("Wand cap with id \"" + cap.id + "\" (" + cap.getClass().getName() + ") was already registered!");
		CAPS.put(cap.id, cap);
	}
	
	public static void registerRod(WandRod rod)
	{
		if(Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION))
			throw new RuntimeException("Attempted to register Wand Rod (" + rod.id + ") too late!");
		
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