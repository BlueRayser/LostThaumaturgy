package com.pengu.lostthaumaturgy.client.gui;

import java.util.Arrays;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.mrdimka.hammercore.math.MathHelper;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.client.render.shared.LiquidVisRenderer;
import com.pengu.lostthaumaturgy.inventory.ContainerAuxiliumTable;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileAuxiliumTable;

public class GuiAuxiliumTable extends GuiContainer
{
	public ResourceLocation tableTexture = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/auxilium_table.png");
	
	public final TileAuxiliumTable table;
	public final EntityPlayer player;
	
	public GuiAuxiliumTable(TileAuxiliumTable table, EntityPlayer player)
	{
		super(new ContainerAuxiliumTable(table, player));
		xSize = 176;
		ySize = 170;
		this.table = table;
		this.player = player;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		int fragmentCount = 9;
		if(table.lastBoost > 0)
			fragmentCount = Math.round((27 - table.lastBoost) / 3F);
		if(fragmentCount == 0)
			fragmentCount = 1;
		
		if(mouseX >= 71 && mouseY >= 39 && mouseX < 71 + 35 && mouseY < 39 + 10)
			drawHoveringText(Arrays.asList("Required Fragment Amount:", fragmentCount + ""), mouseX, mouseY);
		
		if(mouseX >= 41 && mouseY >= 31 && mouseX < 41 + 19 && mouseY < 31 + 35)
			drawHoveringText(Arrays.asList("Vis: " + LostThaumaturgy.standartDecimalFormat.format(table.visConsumed) + "/32.0"), mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(tableTexture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		RenderUtil.drawTexturedModalRect(guiLeft + 63, guiTop + 52, xSize, 0, table.progress * 50, 10);
		RenderUtil.drawTexturedModalRect(guiLeft + 85, guiTop + 42, xSize, 10, (1 - table.lastBoost / 27F) * 20, 4);
		
		GlStateManager.enableBlend();
		
		double fill = MathHelper.clip(table.visConsumed, 0, 32);
		if(LiquidVisRenderer.useShaders())
		{
			LiquidVisRenderer.renderIntoGui(guiLeft + 43, guiTop + 65 - fill, 16, fill, 1);
		} else
		{
			TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			
			RenderUtil.drawTexturedModalRect(guiLeft + 43, guiTop + 65 - Math.min(fill, 16), vis, 16, Math.min(fill, 16));
			if(fill > 16)
				RenderUtil.drawTexturedModalRect(guiLeft + 43, guiTop + 65 - fill, vis, 16, fill - 16);
		}
	}
}