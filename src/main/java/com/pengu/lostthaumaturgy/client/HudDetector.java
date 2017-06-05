package com.pengu.lostthaumaturgy.client;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

public class HudDetector extends Gui
{
	public static final HudDetector instance = new HudDetector();
	
	private static ItemRenderer itemRenderer = new ItemRenderer(Minecraft.getMinecraft());
	protected FontRenderer fontRenderer;
	protected ResourceLocation detector = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_detector.png");
	
	public HudDetector()
	{
		this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
	}
	
	public DecimalFormat radFormat = new DecimalFormat("#0.000");
	private static final List<String> tooltip = new ArrayList<>();
	
	public void render(boolean vis, boolean taint, boolean radiation, SIAuraChunk aura, boolean isGoggles)
	{
		if(aura == null)
			return;
		
		int shift;
		Minecraft mc = Minecraft.getMinecraft();
		World w = mc.world;
		EntityPlayer p = mc.player;
		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int k = scaledresolution.getScaledWidth();
		int l = scaledresolution.getScaledHeight();
		
		int ttk = 32826;
		boolean ttke = GL11.glIsEnabled(ttk);
		mc.entityRenderer.setupOverlayRendering();
		GLRenderState.BLEND.captureState();
		GLRenderState.BLEND.on();
		GL11.glEnable(ttk);
		RenderHelper.enableStandardItemLighting();
		RenderHelper.disableStandardItemLighting();
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(detector);
		this.zLevel = -90.0f;
		double sv = 48F * (LTConfigs.aura_max - aura.vis) / (float) LTConfigs.aura_max;
		double st = 48F * (LTConfigs.aura_max - aura.taint) / (float) LTConfigs.aura_max;
		double sr = 48F * (LTConfigs.aura_radMax - aura.radiation) / (float) LTConfigs.aura_radMax;
		
		int n = shift = vis && taint && radiation ? 56 : 0;
		
		if(vis)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(!taint ? 15 : 0, 0, 0);
			if(aura.goodVibes > 0)
				RenderUtil.drawTexturedModalRect(k - 34, l - 17, 0, 72, 16, 16);
			RenderUtil.drawTexturedModalRect(k - 30, l - 67 + sv, 0 + shift, 0 + sv, 8, 48 - sv);
			if(vis && aura.previousVis < aura.vis)
				RenderUtil.drawTexturedModalRect(k - 30, l - 48, 72, 0, 8, 8);
			if(vis && aura.previousVis > aura.vis)
				RenderUtil.drawTexturedModalRect(k - 30, l - 48, 80, 0, 8, 8);
			RenderUtil.drawTexturedModalRect(k - 31, l - 71, 23, 0, 10, 74);
			GL11.glPopMatrix();
		}
		
		if(taint)
		{
			if(aura.badVibes > 0)
				RenderUtil.drawTexturedModalRect(k - 19, l - 17, 0, 72, 16, 16);
			RenderUtil.drawTexturedModalRect(k - 15, l - 67 + st, 8 + shift, 0 + st, 8, 48 - st);
			if(taint && aura.previousTaint < aura.taint)
				RenderUtil.drawTexturedModalRect(k - 15, l - 48, 72, 0, 8, 8);
			if(taint && aura.previousTaint > aura.taint)
				RenderUtil.drawTexturedModalRect(k - 15, l - 48, 80, 0, 8, 8);
			RenderUtil.drawTexturedModalRect(k - 16, l - 71, 39, 0, 10, 74);
		}
		
		if(radiation)
		{
			GL11.glPushMatrix();
			int off = 0;
			if(!taint) off += 15;
			if(!vis) off += 15;
			GL11.glTranslated(off, 0, 0);
			RenderUtil.drawTexturedModalRect(k - 45, l - 67 + sr, 79 + (shift / 56) * 9, 23 + sr, 8, 48 - sr);
			if(vis && taint && aura.previousRadiation < aura.radiation)
				RenderUtil.drawTexturedModalRect(k - 45, l - 48, 72, 0, 8, 8);
			if(vis && taint && aura.previousRadiation > aura.radiation)
				RenderUtil.drawTexturedModalRect(k - 45, l - 48, 80, 0, 8, 8);
			RenderUtil.drawTexturedModalRect(k - 46, l - 71, 96, 23, 10, 74);
			GL11.glPopMatrix();
		}
		
		if(isGoggles)
		{
			if(aura.goodVibes > 0)
				RenderUtil.drawTexturedModalRect(46, l - 28, 0, 72, 16, 16);
			if(aura.badVibes > 0)
				RenderUtil.drawTexturedModalRect(46, l - 18, 0, 72, 16, 16);
			RenderUtil.drawTexturedModalRect(50, l - 35, 97, 80, 8, 9);
			RenderUtil.drawTexturedModalRect(50, l - 25, 24, 57, 8, 9);
			RenderUtil.drawTexturedModalRect(50, l - 15, 40, 57, 8, 9);
			String dvis = "" + aura.vis + " V";
			String dtaint = "" + aura.taint + " T";
			
			this.fontRenderer.drawString(radFormat.format(aura.radiation) + " R", 6, l - 34, 0xCCCCEE);
			this.fontRenderer.drawString(dvis, 6, l - 24, 15650030);
			this.fontRenderer.drawString(dtaint, 6, l - 14, 10057625);
			dvis = "" + aura.goodVibes + "%";
			dtaint = "" + aura.badVibes + "%";
			this.fontRenderer.drawString(dvis, 61, l - 24, 15650030);
			this.fontRenderer.drawString(dtaint, 61, l - 14, 10057625);
			
			RayTraceResult res = mc.objectMouseOver;
			if(res != null && res.typeOfHit == Type.BLOCK && w.getTileEntity(res.getBlockPos()) instanceof IConnection)
			{
				IConnection tet = (IConnection) w.getTileEntity(res.getBlockPos());
				tet.addTooltipToGoggles(tooltip);
				
				int capp = Math.round((float) Math.round(tet.getPureVis()) / tet.getMaxVis() * 100.0f);
				int capt = Math.round((float) Math.round(tet.getTaintedVis()) / tet.getMaxVis() * 100.0f);
				dvis = "" + Math.round(tet.getPureVis()) + " V (" + capp + "%)";
				dtaint = "" + Math.round(tet.getTaintedVis()) + " T (" + capt + "%)";
				String pres = "" + tet.getVisSuction(null) + " Vis TCB, " + tet.getTaintSuction(null) + " Taint TCB";
				this.fontRenderer.drawString(dvis, 5 + k / 2, 5 + l / 2, 15650030);
				this.fontRenderer.drawString(dtaint, 5 + k / 2, 15 + l / 2, 10057625);
				int lastY = 25 + l / 2;
				if(tet.getTaintSuction(null) > 0 || tet.getVisSuction(null) > 0)
				{
					this.fontRenderer.drawString(pres, 5 + k / 2, 25 + l / 2, 8947848);
					lastY += 10;
				}
				
				for(int i = 0; i < tooltip.size(); ++i)
				{
					fontRenderer.drawString(tooltip.get(i), 5 + k / 2, lastY, 8947848);
					lastY += 10;
				}
				
				tooltip.clear();
			}
		}
		
		if(!ttke)
			GL11.glDisable((int) 32826);
		GLRenderState.BLEND.reset();
		GlStateManager.enableBlend();
		
		GL11.glColor4f(1, 1, 1, 1);
	}
}