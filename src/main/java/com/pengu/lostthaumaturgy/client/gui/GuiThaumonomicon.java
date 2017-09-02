package com.pengu.lostthaumaturgy.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.api.research.ClientResearchData;
import com.pengu.lostthaumaturgy.api.research.ResearchCategories;
import com.pengu.lostthaumaturgy.api.research.ResearchCategoryList;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.api.research.ResearchManager;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.utils.InventoryUtils;
import com.pengu.lostthaumaturgy.core.utils.UtilsFX;
import com.pengu.lostthaumaturgy.init.SoundEventsLT;

public class GuiThaumonomicon extends GuiScreen
{
	private static int guiMapTop;
	private static int guiMapLeft;
	private static int guiMapBottom;
	private static int guiMapRight;
	protected int paneWidth = 256;
	protected int paneHeight = 230;
	protected int mouseX = 0;
	protected int mouseY = 0;
	protected double field_74117_m;
	protected double field_74115_n;
	protected double guiMapX;
	protected double guiMapY;
	protected double field_74124_q;
	protected double field_74123_r;
	private int isMouseButtonDown = 0;
	public static int lastX = -5;
	public static int lastY = -6;
	private GuiButton button;
	private LinkedList research = new LinkedList();
	public static HashMap completedResearch = new HashMap();
	public static ArrayList highlightedItem = new ArrayList();
	private static String selectedCategory = null;
	private FontRenderer galFontRenderer;
	private ResearchItem currentHighlight = null;
	private String player = "";
	long popuptime = 0L;
	String popupmessage = "";
	
	public GuiThaumonomicon()
	{
		short var2 = 141;
		short var3 = 141;
		this.field_74117_m = this.guiMapX = this.field_74124_q = (double) (lastX * 24 - var2 / 2 - 12);
		this.field_74115_n = this.guiMapY = this.field_74123_r = (double) (lastY * 24 - var3 / 2);
		this.player = Minecraft.getMinecraft().player.getName();
		this.updateResearch();
		this.galFontRenderer = Minecraft.getMinecraft().standardGalacticFontRenderer;
	}
	
	public GuiThaumonomicon(double x, double y)
	{
		this.field_74117_m = this.guiMapX = this.field_74124_q = x;
		this.field_74115_n = this.guiMapY = this.field_74123_r = y;
		this.player = Minecraft.getMinecraft().player.getName();
		this.updateResearch();
		this.galFontRenderer = Minecraft.getMinecraft().standardGalacticFontRenderer;
	}
	
	public void updateResearch()
	{
		if(this.mc == null)
			this.mc = Minecraft.getMinecraft();
		
		this.research.clear();
		if(selectedCategory == null)
		{
			Set col = ResearchCategories.researchCategories.keySet();
			selectedCategory = (String) col.iterator().next();
		}
		
		List<ResearchItem> completed = new ArrayList<>();
		
		Collection<ResearchItem> col1 = ResearchCategories.getResearchList(selectedCategory).research.values();
		Iterator<ResearchItem> i$ = col1.iterator();
		
		while(i$.hasNext())
		{
			ResearchItem res = i$.next();
			this.research.add(res);
			if(ClientResearchData.isResearchCompleted(res))
				completed.add(res);
		}
		
		completedResearch.put(player, completed);
		
		guiMapTop = ResearchCategories.getResearchList(selectedCategory).minDisplayColumn * 24 - 85;
		guiMapLeft = ResearchCategories.getResearchList(selectedCategory).minDisplayRow * 24 - 112;
		guiMapBottom = ResearchCategories.getResearchList(selectedCategory).maxDisplayColumn * 24 - 112;
		guiMapRight = ResearchCategories.getResearchList(selectedCategory).maxDisplayRow * 24 - 61;
	}
	
	@Override
	public void onGuiClosed()
	{
		short var2 = 141;
		short var3 = 141;
		lastX = (int) ((this.guiMapX + (double) (var2 / 2D) + 12.0D) / 24.0D);
		lastY = (int) ((this.guiMapY + (double) (var3 / 2D)) / 24.0D);
		super.onGuiClosed();
	}
	
