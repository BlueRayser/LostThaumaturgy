package com.pengu.lostthaumaturgy.client.gui;

import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.asm.WorldHooks;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.inventory.ContainerVisCondenser;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.tile.TileVisCondenser;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiVisCondenser extends GuiContainer
{
	public final ResourceLocation gui = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_condenser.png");
	public final TileVisCondenser tile;
	
	public GuiVisCondenser(EntityPlayer player, TileVisCondenser tile)
	{
		super(new ContainerVisCondenser(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if(tile.getUpgrades()[1] >= 0)
		{
			int x = 104;
			int y = 56;
			ItemStack stack = new ItemStack(ItemUpgrade.byId(tile.getUpgrades()[1]));
			itemRender.renderItemAndEffectIntoGUI(stack, x, y);
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
				drawHoveringText(stack.getTooltip(mc.player, false), mouseX, mouseY);
		}
		
		if(tile.getUpgrades()[0] >= 0)
		{
			int x = 56;
			int y = 56;
			ItemStack stack = new ItemStack(ItemUpgrade.byId(tile.getUpgrades()[0]));
			itemRender.renderItemAndEffectIntoGUI(stack, x, y);
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
				drawHoveringText(stack.getTooltip(mc.player, false), mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawTexturedModalRect(guiLeft + 84, guiTop + 68, 196, tile.getWorld().getMoonPhase() * 8, 8, 8);
		
		switch(tile.currentType)
		{
		case 1:
		{
			Color.glColourRGB(0xFFEE00);
			break;
		}
		case 2:
		{
			Color.glColourRGB(0x361CFF);
			break;
		}
		case 3:
		{
			Color.glColourRGB(0x1CFF1C);
			break;
		}
		case 4:
		{
			Color.glColourRGB(0xFF1C1C);
			break;
		}
		case 5:
		{
			Color.glColourRGB(0x440B6B);
			break;
		}
		default:
		{
			Color.glColourRGB(0xAE00FF);
			break;
		}
		}
		
		double k1 = 36D * tile.degredation / 4550D;
		RenderUtil.drawTexturedModalRect(guiLeft + 78, guiTop + 58 - k1, 176, 35 - k1, 20, k1);
		Color.glColourRGB(0xFFFFFF);
	}
}