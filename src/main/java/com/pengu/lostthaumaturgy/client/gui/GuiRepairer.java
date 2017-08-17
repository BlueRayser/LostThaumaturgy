package com.pengu.lostthaumaturgy.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.inventory.ContainerRepairer;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.tile.TileRepairer;

public class GuiRepairer extends GuiContainer
{
	public static final ResourceLocation tex = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_repairer.png");
	public final TileRepairer tile;
	
	public GuiRepairer(TileRepairer tile, EntityPlayer player)
	{
		super(new ContainerRepairer(tile, player));
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
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		try
		{
			if(tile.getUpgrades()[0] >= 0)
			{
				int x = 130;
				int y = 54;
				ItemStack stack = new ItemStack(ItemUpgrade.byId(tile.getUpgrades()[0]));
				itemRender.renderItemAndEffectIntoGUI(stack, x, y);
				if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
					drawHoveringText(stack.getTooltip(mc.player, TooltipFlags.NORMAL), mouseX, mouseY);
			}
		} catch(Throwable err)
		{
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(tex);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if(tile.boost > 0)
		{
			float i1 = (.1F + (float) tile.boost / 2F) * 6;
			RenderUtil.drawTexturedModalRect(guiLeft + 135, guiTop + 46 - i1, xSize, 30 - i1, mouseY, i1);
		}
	}
}