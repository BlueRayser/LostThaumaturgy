package com.pengu.lostthaumaturgy.init;

import net.minecraft.item.Item;

import com.pengu.lostthaumaturgy.items.ItemCrystallineBell;
import com.pengu.lostthaumaturgy.items.ItemGogglesRevealing;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemThaumonomicon;

public class ItemsLT
{
	public static final ItemMultiMaterial MULTI_MATERIAL = new ItemMultiMaterial();
	public static final ItemGogglesRevealing GOGGLES_OF_REVEALING = new ItemGogglesRevealing();
	public static final Item //
	        CRYSTALLINE_BELL = new ItemCrystallineBell(), //
	        DISCOVERY = new ItemResearch(), //
	        THAUMONOMICON = new ItemThaumonomicon();
}