package com.pengu.lostthaumaturgy.api.items;

import net.minecraft.entity.player.EntityPlayer;

public interface IGoggles
{
	/**
	 * Return a number between 1 and 3.
	 * <br> 0 = show pure vis only
	 * <br> 1 = show tainted vis only
	 * <br> 3 = show advanced info (vis, taint, TCB, chunk atmosphere level charge and potential)
	 */
	public int getRevealType();
	
	public boolean canReveal(EntityPlayer player);
}