package com.pengu.lostthaumaturgy.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.lwjgl.opengl.GL11;

import com.pengu.lostthaumaturgy.api.LostThaumApi;
import com.pengu.lostthaumaturgy.api.research.ResearchCategories;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.api.research.ResearchManager;
import com.pengu.lostthaumaturgy.api.research.ResearchPage;
import com.pengu.lostthaumaturgy.client.TCFontRenderer;
import com.pengu.lostthaumaturgy.init.SoundEventsLT;
import com.pengu.lostthaumaturgy.utils.InventoryUtils;
import com.pengu.lostthaumaturgy.utils.UtilsFX;
import com.sun.jna.platform.unix.X11.XSizeHints.Aspect;

@SideOnly(value = Side.CLIENT)
public class GuiResearchRecipe extends GuiScreen
{
	protected static RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	public static LinkedList<Object[]> history = new LinkedList();
	protected int paneWidth = 256;
	protected int paneHeight = 181;
	protected double guiMapX;
	protected double guiMapY;
	protected int mouseX = 0;
	protected int mouseY = 0;
	private GuiButton button;
	private ResearchItem research;
	private ResearchPage[] pages = null;
	private int page = 0;
	private int maxPages = 0;
	TCFontRenderer fr = null;
	HashMap<Aspect, ArrayList<ItemStack>> aspectItems = new HashMap();
	public static ConcurrentHashMap<Integer, ItemStack> cache = new ConcurrentHashMap();
	String tex1 = "textures/gui/gui_researchbook.png";
	String tex2 = "textures/gui/gui_researchbook_overlay.png";
	private Object[] tooltip = null;
	private long lastCycle = 0;
	ArrayList<List> reference = new ArrayList();
	private int cycle = -1;
	
	public static synchronized void putToCache(int key, ItemStack stack)
	{
		cache.put(key, stack);
	}
	
	public static synchronized ItemStack getFromCache(int key)
	{
		return cache.get(key);
	}
	
	public GuiResearchRecipe(ResearchItem research, int page, double x, double y)
	{
		this.research = research;
		this.guiMapX = x;
		this.guiMapY = y;
		this.mc = Minecraft.getMinecraft();
		this.pages = research.getPages();
		List<ResearchPage> p1 = Arrays.asList(this.pages);
		ArrayList<ResearchPage> p2 = new ArrayList<ResearchPage>();
		for(ResearchPage pp : p1)
		{
			if(pp != null && pp.type == ResearchPage.PageType.TEXT_CONCEALED && !ResearchManager.isResearchComplete(this.mc.player.getName(), pp.research))
				continue;
			p2.add(pp);
		}
		this.pages = p2.toArray(new ResearchPage[0]);
		this.maxPages = this.pages.length;
		this.fr = new TCFontRenderer(this.mc.gameSettings, TCFontRenderer.FONT_NORMAL, this.mc.renderEngine, true);
		if(page % 2 == 1)
		{
			--page;
		}
		this.page = page;
	}
	
	public void initGui()
	{
	}
	
	protected void keyTyped(char par1, int par2) throws IOException
	{
		if(par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() || par2 == 1)
		{
			history.clear();
			this.mc.displayGuiScreen((GuiScreen) new GuiThaumonomicon(this.guiMapX, this.guiMapY));
		} else
		{
			super.keyTyped(par1, par2);
		}
	}
	
	public void drawScreen(int par1, int par2, float par3)
	{
		this.drawDefaultBackground();
		GlStateManager.enableBlend();
		this.genResearchBackground(par1, par2, par3);
		int sw = (this.width - this.paneWidth) / 2;
		int sh = (this.height - this.paneHeight) / 2;
		if(!history.isEmpty())
		{
			int mx = par1 - (sw + 118);
			int my = par2 - (sh + 189);
			if(mx >= 0 && my >= 0 && mx < 20 && my < 12)
				fontRenderer.drawStringWithShadow(I18n.format((String) "recipe.return"), par1, par2, 16777215);
		}
	}
	
