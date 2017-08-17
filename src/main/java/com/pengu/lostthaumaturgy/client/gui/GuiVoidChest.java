package com.pengu.lostthaumaturgy.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.inventory.ContainerVoidChest;
import com.pengu.lostthaumaturgy.tile.TileVoidChest;

public class GuiVoidChest extends GuiContainer
{
	public final ResourceLocation background = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_void_chest.png");
	
	public GuiVoidChest(TileVoidChest tile, EntityPlayer player)
	{
		super(new ContainerVoidChest(tile, player));
		xSize = 176;
		ySize = 240;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
	    super.drawScreen(mouseX, mouseY, partialTicks);
	    renderHoveredToolTip(mouseX, mouseY);
	}
}