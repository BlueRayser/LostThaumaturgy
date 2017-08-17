package com.pengu.lostthaumaturgy.items;

import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.tile.TileSeal;

public class DefaultSealSymbol extends ItemSealSymbol
{
	final ResourceLocation tex1, tex2, tex3;
	final int tex3color;
	
	public DefaultSealSymbol(String name, int tex, int tex3color)
	{
		setUnlocalizedName(name);
		this.tex1 = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/seals/s_0_" + tex + ".png");
		this.tex2 = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/seals/s_1_" + tex + ".png");
		this.tex3 = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/seals/s_2_x.png");
		this.tex3color = tex3color;
	}
	
	@Override
	public boolean doesRotate(TileSeal seal, int index)
	{
		return index == 2 || (getUnlocalizedName().contains("runic_essence_air") && index == 1);
	}
	
	@Override
	public ResourceLocation getTexture(TileSeal seal, int index)
	{
		return index == 2 ? tex3 : index == 1 ? tex2 : tex1;
	}
	
	@Override
	public int getColorMultiplier(TileSeal seal, int index)
	{
		return index == 2 ? tex3color : 0xFFFFFF;
	}
}