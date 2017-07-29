package com.pengu.lostthaumaturgy.client.render.seal;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.client.TextureAtlasSpriteFull;
import com.pengu.lostthaumaturgy.tile.TileSeal;

@SideOnly(Side.CLIENT)
public class LTSealRenders
{
	public static void renderStandart(TileSeal seal, double x, double y, double z, float partialTicks, int index)
	{
		ItemSealSymbol symb = seal.getSymbol(index);
		int color = symb.getColorMultiplier(seal, index);
		boolean rotates = symb.doesRotate(seal, index);
		ResourceLocation tex = symb.getTexture(seal, index);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
		SimpleBlockRendering sbr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
		sbr.begin();
		sbr.setSprite(TextureAtlasSpriteFull.sprite);
		sbr.drawBlock(x, y, z);
		sbr.end();
	}
	
	public static void renderNone(TileSeal seal, double x, double y, double z, float partialTicks)
	{
		
	}
}