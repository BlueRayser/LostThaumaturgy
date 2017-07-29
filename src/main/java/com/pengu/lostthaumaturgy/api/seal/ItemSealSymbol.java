package com.pengu.lostthaumaturgy.api.seal;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@SideOnly(Side.CLIENT)
	public boolean renderSymbol(TileSeal seal, double x, double y, double z, float partialTicks, int index)
	{
		Method render = null;
		
		try
		{
			String patz = getRender(seal, index);
			int i = patz.lastIndexOf(".");
			String claz = patz.substring(0, i);
			String meth = patz.substring(i + 1, patz.length());
			render = Class.forName(claz).getMethod(meth, TileSeal.class, double.class, double.class, double.class, float.class, int.class);
			render.setAccessible(true);
			if(!Modifier.isStatic(render.getModifiers()))
				return false;
		} catch(Throwable err)
		{
			return false;
		}
		
		try
		{
			render.invoke(null, seal, x, y, z, partialTicks, index);
			return true;
		} catch(Throwable err)
		{
		}
		
		return false;
	}
}