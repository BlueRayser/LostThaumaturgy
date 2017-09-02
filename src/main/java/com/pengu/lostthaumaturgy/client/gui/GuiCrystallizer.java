package com.pengu.lostthaumaturgy.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.core.tile.TileCrystallizer;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.inventory.ContainerCrystallizer;

public class GuiCrystallizer extends GuiContainer
{
	public final ContainerCrystallizer container;
	public final TileCrystallizer tile;
	
	public static final ResourceLocation[] backgrounds = { new ResourceLocation(Info.MOD_ID, "textures/gui/gui_crystalizer_5.png"), new ResourceLocation(Info.MOD_ID, "textures/gui/gui_crystalizer_6.png") };
	
	public GuiCrystallizer(TileCrystallizer tile, EntityPlayer player)
	{
		super(new ContainerCrystallizer(tile, player));
		container = (ContainerCrystallizer) inventorySlots;
		this.tile = tile;
		
		xSize = 176;
		ySize = 240;
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
		boolean evil = tile.hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL));
		
		if(!evil)
			evil = !tile.getStackInSlot(5).isEmpty();
		
		container.darkSlot.xPos = evil ? 80 : -guiLeft - 18;
		container.darkSlot.yPos = evil ? 129 : -guiTop - 18;
		
		mc.getTextureManager().bindTexture(backgrounds[evil ? 1 : 0]);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(tile.isCooking())
		{
			double p = tile.crystalTime.get() / tile.maxTime * 46F;
			RenderUtil.drawTexturedModalRect(guiLeft + 160, guiTop + 151 - p, 176, 46 - p, 9, p);
		}
		
		if(tile.boost > 0)
		{
			double p = .1F + tile.boost / 2 * 6F;
			RenderUtil.drawTexturedModalRect(guiLeft + 161, guiTop + 38 - p, 185, 30 - p, 7, p);
		}
		
		if(tile.getUpgrades()[0] >= 0)
		{
			int x = 8;
			int y = 128;
			ItemStack stack = new ItemStack(ItemUpgrade.byId(tile.getUpgrades()[0]));
			itemRender.renderItemAndEffectIntoGUI(stack, guiLeft + x, guiTop + y);
			if(mouseX >= guiLeft + x && mouseX < guiLeft + x + 16 && mouseY >= guiTop + y && mouseY < guiTop + y + 16)
				drawHoveringText(stack.getTooltip(mc.player, TooltipFlags.NORMAL), mouseX, mouseY);
		}
	}
}