package com.pengu.lostthaumaturgy.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemWand;
import com.pengu.lostthaumaturgy.core.tile.TileFuser;
import com.pengu.lostthaumaturgy.inventory.ContainerFuser;

public class GuiFuser extends GuiContainer
{
	public final ResourceLocation gui = new ResourceLocation(Info.MOD_ID, "textures/gui/gui_fuser.png");
	public final TileFuser tile;
	public final ContainerFuser containerFuser;
	
	public GuiFuser(TileFuser tile, InventoryPlayer player)
	{
		super(new ContainerFuser(tile, player));
		containerFuser = (ContainerFuser) inventorySlots;
		this.tile = tile;
		
		xSize = 176;
		ySize = 188;
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
		if(isMouseOverSlot(containerFuser.output, mouseX, mouseY))
		{
			GL11.glPushMatrix();
			GL11.glTranslated(128, 34, 0);
			GlStateManager.disableDepth();
			drawRect(0, 0, 24, 24, -2130706433);
			GlStateManager.enableDepth();
			GL11.glPopMatrix();
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		Color.glColourARGB(0xFFFFFFFF);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		mc.getTextureManager().bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(!tile.inventory.outputInv.getStackInSlot(0).isEmpty())
		{
			IFuserRecipe rec = tile.inventory.findRecipe(mc.player);
			
			ItemStack wand = tile.inventory.wandInv.getStackInSlot(0);
			float cost = !wand.isEmpty() && wand.getItem() instanceof ItemWand ? ItemWand.getVisUsage(wand) : 12000F;
			float vis = !wand.isEmpty() && wand.getItem() instanceof ItemWand ? ItemWand.getVis(wand) : 0;
			float taint = !wand.isEmpty() && wand.getItem() instanceof ItemWand ? ItemWand.getTaint(wand) : 0;
			
			if(rec != null && (vis < rec.getVisUsage() * cost || taint < rec.getTaintUsage() * cost))
			{
				Color.glColourRGB(0xFFCCCC);
				drawTexturedModalRect(guiLeft + 129, guiTop + 68, 129, 68, 22, 3);
				drawTexturedModalRect(guiLeft + 129, guiTop + 87, 129, 87, 22, 3);
				drawTexturedModalRect(guiLeft + 129, guiTop + 71, 129, 71, 3, 16);
				drawTexturedModalRect(guiLeft + 148, guiTop + 71, 148, 71, 3, 16);
				Color.glColourRGB(0xFFFFFF);
			}
			
			itemRender.zLevel = 50;
			zLevel = 50;
			
			GL11.glPushMatrix();
			GL11.glTranslated(guiLeft + 128, guiTop + 34, 0);
			GL11.glScaled(1.5D, 1.5D, 1.5D);
			itemRender.renderItemAndEffectIntoGUI(tile.inventory.outputInv.getStackInSlot(0), 0, 0);
			GL11.glPopMatrix();
			
			itemRender.zLevel = 0;
			zLevel = 0;
		}
	}
	
	@Override
	public boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY)
	{
		if(slotIn == containerFuser.output)
		{
			slotIn.xPos = 132;
			slotIn.yPos = 38;
			
			boolean over = isPointInRegion(slotIn.xPos - 3, slotIn.yPos - 3, 22, 22, mouseX, mouseY);
			
			slotIn.xPos = -guiLeft - 32;
			slotIn.yPos = -guiTop - 32;
			
			return over;
		}
		return isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
	}
}