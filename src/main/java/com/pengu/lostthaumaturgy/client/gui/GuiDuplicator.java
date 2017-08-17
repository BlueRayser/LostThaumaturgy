package com.pengu.lostthaumaturgy.client.gui;

import java.io.IOException;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.color.Color;
import com.pengu.hammercore.net.pkt.PacketSetProperty;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.inventory.ContainerDuplicator;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.tile.TileDuplicator;

public class GuiDuplicator extends GuiContainer
{
	public final TileDuplicator tile;
	
	public GuiDuplicator(TileDuplicator tile, EntityPlayer player)
	{
		super(new ContainerDuplicator(tile, player));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	public final ResourceLocation gui = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_duplicator.png");
	
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
		
		if(tile.getUpgrades()[0] >= 0)
		{
			int x = 152;
			int y = 60;
			ItemStack stack = new ItemStack(ItemUpgrade.byId(tile.getUpgrades()[0]));
			itemRender.renderItemAndEffectIntoGUI(stack, x, y);
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
				drawHoveringText(stack.getTooltip(mc.player, TooltipFlags.NORMAL), mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(tile.duplicatorCopyTime > 0F)
		{
			float i1 = tile.duplicatorCopyTime / tile.currentItemCopyCost * 24;
			RenderUtil.drawTexturedModalRect(guiLeft + 55, guiTop + 41, 176, 10, i1, 3);
		}
		
		if(tile.boost > 0)
		{
			float i1 = (.1F + (float) tile.boost / 2F) * 6;
			RenderUtil.drawTexturedModalRect(guiLeft + 157, guiTop + 45 - i1, 176, 43 - i1, 7, i1);
		}
		
		Color.glColourRGBA(0xFFFFFFFF);
		drawTexturedModalRect(guiLeft + 62, guiTop + 48, 176 + (!tile.repeat.get() ? 0 : 10), 0, 10, 10);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(mouseX >= guiLeft + 62 && mouseY >= guiTop + 48 && mouseX < guiLeft + 72 && mouseY < guiTop + 58)
		{
			tile.repeat.set(tile.repeat.get() != Boolean.TRUE);
			PacketSetProperty.toServer(tile, tile.repeat);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1F));
		}
	}
}