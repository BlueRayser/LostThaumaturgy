package com.pengu.lostthaumaturgy.events;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.api.wand.WandRegistry;
import com.pengu.lostthaumaturgy.api.wand.WandRod;

public class TooltipEvent
{
	@SubscribeEvent
	public void addTooltip(ItemTooltipEvent evt)
	{
		ItemStack stack = evt.getItemStack();
		List<String> tooltip = evt.getToolTip();
		
		if(LTConfigs.enableCrucibleValueTooltips)
		{
			float value = RecipesCrucible.getSmeltingValue(stack);
			
			if(value > 0)
				tooltip.add("[LT] Crucible Smelting Value: " + value + " Vis.");
		}
		
		WandCap cap = WandRegistry.selectCap(stack);
		if(cap != null)
		{
			tooltip.add("[LT] Usable wand cap.");
			tooltip.add("[LT] Vis Discount: " + TextFormatting.LIGHT_PURPLE + (Math.round(-cap.getUseCost())) + "%" + TextFormatting.RESET);
		}
		
		WandRod rod = WandRegistry.selectRod(stack);
		if(rod != null)
		{
			tooltip.add("[LT] Usable wand rod.");
			tooltip.add("[LT] Base Vis Cost: " + TextFormatting.LIGHT_PURPLE + (Math.round(rod.getBaseCost())) + "%" + TextFormatting.RESET);
		}
	}
}