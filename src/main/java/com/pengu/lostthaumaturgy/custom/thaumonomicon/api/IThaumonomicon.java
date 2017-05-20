package com.pengu.lostthaumaturgy.custom.thaumonomicon.api;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.mrdimka.hammercore.HammerCore;
import com.pengu.lostthaumaturgy.LostThaumaturgy;

public interface IThaumonomicon
{
	/** Holds a usable non-null */
	@Nonnull
	public IThaumonomicon instance = HammerCore.pipelineProxy.createAndPipeDependingOnSide("com.pengu.lostthaumaturgy.custom.thaumonomicon.api.ClientThaumonomicon", "com.pengu.lostthaumaturgy.custom.thaumonomicon.api.DummyThaumonomicon");
	
	void setCategoryItemIcon(ItemStack stack, String category);
}