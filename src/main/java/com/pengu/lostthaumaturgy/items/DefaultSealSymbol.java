package com.pengu.lostthaumaturgy.items;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.tile.TileSeal;

public class DefaultSealSymbol extends ItemSealSymbol
{
	final ResourceLocation tex1, tex2, tex3;
	final int tex3color, texIndex;
	
	public DefaultSealSymbol(String name, int tex, int tex3color)
	{
		setUnlocalizedName(name);
		this.tex1 = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/seals/s_0_" + tex + ".png");
		this.tex2 = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/seals/s_1_" + tex + ".png");
		this.tex3 = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/seals/s_2_x.png");
		this.tex3color = tex3color;
		this.texIndex = tex;
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
		/** RAINBOW!!! */
		if(this.texIndex == 7)
		{
			float r = 1;
			float g = 1;
			float b = 1;
			
			long msPerCycle = 4000L;
			long time = (Minecraft.getSystemTime() + seal.getPos().hashCode() * 346L + index * 2356632L) % msPerCycle;
			long msPerSector = msPerCycle / 3L;
			int currentSector = MathHelper.floor((double) time / (double) msPerSector) + 1;
			long sectorCenter = currentSector * msPerSector;
			float sectorProgression = (time % msPerSector) / (float) msPerSector;
			float sectorDepth = sectorProgression * 2F;
			if(sectorDepth > 1)
				sectorDepth = 2 - sectorDepth;
			
			if(currentSector == 1)
			{
				b = 1 - sectorProgression;
				r = 1;
				g = sectorProgression;
			} else if(currentSector == 2)
			{
				r = 1 - sectorProgression;
				g = 1;
				b = sectorProgression;
			} else if(currentSector == 3)
			{
				g = 1 - sectorProgression;
				b = 1;
				r = sectorProgression;
			}
			
			return ColorHelper.packRGB(r, g, b);
		}
		
		return index == 2 ? tex3color : 0xFFFFFF;
	}
}