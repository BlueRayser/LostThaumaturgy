package com.pengu.lostthaumaturgy.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.mrdimka.hammercore.gui.container.ContainerEmpty;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.tile.TileGenerator;

public class GuiGenerator extends GuiContainer
{
	public final TileGenerator tile;
	public final ResourceLocation texture = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_generator.png");
	
	public GuiGenerator(TileGenerator tile)
	{
		super(new ContainerEmpty());
		this.tile = tile;
		xSize = 102;
		ySize = 82;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if(tile.getUpgrades()[0] >= 0)
		{
			int x = 19;
			int y = 25;
			ItemStack stack = new ItemStack(ItemUpgrade.byId(tile.getUpgrades()[0]));
			itemRender.renderItemAndEffectIntoGUI(stack, x, y);
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
				drawHoveringText(stack.getTooltip(mc.player, false), mouseX - guiLeft, mouseY - guiTop);
		}
		
		if(tile.getUpgrades()[1] >= 0)
		{
			int x = 19;
			int y = 47;
			ItemStack stack = new ItemStack(ItemUpgrade.byId(tile.getUpgrades()[1]));
			itemRender.renderItemAndEffectIntoGUI(stack, x, y);
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
				drawHoveringText(stack.getTooltip(mc.player, false), mouseX - guiLeft, mouseY - guiTop);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		double fill = (tile.storedEnergy / (float) tile.energyMax) * 47;
		RenderUtil.drawTexturedModalRect(guiLeft + 47, guiTop + 69 - fill, 102, 47 - fill, 9, fill);
		
		int phase = tile.getWorld().provider.getMoonPhase(tile.getWorld().getTotalWorldTime());
		drawTexturedModalRect(guiLeft + 71, guiTop + 41, 111, phase * 8, 8, 8);
	}
}