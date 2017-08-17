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
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.entity.EntityTravelingTrunk;
import com.pengu.lostthaumaturgy.inventory.ContainerTravelingTrunk;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.net.PacketTrunkToggleSit;

public class GuiTravelingTrunk extends GuiContainer
{
	public GuiTravelingTrunk(EntityTravelingTrunk trunk, EntityPlayer player)
	{
		super(new ContainerTravelingTrunk(trunk, player));
		xSize = 176;
		ySize = 192;
	}
	
	public final ResourceLocation back = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_trunk_base.png");
	public final ResourceLocation slots = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_trunk_slots.png");
	
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
		fontRenderer.drawString("Trunk", 8, 6, 1052688);
		fontRenderer.drawString("Inventory", 8, 96, 1052688);
		
		EntityTravelingTrunk trunk = ((ContainerTravelingTrunk) inventorySlots).trunk;
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if(trunk.getUpgrades()[1] >= 0)
		{
			int x = 120;
			int y = 91;
			ItemStack stack = new ItemStack(ItemUpgrade.byId(trunk.getUpgrades()[1]));
			itemRender.renderItemAndEffectIntoGUI(stack, x, y);
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
				drawHoveringText(stack.getTooltip(mc.player, TooltipFlags.NORMAL), mouseX, mouseY);
		}
		
		if(trunk.getUpgrades()[0] >= 0)
		{
			int x = 98;
			int y = 91;
			ItemStack stack = new ItemStack(ItemUpgrade.byId(trunk.getUpgrades()[0]));
			itemRender.renderItemAndEffectIntoGUI(stack, x, y);
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
				drawHoveringText(stack.getTooltip(mc.player, TooltipFlags.NORMAL), mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		EntityTravelingTrunk trunk = ((ContainerTravelingTrunk) inventorySlots).trunk;
		
		mc.getTextureManager().bindTexture(back);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawTexturedModalRect(guiLeft + 159, guiTop + 88, xSize + (trunk.stay ? 10 : 0), 0, 10, 10);
		RenderUtil.drawTexturedModalRect(guiLeft + 130, guiTop + 6, 176, 16, 39 * trunk.getHealth() / trunk.getMaxHealth(), 6);
		
		mc.getTextureManager().bindTexture(slots);
		drawTexturedModalRect(guiLeft + 7, guiTop + 16, 7, 16, 161, 2 * trunk.inventory.getSizeInventory());
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseX >= guiLeft + 159 && mouseY >= guiTop + 88 && mouseX < guiLeft + 169 && mouseY < guiTop + 98)
		{
			HCNetwork.manager.sendToServer(new PacketTrunkToggleSit(((ContainerTravelingTrunk) inventorySlots).trunk.getUniqueID()));
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_WOOD_STEP, 1F));
		}
	}
}