	protected void genResearchBackground(int par1, int par2, float par3)
	{
		int sw = (this.width - this.paneWidth) / 2;
		int sh = (this.height - this.paneHeight) / 2;
		float var10 = ((float) this.width - (float) this.paneWidth * 1.3f) / 2.0f;
		float var11 = ((float) this.height - (float) this.paneHeight * 1.3f) / 2.0f;
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		UtilsFX.bindTexture(this.tex1);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) var10, (float) var11, (float) 0.0f);
		GL11.glEnable((int) 3042);
		GL11.glScalef((float) 1.3f, (float) 1.3f, (float) 1.0f);
		this.drawTexturedModalRect(0, 0, 0, 0, this.paneWidth, this.paneHeight);
		GL11.glPopMatrix();
		this.reference.clear();
		this.tooltip = null;
		int current = 0;
		for(int a = 0; a < this.pages.length; ++a)
		{
			if((current == this.page || current == this.page + 1) && current < this.maxPages)
			{
				this.drawPage(this.pages[a], current % 2, sw, sh, par1, par2);
			}
			if(++current > this.page + 1)
				break;
		}
		if(this.tooltip != null)
			UtilsFX.drawCustomTooltip(this, itemRenderer, fontRenderer, (List) this.tooltip[0], (Integer) this.tooltip[1], (Integer) this.tooltip[2], (Integer) this.tooltip[3]);
		UtilsFX.bindTexture(this.tex1);
		int mx1 = par1 - (sw + 261);
		int my1 = par2 - (sh + 189);
		int mx2 = par1 - (sw - 17);
		int my2 = par2 - (sh + 189);
		float bob = MathHelper.sin((float) ((float) mc.player.ticksExisted / 3.0f)) * 0.2f + 0.1f;
		if(!history.isEmpty())
		{
			GL11.glEnable((int) 3042);
			this.drawTexturedModalRectScaled(sw + 118, sh + 189, 38, 202, 20, 12, bob);
		}
		if(this.page > 0)
		{
			GL11.glEnable((int) 3042);
			this.drawTexturedModalRectScaled(sw - 16, sh + 190, 0, 184, 12, 8, bob);
		}
		if(this.page < this.maxPages - 2)
		{
			GL11.glEnable((int) 3042);
			this.drawTexturedModalRectScaled(sw + 262, sh + 190, 12, 184, 12, 8, bob);
		}
	}
	
	public void drawCustomTooltip(GuiScreen gui, RenderItem itemRenderer, FontRenderer fr, List var4, int par2, int par3, int subTipColor)
	{
		this.tooltip = new Object[] { var4, par2, par3, subTipColor };
	}
	
	private void drawPage(ResearchPage pageParm, int side, int x, int y, int mx, int my)
	{
		GL11.glPushAttrib((int) 1048575);
		
		if(this.lastCycle < System.currentTimeMillis())
		{
			++this.cycle;
			this.lastCycle = System.currentTimeMillis() + 1000;
		}
		
		if(this.page == 0 && side == 0)
		{
			this.drawTexturedModalRect(x + 4, y - 13, 24, 184, 96, 4);
			this.drawTexturedModalRect(x + 4, y + 4, 24, 184, 96, 4);
			int offset = fontRenderer.getStringWidth(research.getName());
			if(offset <= 130)
			{
				fontRenderer.drawString(this.research.getName(), x + 52 - offset / 2, y - 6, 3158064);
			} else
			{
				float vv = 130.0f / (float) offset;
				GL11.glPushMatrix();
				GL11.glTranslatef((float) ((float) (x + 52) - (float) (offset / 2) * vv), (float) ((float) y - 6.0f * vv), (float) 0.0f);
				GL11.glScalef((float) vv, (float) vv, (float) vv);
				this.fontRenderer.drawString(this.research.getName(), 0, 0, 3158064);
				GL11.glPopMatrix();
			}
			y += 25;
		}
		
		GL11.glAlphaFunc((int) 516, (float) 0.003921569f);
		
		if(pageParm.type == ResearchPage.PageType.TEXT || pageParm.type == ResearchPage.PageType.TEXT_CONCEALED)
			this.drawTextPage(side, x, y - 10, pageParm.getTranslatedText());
		else if(pageParm.type == ResearchPage.PageType.NORMAL_CRAFTING)
			this.drawCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
		else if(pageParm.type == ResearchPage.PageType.SMELTING)
			this.drawSmeltingPage(side, x - 4, y - 8, mx, my, pageParm);
		
		// else if(pageParm.type ==
		// ResearchPage.PageType.DARK_INFUSION_CRAFTING)
		// {
		// this.drawCruciblePage(side, x - 4, y - 8, mx, my, pageParm);
		// } else if(pageParm.type == ResearchPage.PageType.NORMAL_CRAFTING)
		// {
		// this.drawCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
		// } else if(pageParm.type == ResearchPage.PageType.ARCANE_CRAFTING)
		// {
		// this.drawArcaneCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
		// } else if(pageParm.type == ResearchPage.PageType.COMPOUND_CRAFTING)
		// {
		// this.drawCompoundCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
		// } else if(pageParm.type == ResearchPage.PageType.INFUSION_CRAFTING)
		// {
		// this.drawInfusionPage(side, x - 4, y - 8, mx, my, pageParm);
		// } else if(pageParm.type ==
		// ResearchPage.PageType.INFUSION_ENCHANTMENT)
		// {
		// this.drawInfusionEnchantingPage(side, x - 4, y - 8, mx, my,
		// pageParm);
		// } else if(pageParm.type == ResearchPage.PageType.SMELTING)
		// {
		// this.drawSmeltingPage(side, x - 4, y - 8, mx, my, pageParm);
		// }
		GL11.glAlphaFunc((int) 516, (float) 0.1f);
		GL11.glPopAttrib();
	}
	
	// private void drawCompoundCraftingPage(int side, int x, int y, int mx, int
	// my, ResearchPage page)
	// {
	// List r = (List) page.recipe;
	// if(r != null)
	// {
	// int j;
	// int px;
	// int k;
	// int py;
	// AspectList aspects = (AspectList) r.get(0);
	// int dx = (Integer) r.get(1);
	// int dy = (Integer) r.get(2);
	// int dz = (Integer) r.get(3);
	// int xoff = 64 - (dx * 16 + dz * 16) / 2;
	// int yoff = (-dy) * 25;
	// List items = (List) r.get(4);
	// GL11.glPushMatrix();
	// int start = side * 152;
	// String text = StatCollector.translateToLocal((String)
	// "recipe.type.construct");
	// int offset = this.fontRenderer.getStringWidth(text);
	// this.fontRenderer.drawString(text, x + start + 56 - offset / 2, y,
	// 5263440);
	// int mposx = mx;
	// int mposy = my;
	// if(aspects != null && aspects.size() > 0)
	// {
	// int count = 0;
	// for(Aspect tag : aspects.getAspectsSortedAmount())
	// {
	// UtilsFX.drawTag(x + start + 14 + 18 * count + (5 - aspects.size()) * 8, y
	// + 182, tag, (float) aspects.getAmount(tag), 0, 0.0, 771, 1.0f, false);
	// ++count;
	// }
	// count = 0;
	// for(Aspect tag : aspects.getAspectsSortedAmount())
	// {
	// int tx = x + start + 14 + 18 * count + (5 - aspects.size()) * 8;
	// int ty = y + 182;
	// if(mposx >= tx && mposy >= ty && mposx < tx + 16 && mposy < ty + 16)
	// {
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer,
	// Arrays.asList(tag.getName(), tag.getLocalizedDescription()), mx, my - 8,
	// 11);
	// }
	// ++count;
	// }
	// }
	// UtilsFX.bindTexture(this.tex2);
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// RenderHelper.enableGUIStandardItemLighting();
	// GL11.glDisable((int) 2896);
	// if(aspects != null && aspects.size() > 0)
	// {
	// GL11.glPushMatrix();
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 0.4f);
	// GL11.glEnable((int) 3042);
	// GL11.glTranslatef((float) (x + start), (float) (y + 174), (float) 0.0f);
	// GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
	// this.drawTexturedModalRect(0, 0, 68, 76, 12, 12);
	// GL11.glPopMatrix();
	// }
	// GL11.glPushMatrix();
	// float sz = 0.0f;
	// if(dy > 3)
	// {
	// sz = (float) (dy - 3) * 0.2f;
	// GL11.glTranslatef((float) ((float) (x + start) + (float) xoff * (1.0f +
	// sz)), (float) ((float) (y + 108) + (float) yoff * (1.0f - sz)), (float)
	// 0.0f);
	// GL11.glScalef((float) (1.0f - sz), (float) (1.0f - sz), (float) (1.0f -
	// sz));
	// } else
	// {
	// GL11.glTranslatef((float) (x + start + xoff), (float) (y + 108 + yoff),
	// (float) 0.0f);
	// }
	// GL11.glPushMatrix();
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 0.5f);
	// GL11.glEnable((int) 3042);
	// GL11.glTranslatef((float) (-8 - xoff), (float) (-119 + Math.max(3 - dx, 3
	// - dz) * 8 + dx * 4 + dz * 4 + dy * 50), (float) 0.0f);
	// GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
	// this.drawTexturedModalRect(0, 0, 0, 72, 64, 44);
	// GL11.glPopMatrix();
	// int count = 0;
	// for(j = 0; j < dy; ++j)
	// {
	// for(k = dz - 1; k >= 0; --k)
	// {
	// for(int i = dx - 1; i >= 0; --i)
	// {
	// px = i * 16 + k * 16;
	// py = (-i) * 8 + k * 8 + j * 50;
	// GL11.glPushMatrix();
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// RenderHelper.enableGUIStandardItemLighting();
	// GL11.glEnable((int) 2884);
	// GL11.glTranslatef((float) 0.0f, (float) 0.0f, (float) (60 - j * 10));
	// if(items.get(count) != null)
	// {
	// itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack(items.get(count)),
	// px, py);
	// itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine,
	// InventoryUtils.cycleItemStack(items.get(count)).copy().splitStack(1), px,
	// py);
	// }
	// RenderHelper.disableStandardItemLighting();
	// GL11.glPopMatrix();
	// ++count;
	// }
	// }
	// }
	// GL11.glEnable((int) 2896);
	// GL11.glPopMatrix();
	// count = 0;
	// for(j = 0; j < dy; ++j)
	// {
	// for(k = dz - 1; k >= 0; --k)
	// {
	// for(int i = dx - 1; i >= 0; --i)
	// {
	// px = (int) ((float) (x + start) + (float) xoff * (1.0f + sz) + (float) (i
	// * 16) * (1.0f - sz) + (float) (k * 16) * (1.0f - sz));
	// py = (int) ((float) (y + 108) + (float) yoff * (1.0f - sz) - (float) (i *
	// 8) * (1.0f - sz) + (float) (k * 8) * (1.0f - sz) + (float) (j * 50) *
	// (1.0f - sz));
	// if(items.get(count) != null && mposx >= px && mposy >= py && (float)
	// mposx < (float) px + 16.0f * (1.0f - sz) && (float) mposy < (float) py +
	// 16.0f * (1.0f - sz))
	// {
	// List addtext =
	// InventoryUtils.cycleItemStack(items.get(count)).getTooltip((EntityPlayer)
	// this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
	// Object[] ref =
	// this.findRecipeReference(InventoryUtils.cycleItemStack(items.get(count)));
	// if(ref != null && !((String) ref[0]).equals(this.research.key))
	// {
	// addtext.add("\u00a78\u00a7o" + StatCollector.translateToLocal((String)
	// "recipe.clickthrough"));
	// this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer)
	// ref[1]));
	// }
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, addtext,
	// mx, my, 11);
	// }
	// ++count;
	// }
	// }
	// }
	// GL11.glPopMatrix();
	// }
	// }
	//
	// private void drawArcaneCraftingPage(int side, int x, int y, int mx, int
	// my, ResearchPage pageParm)
	// {
	// IArcaneRecipe recipe = null;
	// Object tr = null;
	// if(pageParm.recipe instanceof Object[])
	// {
	// try
	// {
	// tr = ((Object[]) pageParm.recipe)[this.cycle];
	// } catch(Exception e)
	// {
	// this.cycle = 0;
	// tr = ((Object[]) pageParm.recipe)[this.cycle];
	// }
	// } else
	// {
	// tr = pageParm.recipe;
	// }
	// if(tr instanceof ShapedArcaneRecipe)
	// {
	// recipe = (ShapedArcaneRecipe) tr;
	// } else if(tr instanceof ShapelessArcaneRecipe)
	// {
	// recipe = (ShapelessArcaneRecipe) tr;
	// }
	// if(recipe == null)
	// {
	// return;
	// }
	// GL11.glPushMatrix();
	// int start = side * 152;
	// UtilsFX.bindTexture(this.tex2);
	// GL11.glPushMatrix();
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// GL11.glEnable((int) 3042);
	// GL11.glTranslatef((float) (x + start), (float) y, (float) 0.0f);
	// GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
	// this.drawTexturedModalRect(2, 27, 112, 15, 52, 52);
	// this.drawTexturedModalRect(20, 7, 20, 3, 16, 16);
	// GL11.glPopMatrix();
	// GL11.glPushMatrix();
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 0.4f);
	// GL11.glEnable((int) 3042);
	// GL11.glTranslatef((float) (x + start), (float) (y + 164), (float) 0.0f);
	// GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
	// this.drawTexturedModalRect(0, 0, 68, 76, 12, 12);
	// GL11.glPopMatrix();
	// int mposx = mx;
	// int mposy = my;
	// AspectList tags = recipe.getAspects();
	// if(tags != null && tags.size() > 0)
	// {
	// int count = 0;
	// for(Aspect tag : tags.getAspectsSortedAmount())
	// {
	// UtilsFX.drawTag(x + start + 14 + 18 * count + (5 - tags.size()) * 8, y +
	// 172, tag, tags.getAmount(tag), 0, 0.0, 771, 1.0f);
	// ++count;
	// }
	// count = 0;
	// for(Aspect tag : tags.getAspectsSortedAmount())
	// {
	// int tx = x + start + 14 + 18 * count + (5 - tags.size()) * 8;
	// int ty = y + 172;
	// if(mposx >= tx && mposy >= ty && mposx < tx + 16 && mposy < ty + 16)
	// {
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer,
	// Arrays.asList(tag.getName(), tag.getLocalizedDescription()), mx, my - 8,
	// 11);
	// }
	// ++count;
	// }
	// }
	// GL11.glPushMatrix();
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
	// RenderHelper.enableGUIStandardItemLighting();
	// GL11.glEnable((int) 2884);
	// itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack((Object)
	// recipe.getRecipeOutput()), x + 48 + start, y + 22);
	// itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack((Object)
	// recipe.getRecipeOutput()), x + 48 + start, y + 22);
	// RenderHelper.disableStandardItemLighting();
	// GL11.glEnable((int) 2896);
	// GL11.glPopMatrix();
	// if(mposx >= x + 48 + start && mposy >= y + 27 && mposx < x + 48 + start +
	// 16 && mposy < y + 27 + 16)
	// {
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer,
	// InventoryUtils.cycleItemStack((Object)
	// recipe.getRecipeOutput()).getTooltip((EntityPlayer) this.mc.thePlayer,
	// this.mc.gameSettings.advancedItemTooltips), mx, my, 11);
	// }
	// String text = StatCollector.translateToLocal((String)
	// "recipe.type.arcane");
	// int offset = this.fontRenderer.getStringWidth(text);
	// this.fontRenderer.drawString(text, x + start + 56 - offset / 2, y,
	// 5263440);
	// if(recipe != null && recipe instanceof ShapedArcaneRecipe)
	// {
	// int i;
	// int j;
	// int rw = ((ShapedArcaneRecipe) recipe).width;
	// int rh = ((ShapedArcaneRecipe) recipe).height;
	// Object[] items = ((ShapedArcaneRecipe) recipe).getInput();
	// for(i = 0; i < rw && i < 3; ++i)
	// {
	// for(j = 0; j < rh && j < 3; ++j)
	// {
	// if(items[i + j * rw] == null)
	// continue;
	// GL11.glPushMatrix();
	// GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// RenderHelper.enableGUIStandardItemLighting();
	// GL11.glEnable((int) 2884);
	// itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack(items[i + j * rw]), x
	// + start + 16 + i * 32, y + 66 + j * 32);
	// itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack(items[i + j *
	// rw]).copy().splitStack(1), x + start + 16 + i * 32, y + 66 + j * 32);
	// RenderHelper.disableStandardItemLighting();
	// GL11.glEnable((int) 2896);
	// GL11.glPopMatrix();
	// }
	// }
	// for(i = 0; i < rw && i < 3; ++i)
	// {
	// for(j = 0; j < rh && j < 3; ++j)
	// {
	// if(items[i + j * rw] == null || mposx < x + 16 + start + i * 32 || mposy
	// < y + 66 + j * 32 || mposx >= x + 16 + start + i * 32 + 16 || mposy >= y
	// + 66 + j * 32 + 16)
	// continue;
	// List addtext = InventoryUtils.cycleItemStack(items[i + j *
	// rw]).getTooltip((EntityPlayer) this.mc.thePlayer,
	// this.mc.gameSettings.advancedItemTooltips);
	// Object[] ref =
	// this.findRecipeReference(InventoryUtils.cycleItemStack(items[i + j *
	// rw]));
	// if(ref != null && !((String) ref[0]).equals(this.research.key))
	// {
	// addtext.add("\u00a78\u00a7o" + StatCollector.translateToLocal((String)
	// "recipe.clickthrough"));
	// this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer)
	// ref[1]));
	// }
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, addtext,
	// mx, my, 11);
	// }
	// }
	// }
	// if(recipe != null && recipe instanceof ShapelessArcaneRecipe)
	// {
	// int i;
	// ArrayList items = ((ShapelessArcaneRecipe) recipe).getInput();
	// for(i = 0; i < items.size() && i < 9; ++i)
	// {
	// if(items.get(i) == null)
	// continue;
	// GL11.glPushMatrix();
	// GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// RenderHelper.enableGUIStandardItemLighting();
	// GL11.glEnable((int) 2884);
	// itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack(items.get(i)), x +
	// start + 16 + i % 3 * 32, y + 66 + i / 3 * 32);
	// itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack(items.get(i)), x +
	// start + 16 + i % 3 * 32, y + 66 + i / 3 * 32);
	// RenderHelper.disableStandardItemLighting();
	// GL11.glEnable((int) 2896);
	// GL11.glPopMatrix();
	// }
	// for(i = 0; i < items.size() && i < 9; ++i)
	// {
	// if(items.get(i) == null || mposx < x + 16 + start + i % 3 * 32 || mposy <
	// y + 66 + i / 3 * 32 || mposx >= x + 16 + start + i % 3 * 32 + 16 || mposy
	// >= y + 66 + i / 3 * 32 + 16)
	// continue;
	// List addtext =
	// InventoryUtils.cycleItemStack(items.get(i)).getTooltip((EntityPlayer)
	// this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
	// Object[] ref =
	// this.findRecipeReference(InventoryUtils.cycleItemStack(items.get(i)));
	// if(ref != null && !((String) ref[0]).equals(this.research.key))
	// {
	// addtext.add("\u00a78\u00a7o" + StatCollector.translateToLocal((String)
	// "recipe.clickthrough"));
	// this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer)
	// ref[1]));
	// }
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, addtext,
	// mx, my, 11);
	// }
	// }
	// GL11.glPopMatrix();
	// }
	
	private void drawCraftingPage(int side, int x, int y, int mx, int my, ResearchPage pageParm)
	{
		String text;
		int offset;
		IRecipe recipe = null;
		Object tr = null;
		Object r = pageParm.getRecipe();
		
		if(r instanceof Object[])
		{
			try
			{
				tr = ((Object[]) r)[this.cycle];
			} catch(Exception e)
			{
				this.cycle = 0;
				tr = ((Object[]) r)[this.cycle];
			}
		} else
		{
			tr = r;
		}
		if(tr instanceof ShapedRecipes)
		{
			recipe = (ShapedRecipes) tr;
		} else if(tr instanceof ShapelessRecipes)
		{
			recipe = (ShapelessRecipes) tr;
		} else if(tr instanceof ShapedOreRecipe)
		{
			recipe = (ShapedOreRecipe) tr;
		} else if(tr instanceof ShapelessOreRecipe)
		{
			recipe = (ShapelessOreRecipe) tr;
		}
		if(recipe == null)
		{
			return;
		}
		GL11.glPushMatrix();
		int start = side * 152;
		UtilsFX.bindTexture(this.tex2);
		GL11.glPushMatrix();
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		GL11.glEnable((int) 3042);
		GL11.glTranslatef((float) (x + start), (float) y, (float) 0.0f);
		GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
		this.drawTexturedModalRect(2, 32, 60, 15, 52, 52);
		this.drawTexturedModalRect(20, 12, 20, 3, 16, 16);
		GL11.glPopMatrix();
		int mposx = mx;
		int mposy = my;
		GL11.glPushMatrix();
		GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable((int) 2884);
		itemRenderer.renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack((Object) recipe.getRecipeOutput()), x + 48 + start, y + 32);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, InventoryUtils.cycleItemStack((Object) recipe.getRecipeOutput()), x + 48 + start, y + 32, null);
		RenderHelper.disableStandardItemLighting();
		GL11.glEnable((int) 2896);
		GL11.glPopMatrix();
		if(mposx >= x + 48 + start && mposy >= y + 32 && mposx < x + 48 + start + 16 && mposy < y + 32 + 16)
			this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, InventoryUtils.cycleItemStack((Object) recipe.getRecipeOutput()).getTooltip((EntityPlayer) this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL), mx, my, 11);
		
		NonNullList<Ingredient> items = recipe.getIngredients();
		
		if(recipe != null && (recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe))
		{
			int i;
			int j;
			text = I18n.format((String) "recipe.type.workbench");
			offset = this.fontRenderer.getStringWidth(text);
			this.fontRenderer.drawString(text, x + start + 56 - offset / 2, y, 5263440);
			int rw = 0;
			int rh = 0;
			
			if(recipe instanceof ShapedRecipes)
			{
				rw = ((ShapedRecipes) recipe).getWidth();
				rh = ((ShapedRecipes) recipe).getHeight();
				items = recipe.getIngredients();
			} else
			{
				rw = ((ShapedOreRecipe) recipe).getWidth();
				rh = ((ShapedOreRecipe) recipe).getHeight();
				items = ((ShapedOreRecipe) recipe).getIngredients();
			}
			for(i = 0; i < rw && i < 3; ++i)
			{
				for(j = 0; j < rh && j < 3; ++j)
				{
					if(items.get(i + j * rw) == null)
						continue;
					GL11.glPushMatrix();
					GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
					RenderHelper.enableGUIStandardItemLighting();
					GL11.glEnable((int) 2884);
					GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
					itemRenderer.renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack((Object) items.get(i + j * rw)), x + start + 16 + i * 32, y + 76 + j * 32);
					itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, InventoryUtils.cycleItemStack((Object) items.get(i + j * rw)).copy().splitStack(1), x + start + 16 + i * 32, y + 76 + j * 32, null);
					RenderHelper.disableStandardItemLighting();
					GL11.glEnable((int) 2896);
					GL11.glPopMatrix();
				}
			}
			for(i = 0; i < rw && i < 3; ++i)
			{
				for(j = 0; j < rh && j < 3; ++j)
				{
					if(items.get(i + j * rw) == null || mposx < x + 16 + start + i * 32 || mposy < y + 76 + j * 32 || mposx >= x + 16 + start + i * 32 + 16 || mposy >= y + 76 + j * 32 + 16)
						continue;
					List addtext = InventoryUtils.cycleItemStack((Object) items.get(i + j * rw)).getTooltip((EntityPlayer) this.mc.player, this.mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
					Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack((Object) items.get(i + j * rw)));
					if(ref != null && !((String) ref[0]).equals(this.research.key))
					{
						addtext.add("\u00a78\u00a7o" + I18n.format((String) "recipe.clickthrough"));
						this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
					}
					this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, addtext, mx, my, 11);
				}
			}
		}
		if(recipe != null && (recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe))
		{
			int i;
			text = I18n.format((String) "recipe.type.workbenchshapeless");
			offset = this.fontRenderer.getStringWidth(text);
			this.fontRenderer.drawString(text, x + start + 56 - offset / 2, y, 5263440);
			for(i = 0; i < items.size() && i < 9; ++i)
			{
				if(items.get(i) == null)
					continue;
				GL11.glPushMatrix();
				GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
				RenderHelper.enableGUIStandardItemLighting();
				GL11.glEnable((int) 2884);
				GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
				itemRenderer.renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack(items.get(i)), x + start + 16 + i % 3 * 32, y + 76 + i / 3 * 32);
				itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, InventoryUtils.cycleItemStack(items.get(i)).copy().splitStack(1), x + start + 16 + i % 3 * 32, y + 76 + i / 3 * 32, null);
				RenderHelper.disableStandardItemLighting();
				GL11.glEnable((int) 2896);
				GL11.glPopMatrix();
			}
			for(i = 0; i < items.size() && i < 9; ++i)
			{
				if(items.get(i) == null || mposx < x + 16 + start + i % 3 * 32 || mposy < y + 76 + i / 3 * 32 || mposx >= x + 16 + start + i % 3 * 32 + 16 || mposy >= y + 76 + i / 3 * 32 + 16)
					continue;
				List addtext = InventoryUtils.cycleItemStack(items.get(i)).getTooltip((EntityPlayer) this.mc.player, this.mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
				Object[] ref = this.findRecipeReference(InventoryUtils.cycleItemStack(items.get(i)));
				if(ref != null && !((String) ref[0]).equals(this.research.key))
				{
					addtext.add("\u00a78\u00a7o" + I18n.format((String) "recipe.clickthrough"));
					this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
				}
				this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, addtext, mx, my, 11);
			}
		}
		GL11.glPopMatrix();
	}
	
	private void drawSmeltingPage(int side, int x, int y, int mx, int my, ResearchPage pageParm)
	{
		ItemStack in = (ItemStack) pageParm.getRecipe();
		ItemStack out = null;
		if(in != null)
		{
			out = FurnaceRecipes.instance().getSmeltingResult(in);
		}
		if(in != null && out != null)
		{
			GL11.glPushMatrix();
			int start = side * 152;
			String text = I18n.format((String) "recipe.type.smelting");
			int offset = this.fontRenderer.getStringWidth(text);
			this.fontRenderer.drawString(text, x + start + 56 - offset / 2, y, 5263440);
			UtilsFX.bindTexture(this.tex2);
			GL11.glPushMatrix();
			GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
			GL11.glEnable((int) 3042);
			GL11.glTranslatef((float) (x + start), (float) (y + 28), (float) 0.0f);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
			this.drawTexturedModalRect(0, 0, 0, 192, 56, 64);
			GL11.glPopMatrix();
			int mposx = mx;
			int mposy = my;
			GL11.glPushMatrix();
			GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
			GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable((int) 2884);
			itemRenderer.renderItemAndEffectIntoGUI(in, x + 48 + start, y + 64);
			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, in, x + 48 + start, y + 64, null);
			RenderHelper.disableStandardItemLighting();
			GL11.glEnable((int) 2896);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
			GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable((int) 2884);
			itemRenderer.renderItemAndEffectIntoGUI(out, x + 48 + start, y + 144);
			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, out, x + 48 + start, y + 144, null);
			RenderHelper.disableStandardItemLighting();
			GL11.glEnable((int) 2896);
			GL11.glPopMatrix();
			if(mposx >= x + 48 + start && mposy >= y + 64 && mposx < x + 48 + start + 16 && mposy < y + 64 + 16)
			{
				List addtext = in.getTooltip((EntityPlayer) this.mc.player, this.mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
				Object[] ref = this.findRecipeReference(in);
				if(ref != null && !((String) ref[0]).equals(this.research.key))
				{
					addtext.add("\u00a78\u00a7o" + I18n.format((String) "recipe.clickthrough"));
					this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer) ref[1]));
				}
				this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, addtext, mx, my, 11);
			}
			if(mposx >= x + 48 + start && mposy >= y + 144 && mposx < x + 48 + start + 16 && mposy < y + 144 + 16)
			{
				this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, out.getTooltip((EntityPlayer) this.mc.player, this.mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL), mx, my, 11);
			}
			GL11.glPopMatrix();
		}
	}
	
	// private void drawInfusionPage(int side, int x, int y, int mx, int my,
	// ResearchPage pageParm)
	// {
	// InfusionRecipe ri;
	// Object tr = null;
	// if(pageParm.recipe instanceof Object[])
	// {
	// try
	// {
	// tr = ((Object[]) pageParm.recipe)[this.cycle];
	// } catch(Exception e)
	// {
	// this.cycle = 0;
	// tr = ((Object[]) pageParm.recipe)[this.cycle];
	// }
	// } else
	// {
	// tr = pageParm.recipe;
	// }
	// if((ri = (InfusionRecipe) tr) != null)
	// {
	// int vy;
	// int vx;
	// GL11.glPushMatrix();
	// int start = side * 152;
	// String text = StatCollector.translateToLocal((String)
	// "recipe.type.infusion");
	// int offset = this.fontRenderer.getStringWidth(text);
	// this.fontRenderer.drawString(text, x + start + 56 - offset / 2, y,
	// 5263440);
	// int inst = Math.min(5, ri.getInstability() / 2);
	// text = StatCollector.translateToLocal((String) "tc.inst") + " " +
	// StatCollector.translateToLocal((String) new
	// StringBuilder().append("tc.inst.").append(inst).toString());
	// offset = this.fontRenderer.getStringWidth(text);
	// this.fontRenderer.drawString(text, x + start + 56 - offset / 2, y + 194,
	// 5263440);
	// UtilsFX.bindTexture(this.tex2);
	// GL11.glPushMatrix();
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// GL11.glEnable((int) 3042);
	// GL11.glTranslatef((float) (x + start), (float) (y + 20), (float) 0.0f);
	// GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
	// this.drawTexturedModalRect(0, 0, 0, 3, 56, 17);
	// GL11.glTranslatef((float) 0.0f, (float) 19.0f, (float) 0.0f);
	// this.drawTexturedModalRect(0, 0, 200, 77, 60, 44);
	// GL11.glPopMatrix();
	// int mposx = mx;
	// int mposy = my;
	// int total = 0;
	// int rows = (ri.getAspects().size() - 1) / 5;
	// int shift = (5 - ri.getAspects().size() % 5) * 10;
	// int sx = x + start + 8;
	// int sy = y + 164 - 10 * rows;
	// for(Aspect tag : ri.getAspects().getAspectsSorted())
	// {
	// int m = 0;
	// if(total / 5 >= rows && (rows > 1 || ri.getAspects().size() < 5))
	// {
	// m = 1;
	// }
	// int vx2 = sx + total % 5 * 20 + shift * m;
	// int vy2 = sy + total / 5 * 20;
	// UtilsFX.drawTag(vx2, vy2, tag, ri.getAspects().getAmount(tag), 0,
	// this.zLevel);
	// ++total;
	// }
	// ItemStack idisp = null;
	// if(ri.getRecipeOutput() instanceof ItemStack)
	// {
	// idisp = InventoryUtils.cycleItemStack((Object) ((ItemStack)
	// ri.getRecipeOutput()));
	// } else
	// {
	// idisp = InventoryUtils.cycleItemStack((Object)
	// ri.getRecipeInput()).copy();
	// Object[] obj = (Object[]) ri.getRecipeOutput();
	// NBTBase tag = (NBTBase) obj[1];
	// idisp.setTagInfo((String) obj[0], tag);
	// }
	// GL11.glPushMatrix();
	// GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// RenderHelper.enableGUIStandardItemLighting();
	// GL11.glEnable((int) 2884);
	// itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, idisp, x + 48 + start, y + 28);
	// itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, idisp, x + 48 + start, y + 28);
	// RenderHelper.disableStandardItemLighting();
	// GL11.glEnable((int) 2896);
	// GL11.glPopMatrix();
	// GL11.glPushMatrix();
	// GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// RenderHelper.enableGUIStandardItemLighting();
	// GL11.glEnable((int) 2884);
	// itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack((Object)
	// ri.getRecipeInput()), x + 48 + start, y + 94);
	// itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack((Object)
	// ri.getRecipeInput()).copy().splitStack(1), x + 48 + start, y + 94);
	// RenderHelper.disableStandardItemLighting();
	// GL11.glEnable((int) 2896);
	// GL11.glPopMatrix();
	// GL11.glPushMatrix();
	// GL11.glTranslated((double) 0.0, (double) 0.0, (double) 100.0);
	// GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	// RenderHelper.enableGUIStandardItemLighting();
	// GL11.glDisable((int) 2896);
	// GL11.glEnable((int) 2884);
	// int le = ri.getComponents().length;
	// ArrayList<Coord2D> coords = new ArrayList<Coord2D>();
	// float pieSlice = 360 / le;
	// float currentRot = -90.0f;
	// for(int a = 0; a < le; ++a)
	// {
	// int xx = (int) (MathHelper.cos((float) (currentRot / 180.0f *
	// 3.1415927f)) * 40.0f) - 8;
	// int yy = (int) (MathHelper.sin((float) (currentRot / 180.0f *
	// 3.1415927f)) * 40.0f) - 8;
	// currentRot += pieSlice;
	// coords.add(new Coord2D(xx, yy));
	// }
	// total = 0;
	// sx = x + 56 + start;
	// sy = y + 102;
	// for(ItemStack ingredient : ri.getComponents())
	// {
	// RenderHelper.enableGUIStandardItemLighting();
	// vx = sx + ((Coord2D) coords.get((int) total)).x;
	// vy = sy + ((Coord2D) coords.get((int) total)).y;
	// itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack((Object) ingredient),
	// vx, vy);
	// itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer,
	// this.mc.renderEngine, InventoryUtils.cycleItemStack((Object)
	// ingredient).copy().splitStack(1), vx, vy);
	// RenderHelper.disableStandardItemLighting();
	// ++total;
	// }
	// GL11.glEnable((int) 2896);
	// GL11.glPopMatrix();
	// if(mposx >= x + 48 + start && mposy >= y + 28 && mposx < x + 48 + start +
	// 16 && mposy < y + 28 + 16)
	// {
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer,
	// idisp.getTooltip((EntityPlayer) this.mc.thePlayer,
	// this.mc.gameSettings.advancedItemTooltips), mx, my, 11);
	// }
	// if(mposx >= x + 48 + start && mposy >= y + 94 && mposx < x + 48 + start +
	// 16 && mposy < y + 94 + 16)
	// {
	// List addtext = InventoryUtils.cycleItemStack((Object)
	// ri.getRecipeInput()).getTooltip((EntityPlayer) this.mc.thePlayer,
	// this.mc.gameSettings.advancedItemTooltips);
	// Object[] ref =
	// this.findRecipeReference(InventoryUtils.cycleItemStack((Object)
	// ri.getRecipeInput()));
	// if(ref != null && !((String) ref[0]).equals(this.research.key))
	// {
	// addtext.add("\u00a78\u00a7o" + StatCollector.translateToLocal((String)
	// "recipe.clickthrough"));
	// this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer)
	// ref[1]));
	// }
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, addtext,
	// mx, my, 11);
	// }
	// total = 0;
	// sx = x + 56 + start;
	// sy = y + 102;
	// for(ItemStack ingredient : ri.getComponents())
	// {
	// vx = sx + ((Coord2D) coords.get((int) total)).x;
	// vy = sy + ((Coord2D) coords.get((int) total)).y;
	// if(mposx >= vx && mposy >= vy && mposx < vx + 16 && mposy < vy + 16)
	// {
	// List addtext = InventoryUtils.cycleItemStack((Object)
	// ingredient).getTooltip((EntityPlayer) this.mc.thePlayer,
	// this.mc.gameSettings.advancedItemTooltips);
	// Object[] ref =
	// this.findRecipeReference(InventoryUtils.cycleItemStack((Object)
	// ingredient));
	// if(ref != null && !((String) ref[0]).equals(this.research.key))
	// {
	// addtext.add("\u00a78\u00a7o" + StatCollector.translateToLocal((String)
	// "recipe.clickthrough"));
	// this.reference.add(Arrays.asList(mx, my, (String) ref[0], (Integer)
	// ref[1]));
	// }
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer, addtext,
	// mx, my, 11);
	// }
	// ++total;
	// }
	// total = 0;
	// rows = (ri.getAspects().size() - 1) / 5;
	// shift = (5 - ri.getAspects().size() % 5) * 10;
	// sx = x + start + 8;
	// sy = y + 164 - 10 * rows;
	// for(ItemStack tag : ri.getAspects().getAspectsSorted())
	// {
	// int m = 0;
	// if(total / 5 >= rows && (rows > 1 || ri.getAspects().size() < 5))
	// {
	// m = 1;
	// }
	// int vx3 = sx + total % 5 * 20 + shift * m;
	// int vy3 = sy + total / 5 * 20;
	// if(mposx >= vx3 && mposy >= vy3 && mposx < vx3 + 16 && mposy < vy3 + 16)
	// {
	// this.drawCustomTooltip(this, itemRenderer, this.fontRenderer,
	// Arrays.asList(tag.getName(), tag.getLocalizedDescription()), mx, my, 11);
	// }
	// ++total;
	// }
	// GL11.glPopMatrix();
	// }
	// }
	
	private void drawTextPage(int side, int x, int y, String text)
	{
		GL11.glPushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable((int) 3042);
		fr.drawSplitString(text, x - 15 + side * 152, y, 139, 0, (Gui) this);
		GL11.glPopMatrix();
	}
	
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		int var4 = (this.width - this.paneWidth) / 2;
		int var5 = (this.height - this.paneHeight) / 2;
		int mx = par1 - (var4 + 261);
		int my = par2 - (var5 + 189);
		if(this.page < this.maxPages - 2 && mx >= 0 && my >= 0 && mx < 14 && my < 10)
		{
			this.page += 2;
			this.lastCycle = 0;
			this.cycle = -1;
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEventsLT.PAGE.sound, 1));
		}
		mx = par1 - (var4 - 17);
		my = par2 - (var5 + 189);
		if(this.page >= 2 && mx >= 0 && my >= 0 && mx < 14 && my < 10)
		{
			this.page -= 2;
			this.lastCycle = 0;
			this.cycle = -1;
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEventsLT.PAGE.sound, 1));
		}
		if(!history.isEmpty())
		{
			mx = par1 - (var4 + 118);
			my = par2 - (var5 + 189);
			if(mx >= 0 && my >= 0 && mx < 20 && my < 12)
			{
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEventsLT.PAGE.sound, 1));
				Object[] o = history.pop();
				this.mc.displayGuiScreen((GuiScreen) new GuiResearchRecipe(ResearchCategories.getResearch((String) o[0]), (Integer) o[1], this.guiMapX, this.guiMapY));
			}
		}
		if(this.reference.size() > 0)
		{
			for(List coords : this.reference)
			{
				if(par1 < (Integer) coords.get(0) || par2 < (Integer) coords.get(1) || par1 >= (Integer) coords.get(0) + 16 || par2 >= (Integer) coords.get(1) + 16)
					continue;
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEventsLT.PAGE.sound, 1));
				history.push(new Object[] { this.research.key, this.page });
				this.mc.displayGuiScreen((GuiScreen) new GuiResearchRecipe(ResearchCategories.getResearch((String) coords.get(2)), (Integer) coords.get(3), this.guiMapX, this.guiMapY));
			}
		}
		super.mouseClicked(par1, par2, par3);
	}
	
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	public Object[] findRecipeReference(ItemStack item)
	{
		return LostThaumApi.getCraftingRecipeKey((EntityPlayer) this.mc.player, item);
	}
	
	public void drawTexturedModalRectScaled(int par1, int par2, int par3, int par4, int par5, int par6, float scale)
	{
		GL11.glPushMatrix();
		float var7 = 0.00390625f;
		float var8 = 0.00390625f;
		Tessellator var9 = Tessellator.getInstance();
		GL11.glTranslatef((float) ((float) par1 + (float) par5 / 2.0f), (float) ((float) par2 + (float) par6 / 2.0f), (float) 0.0f);
		GL11.glScalef((float) (1.0f + scale), (float) (1.0f + scale), (float) 1.0f);
		BufferBuilder b = var9.getBuffer();
		b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		b.pos((double) ((float) (-par5) / 2.0f), (double) ((float) par6 / 2.0f), (double) this.zLevel).tex((double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + par6) * var8)).endVertex();
		b.pos((double) ((float) par5 / 2.0f), (double) ((float) par6 / 2.0f), (double) this.zLevel).tex((double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + par6) * var8)).endVertex();
		b.pos((double) ((float) par5 / 2.0f), (double) ((float) (-par6) / 2.0f), (double) this.zLevel).tex((double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + 0) * var8)).endVertex();
		b.pos((double) ((float) (-par5) / 2.0f), (double) ((float) (-par6) / 2.0f), (double) this.zLevel).tex((double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + 0) * var8)).endVertex();
		var9.draw();
		GL11.glPopMatrix();
	}
	
	class Coord2D
	{
		int x;
		int y;
		
		Coord2D(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
}