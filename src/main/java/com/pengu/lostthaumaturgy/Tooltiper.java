package com.pengu.lostthaumaturgy;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mrdimka.hammercore.annotations.MCFBus;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;

@MCFBus
public class Tooltiper
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
	}
}