	@Override
	public void initGui()
	{
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException
	{
		if(par2 == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			highlightedItem.clear();
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
		} else
		{
			if(par2 == 1)
				highlightedItem.clear();
			
			super.keyTyped(par1, par2);
		}
	}
	
	public void updateScreen()
	{
		this.field_74117_m = this.guiMapX;
		this.field_74115_n = this.guiMapY;
		double var1 = this.field_74124_q - this.guiMapX;
		double var3 = this.field_74123_r - this.guiMapY;
		if(var1 * var1 + var3 * var3 < 4.0D)
		{
			this.guiMapX += var1;
			this.guiMapY += var3;
		} else
		{
			this.guiMapX += var1 * 0.85D;
			this.guiMapY += var3 * 0.85D;
		}
	}
	
	protected void genResearchBackground(int par1, int par2, float par3)
	{
		long t = System.nanoTime() / 50000000L;
		int var4 = MathHelper.floor(this.field_74117_m + (this.guiMapX - this.field_74117_m) * (double) par3);
		int var5 = MathHelper.floor(this.field_74115_n + (this.guiMapY - this.field_74115_n) * (double) par3);
		if(var4 < guiMapTop)
		{
			var4 = guiMapTop;
		}
		
		if(var5 < guiMapLeft)
		{
			var5 = guiMapLeft;
		}
		
		if(var4 >= guiMapBottom)
		{
			var4 = guiMapBottom - 1;
		}
		
		if(var5 >= guiMapRight)
		{
			var5 = guiMapRight - 1;
		}
		
		int var8 = (this.width - this.paneWidth) / 2;
		int var9 = (this.height - this.paneHeight) / 2;
		int var10 = var8 + 16;
		int var11 = var9 + 17;
		this.zLevel = 0.0F;
		GL11.glDepthFunc(518);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glEnable(3553);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(2896);
		GL11.glEnable('\u803a');
		GL11.glEnable(2903);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 1.0F);
		int vx = (int) ((float) (var4 - guiMapTop) / (float) Math.abs(guiMapTop - guiMapBottom) * 288.0F);
		int vy = (int) ((float) (var5 - guiMapLeft) / (float) Math.abs(guiMapLeft - guiMapRight) * 316.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(ResearchCategories.getResearchList(selectedCategory).background);
		this.drawTexturedModalRect(var10 / 2, var11 / 2, vx / 2, vy / 2, 112, 98);
		GL11.glScalef(0.5F, 0.5F, 1.0F);
		GL11.glPopMatrix();
		GL11.glEnable(2929);
		GL11.glDepthFunc(515);
		int var24;
		int var26;
		int var27;
		int var42;
		
		if(completedResearch.get(player) != null)
		{
			for(int var22 = 0; var22 < research.size(); ++var22)
			{
				ResearchItem itemRenderer = (ResearchItem) research.get(var22);
				int var25;
				ResearchItem var41;
				boolean cats;
				boolean count;
				if(itemRenderer.parents != null && itemRenderer.parents.length > 0)
				{
					for(var42 = 0; var42 < itemRenderer.parents.length; ++var42)
					{
						if(itemRenderer.parents[var42] != null && ResearchCategories.getResearch(itemRenderer.parents[var42]).category.equals(selectedCategory))
						{
							var41 = ResearchCategories.getResearch(itemRenderer.parents[var42]);
							if(!var41.isVirtual())
							{
								var24 = itemRenderer.displayColumn * 24 - var4 + 11 + var10;
								var25 = itemRenderer.displayRow * 24 - var5 + 11 + var11;
								var26 = var41.displayColumn * 24 - var4 + 11 + var10;
								var27 = var41.displayRow * 24 - var5 + 11 + var11;
								cats = ClientResearchData.isResearchCompleted(itemRenderer);
								count = ClientResearchData.isResearchCompleted(var41);
								boolean var30 = Math.sin((double) (Minecraft.getSystemTime() % 600L) / 600.0D * 3.141592653589793D * 2.0D) > 0.6D ? true : true;
								if(cats)
									drawLine(var24, var25, var26, var27, 0.1F, 0.1F, 0.1F, par3, false);
								else if(!itemRenderer.isLost() && (!itemRenderer.isHidden() && !itemRenderer.isLost() || ((ArrayList) completedResearch.get(this.player)).contains("@" + itemRenderer.key)) && (!itemRenderer.isConcealed() || canUnlockResearch(itemRenderer)))
								{
									if(count)
										drawLine(var24, var25, var26, var27, 0.0F, 1.0F, 0.0F, par3, true);
									else if((!var41.isHidden() && !itemRenderer.isLost() || ((ArrayList) completedResearch.get(this.player)).contains("@" + var41.key)) && (!var41.isConcealed() || canUnlockResearch(var41)))
										drawLine(var24, var25, var26, var27, 0.0F, 0.0F, 1.0F, par3, true);
								}
							}
						}
					}
				}
				
				if(itemRenderer.siblings != null && itemRenderer.siblings.length > 0)
				{
					for(var42 = 0; var42 < itemRenderer.siblings.length; ++var42)
					{
						if(itemRenderer.siblings[var42] != null && ResearchCategories.getResearch(itemRenderer.siblings[var42]).category.equals(selectedCategory))
						{
							var41 = ResearchCategories.getResearch(itemRenderer.siblings[var42]);
							if(!var41.isVirtual() && (var41.parents == null || var41.parents != null && !Arrays.asList(var41.parents).contains(itemRenderer.key)))
							{
								var24 = itemRenderer.displayColumn * 24 - var4 + 11 + var10;
								var25 = itemRenderer.displayRow * 24 - var5 + 11 + var11;
								var26 = var41.displayColumn * 24 - var4 + 11 + var10;
								var27 = var41.displayRow * 24 - var5 + 11 + var11;
								cats = ClientResearchData.isResearchCompleted(itemRenderer);
								count = ClientResearchData.isResearchCompleted(var41);
								if(cats)
									drawLine(var24, var25, var26, var27, 0.1F, 0.1F, 0.2F, par3, false);
								else if(!itemRenderer.isLost() && (!itemRenderer.isHidden() || ((ArrayList) completedResearch.get(this.player)).contains("@" + itemRenderer.key)) && (!itemRenderer.isConcealed() || this.canUnlockResearch(itemRenderer)))
								{
									if(count)
										drawLine(var24, var25, var26, var27, 0.0F, 1.0F, 0.0F, par3, true);
									else if((!var41.isHidden() || ((ArrayList) completedResearch.get(this.player)).contains("@" + var41.key)) && (!var41.isConcealed() || canUnlockResearch(var41)))
										drawLine(var24, var25, var26, var27, 0.0F, 0.0F, 1.0F, par3, true);
								}
							}
						}
					}
				}
			}
		}
		
		this.currentHighlight = null;
		RenderItem var43 = Minecraft.getMinecraft().getRenderItem();
		GL11.glEnable('\u803a');
		GL11.glEnable(2903);
		boolean renderWithColor = true;
		int var44;
		if(completedResearch.get(this.player) != null)
		{
			for(var24 = 0; var24 < this.research.size(); ++var24)
			{
				ResearchItem var45 = (ResearchItem) this.research.get(var24);
				var26 = var45.displayColumn * 24 - var4;
				var27 = var45.displayRow * 24 - var5;
				if(!var45.isVirtual() && var26 >= -24 && var27 >= -24 && var26 <= 224 && var27 <= 196)
				{
					var42 = var10 + var26;
					var44 = var11 + var27;
					float var47;
					if(ClientResearchData.isResearchCompleted(var45))
					{
						var47 = 1.0F;
						GL11.glColor4f(var47, var47, var47, 1.0F);
					} else
					{
						if(!((ArrayList) completedResearch.get(this.player)).contains("@" + var45.key) && (var45.isLost() || var45.isHidden() && !((ArrayList) completedResearch.get(this.player)).contains("@" + var45.key) || var45.isConcealed() && !canUnlockResearch(var45)))
							continue;
						
						if(canUnlockResearch(var45))
						{
							var47 = (float) Math.sin((double) (Minecraft.getSystemTime() % 600L) / 600.0D * 3.141592653589793D * 2.0D) * 0.25F + 0.75F;
							GL11.glColor4f(var47, var47, var47, 1.0F);
						} else
						{
							var47 = 0.3F;
							GL11.glColor4f(var47, var47, var47, 1.0F);
						}
					}
					
					UtilsFX.bindTexture("textures/gui/gui_research.png");
					GL11.glEnable(2884);
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					if(var45.isRound())
						this.drawTexturedModalRect(var42 - 2, var44 - 2, 54, 230, 26, 26);
					else if(var45.isHidden())
					{
						if(!var45.isSecondary())
							drawTexturedModalRect(var42 - 2, var44 - 2, 86, 230, 26, 26);
						else
							drawTexturedModalRect(var42 - 2, var44 - 2, 230, 230, 26, 26);
					} else if(!var45.isSecondary())
						this.drawTexturedModalRect(var42 - 2, var44 - 2, 0, 230, 26, 26);
					else
						this.drawTexturedModalRect(var42 - 2, var44 - 2, 110, 230, 26, 26);
					
					if(var45.isSpecial())
					{
						this.drawTexturedModalRect(var42 - 2, var44 - 2, 26, 230, 26, 26);
					}
					
					if(!canUnlockResearch(var45))
					{
						GL11.glColor4f(0.1F, 0.1F, 0.1F, 1.0F);
						renderWithColor = false;
					}
					
					GL11.glDisable(3042);
					if(highlightedItem.contains(var45.key))
					{
						GL11.glPushMatrix();
						GL11.glEnable(3042);
						GL11.glBlendFunc(770, 771);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						this.mc.renderEngine.bindTexture(com.pengu.hammercore.client.utils.UtilsFX.getMCParticleTexture());
						int var48 = (int) (t % 16L) * 16;
						GL11.glTranslatef((float) (var42 - 5), (float) (var44 - 5), 0.0F);
						RenderUtil.drawTexturedModalRect(0, 0, var48, 80, 16, 16, 0.0D);
						GL11.glDisable(3042);
						GL11.glPopMatrix();
					}
					
					if(var45.icon_item != null)
					{
						GL11.glPushMatrix();
						GL11.glEnable(3042);
						GL11.glBlendFunc(770, 771);
						RenderHelper.enableGUIStandardItemLighting();
						GL11.glDisable(2896);
						GL11.glEnable('\u803a');
						GL11.glEnable(2903);
						GL11.glEnable(2896);
						var43.renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack(var45.icon_item), var42 + 3, var44 + 3);
						GL11.glDisable(2896);
						GL11.glDepthMask(true);
						GL11.glEnable(2929);
						GL11.glDisable(3042);
						GL11.glPopMatrix();
					} else if(var45.icon_resource != null)
					{
						GL11.glPushMatrix();
						GL11.glEnable(3042);
						GL11.glBlendFunc(770, 771);
						mc.renderEngine.bindTexture(var45.icon_resource);
						if(!renderWithColor)
							GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
						UtilsFX.drawTexturedQuadFull(var42 + 3, var44 + 3, (double) this.zLevel);
						GL11.glPopMatrix();
					}
					
					if(!canUnlockResearch(var45))
						renderWithColor = true;
					
					if(par1 >= var10 && par2 >= var11 && par1 < var10 + 224 && par2 < var11 + 196 && par1 >= var42 && par1 <= var42 + 22 && par2 >= var44 && par2 <= var44 + 22)
						this.currentHighlight = var45;
					
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
			}
		}
		
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Set var46 = ResearchCategories.researchCategories.keySet();
		int var49 = 0;
		boolean var50 = false;
		Iterator var34 = var46.iterator();
		
		int warp;
		while(var34.hasNext())
		{
			Object var99 = var34.next();
			ResearchCategoryList fr = ResearchCategories.getResearchList((String) var99);
			if(!((String) var99).equals("ELDRITCH") || ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR"))
			{
				GL11.glPushMatrix();
				if(var49 == 9)
				{
					var49 = 0;
					var50 = true;
				}
				
				int var39 = !var50 ? 0 : 264;
				byte primary = 0;
				warp = var50 ? 14 : 0;
				if(!selectedCategory.equals((String) var99))
				{
					primary = 24;
					warp = var50 ? 6 : 8;
				}
				
				UtilsFX.bindTexture("textures/gui/gui_research.png");
				
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				if(var50)
					this.drawTexturedModalRectReversed(var8 + var39 - 8, var9 + var49 * 24, 176 + primary, 232, 24, 24);
				else
					this.drawTexturedModalRect(var8 - 24 + var39, var9 + var49 * 24, 152 + primary, 232, 24, 24);
				
				if(highlightedItem.contains((String) var99))
				{
					GL11.glPushMatrix();
					this.mc.renderEngine.bindTexture(com.pengu.hammercore.client.utils.UtilsFX.getMCParticleTexture());
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					int ws = (int) (16L * (t % 16L));
					RenderUtil.drawTexturedModalRect(var8 - 27 + warp + var39, var9 - 4 + var49 * 24, ws, 80, 16, 16, -90.0D);
					GL11.glPopMatrix();
				}
				
				GL11.glPushMatrix();
				this.mc.renderEngine.bindTexture(fr.icon);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				UtilsFX.drawTexturedQuadFull(var8 - 19 + warp + var39, var9 + 4 + var49 * 24, -80.0D);
				GL11.glPopMatrix();
				if(!selectedCategory.equals((String) var99))
				{
					UtilsFX.bindTexture("textures/gui/gui_research.png");
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					if(var50)
					{
						this.drawTexturedModalRectReversed(var8 + var39 - 8, var9 + var49 * 24, 224, 232, 24, 24);
					} else
					{
						this.drawTexturedModalRect(var8 - 24 + var39, var9 + var49 * 24, 200, 232, 24, 24);
					}
				}
				
				GL11.glPopMatrix();
				++var49;
			}
		}
		
		UtilsFX.bindTexture("textures/gui/gui_research.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(var8, var9, 0, 0, this.paneWidth, this.paneHeight);
		GL11.glPopMatrix();
		this.zLevel = 0.0F;
		GL11.glDepthFunc(515);
		GL11.glDisable(2929);
		GL11.glEnable(3553);
		
		super.drawScreen(par1, par2, par3);
		
		if(completedResearch.get(this.player) != null && this.currentHighlight != null)
		{
			String var51 = this.currentHighlight.getName();
			var26 = par1 + 6;
			var27 = par2 - 4;
			int var52 = 0;
			FontRenderer var53 = this.fontRenderer;
			if(!ClientResearchData.isResearchCompleted(currentHighlight) && !canUnlockResearch(this.currentHighlight))
			{
				var53 = this.galFontRenderer;
			}
			
			if(canUnlockResearch(this.currentHighlight))
			{
				var42 = (int) Math.max((float) var53.getStringWidth(var51), (float) var53.getStringWidth(this.currentHighlight.getText()) / 1.9F);
				var44 = var53.getWordWrappedHeight(var51, var42) + 5;
				
				this.drawGradientRect(var26 - 3, var27 - 3, var26 + var42 + 3, var27 + var44 + 6 + var52, -1073741824, -1073741824);
				GL11.glPushMatrix();
				GL11.glTranslatef((float) var26, (float) (var27 + var44 - 1), 0.0F);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				this.fontRenderer.drawStringWithShadow(this.currentHighlight.getText(), 0, 0, -7302913);
				GL11.glPopMatrix();
				
				GL11.glPushMatrix();
				// if(var56)
				// {
				// GL11.glPushMatrix();
				// GL11.glTranslatef((float) var26, (float) (var27 + var44 + 8),
				// 0.0F);
				// GL11.glScalef(0.5F, 0.5F, 0.5F);
				//
				// // if(ResearchManager.getResearchSlot(this.mc.thePlayer,
				// // this.currentHighlight.key) >= 0)
				// // {
				// //
				// this.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tc.research.hasnote"),
				// // 0, 0, 16753920);
				// // } else if(this.hasScribestuff)
				// // {
				// //
				// this.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tc.research.getprim"),
				// // 0, 0, 8900331);
				// // } else
				// // {
				// //
				// this.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tc.research.shortprim"),
				// // 0, 0, 14423100);
				// // }
				//
				// GL11.glPopMatrix();
				// }
				
				GL11.glPopMatrix();
			} else
			{
				GL11.glPushMatrix();
				var42 = (int) Math.max((float) var53.getStringWidth(var51), (float) var53.getStringWidth(I18n.format("lt.researchmissing")) / 1.5F);
				String var55 = I18n.format("lt.researchmissing");
				int var421 = var53.getWordWrappedHeight(var55, var42 * 2);
				this.drawGradientRect(var26 - 3, var27 - 3, var26 + var42 + 3, var27 + var421 + 10, -1073741824, -1073741824);
				GL11.glTranslatef((float) var26, (float) (var27 + 12), 0.0F);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				this.fontRenderer.drawSplitString(var55, 0, 0, var42 * 2, -9416624);
				GL11.glPopMatrix();
			}
			
			var53.drawStringWithShadow(var51, var26, var27, canUnlockResearch(this.currentHighlight) ? (this.currentHighlight.isSpecial() ? -128 : -1) : (this.currentHighlight.isSpecial() ? -8355776 : -8355712));
		}
		
		GL11.glEnable(2929);
		GL11.glEnable(2896);
		RenderHelper.disableStandardItemLighting();
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		this.popuptime = System.currentTimeMillis() - 1L;
		int count;
		if(this.currentHighlight != null && ClientResearchData.isResearchCompleted(this.currentHighlight))
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEventsLT.PAGE.sound, 1));
			this.mc.displayGuiScreen(new GuiResearchRecipe(this.currentHighlight, 0, this.guiMapX, this.guiMapY));
		} else
		{
			int var4 = (this.width - this.paneWidth) / 2;
			int var5 = (this.height - this.paneHeight) / 2;
			Set cats = ResearchCategories.researchCategories.keySet();
			count = 0;
			boolean swop = false;
			Iterator i$ = cats.iterator();
			
			while(i$.hasNext())
			{
				Object obj = i$.next();
				ResearchCategoryList rcl = ResearchCategories.getResearchList((String) obj);
				if(!((String) obj).equals("ELDRITCH") || ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR"))
				{
					if(count == 9)
					{
						count = 0;
						swop = true;
					}
					
					int mposx = par1 - (var4 - 24 + (swop ? 280 : 0));
					int mposy = par2 - (var5 + count * 24);
					if(mposx >= 0 && mposx < 24 && mposy >= 0 && mposy < 24)
					{
						selectedCategory = (String) obj;
						this.updateResearch();
						this.playButtonClick();
						break;
					}
					
					++count;
				}
			}
		}
		
		super.mouseClicked(par1, par2, par3);
	}
	
	public void drawTexturedModalRectReversed(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder b = tessellator.getBuffer();
		b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		b.pos((double) (par1 + 0), (double) (par2 + par6), (double) this.zLevel).tex((double) ((float) (par3 + 0) * f), (double) ((float) (par4 + par6) * f1)).endVertex();
		b.pos((double) (par1 + par5), (double) (par2 + par6), (double) this.zLevel).tex((double) ((float) (par3 - par5) * f), (double) ((float) (par4 + par6) * f1)).endVertex();
		b.pos((double) (par1 + par5), (double) (par2 + 0), (double) this.zLevel).tex((double) ((float) (par3 - par5) * f), (double) ((float) (par4 + 0) * f1)).endVertex();
		b.pos((double) (par1 + 0), (double) (par2 + 0), (double) this.zLevel).tex((double) ((float) (par3 + 0) * f), (double) ((float) (par4 + 0) * f1)).endVertex();
		tessellator.draw();
	}
	
	private void playButtonClick()
	{
		HammerCore.audioProxy.playSoundAt(mc.getRenderViewEntity().world, Info.MOD_ID + ":cameraclack", this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY, this.mc.getRenderViewEntity().posZ, 0.4F, 1.0F, SoundCategory.MASTER);
	}
	
	private boolean canUnlockResearch(ResearchItem res)
	{
		String[] arr$;
		int len$;
		int i$;
		String pt;
		ResearchItem parent;
		if(res.parents != null && res.parents.length > 0)
		{
			arr$ = res.parents;
			len$ = arr$.length;
			
			for(i$ = 0; i$ < len$; ++i$)
			{
				pt = arr$[i$];
				parent = ResearchCategories.getResearch(pt);
				if(parent != null && !ClientResearchData.isResearchCompleted(parent))
				{
					return false;
				}
			}
		}
		
		if(res.parentsHidden != null && res.parentsHidden.length > 0)
		{
			arr$ = res.parentsHidden;
			len$ = arr$.length;
			
			for(i$ = 0; i$ < len$; ++i$)
			{
				pt = arr$[i$];
				parent = ResearchCategories.getResearch(pt);
				if(parent != null && !ClientResearchData.isResearchCompleted(parent))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void drawLine(int x, int y, int x2, int y2, float r, float g, float b, float te, boolean wiggle)
	{
		float count = (float) Minecraft.getMinecraft().player.ticksExisted + te;
		Tessellator var12 = Tessellator.getInstance();
		GL11.glPushMatrix();
		GL11.glAlphaFunc(516, 0.003921569F);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		double d3 = (double) (x - x2);
		double d4 = (double) (y - y2);
		float dist = MathHelper.sqrt(d3 * d3 + d4 * d4);
		int inc = (int) (dist / 2.0F);
		float dx = (float) (d3 / (double) inc);
		float dy = (float) (d4 / (double) inc);
		if(Math.abs(d3) > Math.abs(d4))
		{
			dx *= 2.0F;
		} else
		{
			dy *= 2.0F;
		}
		
		GL11.glLineWidth(3.0F);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		BufferBuilder bb = var12.getBuffer();
		bb.begin(3, DefaultVertexFormats.POSITION_COLOR);
		
		for(int a = 0; a <= inc; ++a)
		{
			float r2 = r;
			float g2 = g;
			float b2 = b;
			float mx = 0.0F;
			float my = 0.0F;
			float op = 0.6F;
			
			if(wiggle)
			{
				float phase = (float) a / (float) inc;
				mx = MathHelper.sin((count + (float) a) / 7.0F) * 5.0F * (1.0F - phase);
				my = MathHelper.sin((count + (float) a) / 5.0F) * 5.0F * (1.0F - phase);
				r2 = r * (1.0F - phase);
				g2 = g * (1.0F - phase);
				b2 = b * (1.0F - phase);
				op *= phase;
			}
			
			bb.pos((double) ((float) x - dx * (float) a + mx), (double) ((float) y - dy * (float) a + my), 0.0D).color(r2, g2, b2, op).endVertex();
			
			if(Math.abs(d3) > Math.abs(d4))
				dx *= 1.0F - 1.0F / ((float) inc * 3.0F / 2.0F);
			else
				dy *= 1.0F - 1.0F / ((float) inc * 3.0F / 2.0F);
		}
		
		var12.draw();
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glDisable('\u803a');
		GL11.glEnable(3553);
		GL11.glAlphaFunc(516, 0.1F);
		GL11.glPopMatrix();
	}
	
	public void drawScreen(int mx, int my, float par3)
	{
		int var4 = (this.width - this.paneWidth) / 2;
		int var5 = (this.height - this.paneHeight) / 2;
		if(Mouse.isButtonDown(0))
		{
			int var6 = var4 + 8;
			int var7 = var5 + 17;
			if((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && mx >= var6 && mx < var6 + 224 && my >= var7 && my < var7 + 196)
			{
				if(this.isMouseButtonDown == 0)
				{
					this.isMouseButtonDown = 1;
				} else
				{
					this.guiMapX -= (double) (mx - this.mouseX);
					this.guiMapY -= (double) (my - this.mouseY);
					this.field_74124_q = this.field_74117_m = this.guiMapX;
					this.field_74123_r = this.field_74115_n = this.guiMapY;
				}
				this.mouseX = mx;
				this.mouseY = my;
			}
			if(this.field_74124_q < (double) guiMapTop)
			{
				this.field_74124_q = guiMapTop;
			}
			if(this.field_74123_r < (double) guiMapLeft)
			{
				this.field_74123_r = guiMapLeft;
			}
			if(this.field_74124_q >= (double) guiMapBottom)
			{
				this.field_74124_q = guiMapBottom - 1;
			}
			if(this.field_74123_r >= (double) guiMapRight)
			{
				this.field_74123_r = guiMapRight - 1;
			}
		} else
		{
			this.isMouseButtonDown = 0;
		}
		
		drawDefaultBackground();
		GlStateManager.enableBlend();
		
		genResearchBackground(mx, my, par3);
		
		if(this.popuptime > System.currentTimeMillis())
		{
			int xq = var4 + 128;
			int yq = var5 + 128;
			int var41 = this.fontRenderer.getWordWrappedHeight(this.popupmessage, 150) / 2;
			this.drawGradientRect(xq - 78, yq - var41 - 3, xq + 78, yq + var41 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawSplitString(this.popupmessage, xq - 75, yq - var41, 150, -7302913);
		}
		
		Set<String> cats = ResearchCategories.researchCategories.keySet();
		int count = 0;
		boolean swop = false;
		for(String obj : cats)
		{
			if(count == 9)
			{
				count = 0;
				swop = true;
			}
			ResearchCategoryList rcl = ResearchCategories.getResearchList(obj);
			if(obj.equals("ELDRITCH") && !ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR"))
				continue;
			int mposx = mx - (var4 - 24 + (swop ? 280 : 0));
			int mposy = my - (var5 + count * 24);
			if(mposx >= 0 && mposx < 24 && mposy >= 0 && mposy < 24)
				fontRenderer.drawStringWithShadow(ResearchCategories.getCategoryName(obj), mx, my - 8, 16777215);
			++count;
		}
	}
}