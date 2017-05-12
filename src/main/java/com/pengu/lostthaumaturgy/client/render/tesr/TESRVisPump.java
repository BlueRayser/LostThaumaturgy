package com.pengu.lostthaumaturgy.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.client.model.ModelPump;
import com.pengu.lostthaumaturgy.tile.TileVisPump;

public class TESRVisPump extends TESR<TileVisPump>
{
	public static final TESRVisPump INSTANCE = new TESRVisPump();
	private ModelPump model = new ModelPump();
	private ResourceLocation pump = new ResourceLocation(LTInfo.MOD_ID, "textures/models/pump.png"), pump_off = new ResourceLocation(LTInfo.MOD_ID, "textures/models/pump_off.png");
	
	@Override
	public void renderTileEntityAt(TileVisPump te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		if(te.gettingPower())
			bindTexture(pump_off);
		else
			bindTexture(pump);
		
		renderEntityAt(te, x, y, z, te.ticksExisted);
		
		if(destroyStage != null)
		{
			bindTexture(destroyStage);
			renderEntityAt(te, x, y, z, te.ticksExisted);
		}
	}
	
	private void translateFromOrientation(double x, double y, double z, int orientation)
	{
		GL11.glTranslated((x + .5F), (y + .5F), (z + .5F));
		if(orientation != 0)
		{
			if(orientation == 1)
				GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			else if(orientation == 2)
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			else if(orientation == 3)
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			else if(orientation == 4)
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
			else if(orientation == 5)
				GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		}
		GL11.glTranslatef(0.0F, -1.0F, 0.0F);
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		bindTexture(this.pump);
		int frames = item.getCount() > 0 ? item.hashCode() : 0;
		frames += Minecraft.getSystemTime() / 50D % 32;
		if(item.getTagCompound() != null && item.getTagCompound().hasKey("frames", NBT.TAG_INT)) frames = item.getTagCompound().getInteger("frames");
		renderEntityAt(null, 0, 0, 0, frames);
	}
	
	public void renderEntityAt(TileVisPump pump, double x, double y, double z, double count)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		double bob = Math.abs(Math.sin(count / 10F));
		
		GLRenderState blend = GLRenderState.BLEND;
		
		blend.captureState();
		blend.on();
		
		GL11.glPushMatrix();
		GLRenderState.NORMALIZE.captureState();
		GLRenderState.NORMALIZE.on();
		GL11.glEnable(32826);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4d(1.0F, 1.0F, 1.0F, 1.0F);
		this.translateFromOrientation(x, y, z, pump == null ? EnumFacing.UP.ordinal() : pump.orientation.ordinal());
		GL11.glPushMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0.0F, (0.251F - bob / 4.0F), 0.0F);
		this.model.MoveBase.render(0.0625F);
		GL11.glPopMatrix();
		GL11.glTranslatef(0.0F, 0.875f, 0.0F);
		GL11.glPushMatrix();
		GL11.glScaled(1.0F, bob, 1.0F);
		this.model.MoveFrill.setRotationPoint(-5.0F, 0.0F, -5.0F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		this.model.MoveFrill.render(0.0625F);
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		this.model.render();
		GL11.glDisable(32826);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		
		GLRenderState.NORMALIZE.reset();
		blend.reset();
	}
}