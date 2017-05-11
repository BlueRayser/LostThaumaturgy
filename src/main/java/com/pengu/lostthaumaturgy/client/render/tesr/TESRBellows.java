package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.model.ModelBellows;
import com.pengu.lostthaumaturgy.tile.TileBellows;

public class TESRBellows extends TESR<TileBellows>
{
	public static final TESRBellows INSTANCE = new TESRBellows();
	private ModelBellows model = new ModelBellows();
	private ResourceLocation texture = new ResourceLocation(LTInfo.MOD_ID, "textures/models/bellows.png");
	
	@Override
	public void renderTileEntityAt(TileBellows te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		renderEntityAt(te, x, y, z, te.scale);
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		renderEntityAt(null, 0, 0, 0, 1);
	}
	
	private void translateFromOrientation(double x, double y, double z, int orientation)
	{
		GL11.glTranslated((x + .5F), (y - .5F), (z + .5F));
		if(orientation == 0)
			GL11.glRotatef(180, 0, 1, 0);
		else if(orientation == 1)
			GL11.glRotatef(90, 0, 1, 0);
		else if(orientation != 2 && orientation == 3)
			GL11.glRotatef(270, 0, 1, 0);
	}
	
	public void renderEntityAt(TileBellows bellows, double x, double y, double z, float scale)
	{
		GLRenderState blend = GLRenderState.BLEND, normalize = GLRenderState.NORMALIZE;
		
		blend.captureState();
		normalize.captureState();
		
		float tscale = .125F + scale * .875F;
		bindTexture(texture);
		
		blend.on();
		normalize.on();
		
		GL11.glPushMatrix();
		GL11.glEnable(32826);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1, 1, 1, 1);
		translateFromOrientation(x, y, z, bellows != null ? bellows.orientation : 0);
		GL11.glTranslatef(0, 1, 0);
		GL11.glPushMatrix();
		GL11.glScalef(.5F, ((scale + .1F) / 2), .5F);
		model.Bag.setRotationPoint(0, .5F, 0);
		model.Bag.render(.0625F);
		GL11.glScalef(1, 1, 1);
		GL11.glPopMatrix();
		GL11.glTranslatef(0, -1, 0);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, ((-tscale) / 2 + .5F), 0);
		model.TopPlank.render(.0625f);
		GL11.glTranslatef(0, (tscale / 2 - .5F), 0);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(0, (tscale / 2 - .5F), 0);
		model.BottomPlank.render(.0625F);
		GL11.glTranslatef(0, ((-tscale) / 2 + .5F), 0);
		GL11.glPopMatrix();
		model.render();
		GL11.glDisable(32826);
		GL11.glPopMatrix();
		blend.reset();
		normalize.reset();
		GL11.glColor4f(1, 1, 1, 1);
	}
}