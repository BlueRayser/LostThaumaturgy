package com.pengu.lostthaumaturgy.api.restorer;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.items.IRepairable;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;

public class DefaultRepairGetter implements ICustomRepairable
{
	@Override
	public float getVisCost(ItemStack stack, WorldLocation loc)
	{
		if(stack.getItem() instanceof IRepairable)
			return ((IRepairable) stack.getItem()).getRestoreCost(stack, loc);
		if(stack.getItem() instanceof ItemTool)
		{
			ItemTool tool = (ItemTool) stack.getItem();
			ToolMaterial mat = tool.getToolMaterial();
			if(mat == ToolMaterial.WOOD)
				return .5F;
			if(mat == ToolMaterial.STONE)
				return 1;
			if(mat == ToolMaterial.IRON || mat == ToolMaterial.GOLD)
				return 2;
			if(mat == ItemMaterialsLT.tool_thaumium)
				return 3;
			if(mat == ToolMaterial.DIAMOND)
				return 4;
			if(mat == ItemMaterialsLT.tool_elemental)
				return 12;
		}
		if(stack.getItem() instanceof ItemArmor)
		{
			ItemArmor arm = (ItemArmor) stack.getItem();
			ArmorMaterial mat = arm.getArmorMaterial();
			if(mat == ArmorMaterial.LEATHER)
				return .5F;
			if(mat == ArmorMaterial.CHAIN)
				return 1;
			if(mat == ArmorMaterial.IRON || mat == ArmorMaterial.GOLD)
				return 2;
			if(mat == ItemMaterialsLT.armor_thaumium)
				return 3;
			if(mat == ArmorMaterial.DIAMOND)
				return 4;
			if(mat == ItemMaterialsLT.armor_goggles || mat == ItemMaterialsLT.armor_boots_striding)
				return 6;
			if(mat == ItemMaterialsLT.armor_boots_seven)
				return 7;
			if(mat == ItemMaterialsLT.armor_void || mat == ItemMaterialsLT.armor_boots_meteor)
				return 8;
		}
		return 8;
	}
	
	@Override
	public boolean attempRepair(ItemStack stack, WorldLocation loc)
	{
		if(!stack.getItem().isDamageable())
			return false;
		return ICustomRepairable.super.attempRepair(stack, loc);
	}
	
	@Override
	public boolean canRepair(ItemStack stack, WorldLocation loc)
	{
		if(!stack.getItem().isDamageable())
			return false;
		return ICustomRepairable.super.canRepair(stack, loc);
	}
}