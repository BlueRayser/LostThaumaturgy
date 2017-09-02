package com.pengu.lostthaumaturgy.core.worldgen;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import com.pengu.hammercore.common.utils.ArrayEntry;
import com.pengu.hammercore.utils.ListUtils;
import com.pengu.hammercore.world.gen.IWorldGenFeature;
import com.pengu.lostthaumaturgy.core.block.BlockLyingItem;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class WorldGenLostArtifacts implements IWorldGenFeature
{
	public static class DropMaker
	{
		private List<ItemStack> equalDrops = new ArrayList<>();
		private Map<Entry<Integer, Integer>, ItemStack> rareDrops = new HashMap<>();
		
		public void include(DropMaker maker)
		{
			equalDrops.addAll(maker.equalDrops);
			rareDrops.putAll(maker.rareDrops);
		}
		
		public void addCommonItem(ItemStack stack)
		{
			equalDrops.add(stack.copy());
			equalDrops = ListUtils.randomizeList(equalDrops, rand);
		}
		
		public void addRareItem(int chance, int maxChance, ItemStack stack)
		{
			rareDrops.put(new ArrayEntry(chance, maxChance), stack.copy());
		}
		
		public ItemStack pickRandom(Random rand)
		{
			ItemStack picked = equalDrops.get(rand.nextInt(equalDrops.size()));
			for(Entry<Integer, Integer> key : rareDrops.keySet())
				if(rand.nextInt(key.getValue()) <= key.getKey())
					picked = rareDrops.get(key);
			return picked.copy();
		}
	}
	
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return 1024;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return 16;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return 64;
	}
	
	public static final DropMaker drops = new DropMaker();
	public static final SecureRandom rand = new SecureRandom();
	
	static
	{
		DropMaker drop = new DropMaker();
		
		EnumMultiMaterialType[] lost_artifacts = { EnumMultiMaterialType.ANCIENT_POTTERY, EnumMultiMaterialType.TARNISHED_CHALICE, EnumMultiMaterialType.WORN_STATUETTE, EnumMultiMaterialType.ANCIENT_SEAL, EnumMultiMaterialType.ANCIENT_WEAPON };
		for(int i = 0; i < 33; ++i)
		{
			for(EnumMultiMaterialType type : lost_artifacts)
				drop.addCommonItem(type.stack());
		}
		
		EnumMultiMaterialType[] forbidden_artifacts = { EnumMultiMaterialType.CRACKED_WISP_SHELL, EnumMultiMaterialType.DISTORTED_SKULL, EnumMultiMaterialType.INHUMAN_SKULL, EnumMultiMaterialType.DARKENED_CRYSTAL_EYE, EnumMultiMaterialType.KNOTTED_SPIKE };
		for(int i = 0; i < 12; ++i)
		{
			for(EnumMultiMaterialType type : forbidden_artifacts)
				drop.addCommonItem(type.stack());
		}
		
		drop.addRareItem(0, 8, EnumMultiMaterialType.ANCIENT_STONE_TABLET.stack());
		drop.addRareItem(0, 8, EnumMultiMaterialType.TOME_FORBIDDEN_KNOWLEDGE.stack());
		
		addDrop(drop);
	}
	
	public static void addDrop(DropMaker drop)
	{
		drops.include(drop);
	}
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		if(world.getBlockState(pos.down()).getBlock() == Blocks.STONE && world.isAirBlock(pos))
		{
			ItemStack stack = drops.pickRandom(rand);
			BlockLyingItem.place(world, pos, stack);
		}
	}
}