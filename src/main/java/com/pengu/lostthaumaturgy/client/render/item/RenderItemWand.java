package com.pengu.lostthaumaturgy.client.render.item;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.render.item.IItemRender;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.wand.EnumCapLocation;
import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.api.wand.WandRod;
import com.pengu.lostthaumaturgy.items.ItemWand;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class RenderItemWand implements IItemRender
{
	@Override
	public void renderItem(ItemStack stack)
	{
		SimpleBlockRendering sbr = RenderBlocks.forMod(LTInfo.MOD_ID).simpleRenderer;
		
		WandRod wr = ItemWand.getRod(stack);
		WandCap wc1 = ItemWand.getCap(stack, EnumCapLocation.DOWN);
		WandCap wc2 = ItemWand.getCap(stack, EnumCapLocation.UP);
		
		TextureAtlasSprite rod = ClientProxy.getSprite(wr != null ? wr.getRodTexture() : "minecraft:missing");
		TextureAtlasSprite cap1 = ClientProxy.getSprite(wc1 != null ? wc1.getCapTexture() : "minecraft:missing");
		TextureAtlasSprite cap2 = ClientProxy.getSprite(wc2 != null ? wc2.getCapTexture() : "minecraft:missing");
		
		GL11.glPushMatrix();
		GL11.glTranslated(-.4, -.2, 0);
		GL11.glRotated(45, 0, 1, 0);
		GL11.glRotated(45, -1, 0, 0);
		GL11.glTranslated(-.3, -.8, .7);
		GL11.glScaled(1.2, 1.2, 1.2);
		sbr.begin();
		sbr.setBrightness(sbr.rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition()));
		sbr.setSprite(rod);
		sbr.setRenderBounds(7 / 16D, 0, 7 / 16D, 9 / 16D, 1, 9 / 16D);
		sbr.drawBlock(0, 0, 0);
		sbr.end();
		
		GL11.glPushMatrix();
		GL11.glScaled(.5, .5, .5);
		
		sbr.begin();
		sbr.setBrightness(sbr.rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition()));
		
		sbr.setSprite(cap2);
		
		sbr.setRenderBounds(5 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);
		sbr.drawBlock(.5, 1.6, .5);
		
		sbr.setSprite(cap1);
		
		sbr.setRenderBounds(5 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D, 11 / 16D);
		sbr.drawBlock(.5, -.6, .5);
		
		sbr.end();
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}