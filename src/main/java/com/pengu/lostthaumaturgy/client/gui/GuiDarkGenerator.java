package com.pengu.lostthaumaturgy.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.tile.TileDarknessGenerator;
import com.pengu.lostthaumaturgy.inventory.ContainerDarkGenerator;

public class GuiDarkGenerator extends GuiContainer
{
	public final TileDarknessGenerator tile;
	public final ResourceLocation gui = new ResourceLocation(Info.MOD_ID, "textures/gui/gui_dark_gen.png");
	
	public GuiDarkGenerator(TileDarknessGenerator tile, InventoryPlayer player)
	{
		super(new ContainerDarkGenerator(tile, player));
		this.tile = tile;
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
		mc.getTextureManager().bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		float progress = ((float) tile.progress.get() / 90000F) * 64F;
		RenderUtil.drawTexturedModalRect(guiLeft + 56, guiTop + 64, 192, 64, progress, 8);
		
		int light = mc.world.getLight(tile.getPos());
		drawTexturedModalRect(guiLeft + 80, guiTop + 24, 176, light * 16, 16, 16);
		
		int moon = tile.getWorld().getMoonPhase();
		drawTexturedModalRect(guiLeft + 84, guiTop + 44, 192, moon * 8, 8, 8);
	}
}