package com.pengu.lostthaumaturgy.client.gui;

import java.util.Arrays;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.math.MathHelper;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.tile.TileStudiumTable;
import com.pengu.lostthaumaturgy.inventory.ContainerStudiumTable;

public class GuiStudiumTable extends GuiContainer
{
	public ResourceLocation tableTexture = new ResourceLocation(Info.MOD_ID, "textures/gui/studium_table.png");
	
	public final TileStudiumTable table;
	public final EntityPlayer player;
	
	public GuiStudiumTable(TileStudiumTable table, EntityPlayer player)
	{
		super(new ContainerStudiumTable(table, player));
		xSize = 176;
		ySize = 170;
		this.table = table;
		this.player = player;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if(mouseX >= 58 && mouseY >= 38 && mouseX < 58 + 35 && mouseY < 38 + 10)
			drawHoveringText(Arrays.asList("Boost: " + table.lastBoost), mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(tableTexture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		RenderUtil.drawTexturedModalRect(guiLeft + 52, guiTop + 52, xSize, 0, table.researchProgress * 50, 10);
		RenderUtil.drawTexturedModalRect(guiLeft + 72, guiTop + 41, xSize, 10, MathHelper.clip(table.lastBoost, 0, 50) / 50 * 20, 4);
	}
}