package com.pengu.lostthaumaturgy.api.seal;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.tile.TileSeal;

public abstract class ItemSealSymbol extends Item
{
	{
		setMaxStackSize(16);
	}
	
	public abstract ResourceLocation getTexture(TileSeal seal, int index);
	
	public abstract int getColorMultiplier(TileSeal seal, int index);
	
	public abstract boolean doesRotate(TileSeal seal, int index);
	
	/**
	 * Used to invoke a static method via reflection. <br>
	 * Format: com.package.RenderClass.methodName
	 */
	public String getRender(TileSeal seal, int index)
	{
		return "com.pengu.lostthaumaturgy.client.render.seal.LTSealRenders.renderStandart";
	}
}