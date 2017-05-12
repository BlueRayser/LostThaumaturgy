package com.pengu.lostthaumaturgy.client.gui;

import java.util.Arrays;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.RecipesInfuser;
import com.pengu.lostthaumaturgy.api.tiles.IInfuser;
import com.pengu.lostthaumaturgy.custom.research.ResearchPredicate;
import com.pengu.lostthaumaturgy.inventory.ContainerInfuser;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;
import com.pengu.lostthaumaturgy.tile.TileInfuser;

public class GuiInfuser extends GuiContainer
{
	private TileInfuser infuserInventory;
	
	public GuiInfuser(InventoryPlayer inventoryplayer, TileInfuser tileInfuser)
	{
		super(new ContainerInfuser(inventoryplayer, tileInfuser));
		this.infuserInventory = tileInfuser;
		xSize = 176;
		ySize = 240;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRendererObj.drawString("Infuser", 8, 5, 4210752);
		
		try
		{
			mouseX -= guiLeft;
			mouseY -= guiTop;
			
			Predicate<IInfuser> predicate = RecipesInfuser.getPredicate(infuserInventory.entry);
			
			if(predicate instanceof ResearchPredicate)
			{
				ResearchPredicate pred = (ResearchPredicate) predicate;
				ItemStack[] stacks = pred.getResearchItems(EnumResearchItemType.DISCOVERY);
				int xStart = 80 - stacks.length / 2;
				for(int i = 0; i < stacks.length; ++i)
				{
					GL11.glPushMatrix();
					GL11.glTranslated(0, .5, 0);
					itemRender.renderItemAndEffectIntoGUI(stacks[i], xStart + i * 16, 36);
					GL11.glPopMatrix();
					if(mouseX >= xStart + i * 16 && mouseY >= 36 && mouseX < xStart + i * 16 + 16 && mouseY < 36 + 16)
						drawHoveringText(Arrays.asList(ItemResearch.getFromStack(stacks[i]).getTitle()), mouseX, mouseY);
				}
			}
		}catch(Throwable err) {}
	}
	
	private ResourceLocation gui_infuser = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_infuser.png");
	private ResourceLocation upgrade_icons = new ResourceLocation(LTInfo.MOD_ID, "textures/misc/upgrade_icons.png");
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		mc.getTextureManager().bindTexture(gui_infuser);
		RenderUtil.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(this.infuserInventory.isCooking())
		{
			float i1 = this.infuserInventory.getCookProgressScaled(46);
			RenderUtil.drawTexturedModalRect(guiLeft + 160, guiTop + 151 - i1, 176, 46 - i1, 9, i1);
		}
		
		if(this.infuserInventory.boost > 0)
		{
			float i1 = this.infuserInventory.getBoostScaled();
			RenderUtil.drawTexturedModalRect(guiLeft + 161, guiTop + 38 - i1, 192, 30 - i1, 7, i1);
		}
		
		mc.getTextureManager().bindTexture(upgrade_icons);
		if(this.infuserInventory.getUpgrades()[0] >= 0)
			RenderUtil.drawTexturedModalRect(guiLeft + 8, guiTop + 128, 16 * this.infuserInventory.getUpgrades()[0], 0, 16, 16);
	}
}