package com.pengu.lostthaumaturgy.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.inventory.ContainerWandConstructor;
import com.pengu.lostthaumaturgy.tile.TileWandConstructor;

public class GuiWandConstructor extends GuiContainer
{
	public final ContainerWandConstructor cont;
	public final ResourceLocation gui = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_wand_constructor.png");
	
	public GuiWandConstructor(TileWandConstructor tile, InventoryPlayer player)
	{
		super(new ContainerWandConstructor(tile, player));
		cont = (ContainerWandConstructor) inventorySlots;
		
		xSize = 176;
		ySize = 170;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		
		mc.getTextureManager().bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		Color.glColourARGB(0xFFFFFFFF);
		
		if(cont.tile.totalCost.get() == 0)
			return;
		
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft + 79, guiTop + 32, 0);
		
		double rot = 45 + (Math.sin((System.currentTimeMillis() % 20000L) / 20000D * 90D) * 12 - 6);
		double progress = cont.tile.currentVis.get() / cont.tile.totalCost.get() * 2D;
		if(progress > 1)
			progress = 2 - progress;
		
		GL11.glTranslated(13.5, 12, 0);
		GL11.glRotated(rot, 0, 0, 1);
		GL11.glTranslated(-12, -12, 0);
		
		Color.glColourARGB(Color.packARGB(230, 230, 230, (int) (progress * 255D)));
		drawTexturedModalRect(0, 0, xSize, 0, 24, 24);
		
		float fill = (cont.tile.currentVis.get() / cont.tile.totalCost.get()) * 24F;
		
		Color.glColourARGB(Color.packARGB(223, 45, 255, (int) (progress * 255D)));
		RenderUtil.drawTexturedModalRect(0, 24 - fill, xSize, 24 - fill, 24, fill);
		
		Color.glColourARGB(0xFFFFFFFF);
		GL11.glPopMatrix();
	}
}