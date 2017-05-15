package com.pengu.lostthaumaturgy.client.render.shared;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

import org.lwjgl.opengl.ARBShaderObjects;

import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.renderer.shader.HCShaderPipeline;
import com.mrdimka.hammercore.client.renderer.shader.IShaderOperation;
import com.mrdimka.hammercore.client.renderer.shader.ShaderProgram;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public class LiquidVisRenderer
{
	public static ShaderProgram visShader;
	public static LiquidVisOperation operation;
	
	public static float getVisSaturation(float taintedVis, float pureVis)
	{
		if(taintedVis + pureVis <= 0.001F) return 1;
		return Math.min(1, taintedVis / (taintedVis + pureVis));
	}
	
	public static void reloadShader()
	{
		try
		{
			if(visShader != null) visShader.cleanup();
			visShader = new ShaderProgram();
			visShader.attachFrag("/assets/" + LTInfo.MOD_ID + "/shaders/liquid_vis.fsh");
			visShader.attachVert("/assets/" + LTInfo.MOD_ID + "/shaders/liquid_vis.vsh");
			visShader.attachShaderOperation(operation = new LiquidVisOperation(HCShaderPipeline.registerOperation()));
			visShader.validate();
		}
		catch(Throwable err)
		{
			err.printStackTrace();
		}
	}
	
	public static void renderIntoGui(double xCoord, double yCoord, double widthIn, double heightIn, float visSaturation)
	{
		TextureAtlasSprite vis = ClientProxy.getSprite(LTInfo.MOD_ID + ":blocks/fluid_vis");
		
		if(useShaders() && visShader == null) reloadShader();
		if(useShaders() && visShader != null)
		{
			operation.red = (120 + visSaturation * 80) / 255F;
			operation.green = 0;
			operation.blue = 1;
			operation.setResolution(200);
			visShader.freeBindShader();
			
			int loc = visShader.getUniformLoc("alpha");
			ARBShaderObjects.glUniform1fARB(loc, 0.5F);
		}else
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		RenderUtil.drawTexturedModalRect(xCoord, yCoord, vis, widthIn, heightIn);
		
		if(useShaders()) ShaderProgram.unbindShader();
	}
	
	public static void finishDrawWithShaders(Tessellator tess, float visSaturation)
	{
		if(useShaders() && visShader == null) reloadShader();
		if(useShaders() && visShader != null)
		{
			operation.red = (120 + visSaturation * 80) / 255F;
			operation.green = 0;
			operation.blue = 1;
			operation.setResolution(200);
			visShader.freeBindShader();
			
			int loc = visShader.getUniformLoc("alpha");
			ARBShaderObjects.glUniform1fARB(loc, 0.5F);
		}
		
		GLRenderState.BLEND.on();
		tess.draw();
		
		if(useShaders()) ShaderProgram.unbindShader();
	}
	
	public static class LiquidVisOperation implements IShaderOperation
	{
		public final int op;
		
		public float red, green, blue;
		public int resWidth, resHeight;
		
		public LiquidVisOperation(int op)
        {
			this.op = op;
        }
		
		@Override
        public boolean load(ShaderProgram program)
        {
	        return true;
        }
		
		@Override
        public void operate(ShaderProgram program)
        {
			int loc = program.getUniformLoc("time");
			ARBShaderObjects.glUniform1fARB(loc, (float) (Minecraft.getMinecraft().world != null ? Minecraft.getMinecraft().world.getTotalWorldTime() / 10D : Minecraft.getSystemTime() / 5000D));
			
			loc = program.getUniformLoc("color");
			ARBShaderObjects.glUniform3fARB(loc, red, green, blue);
			
			loc = program.getUniformLoc("resolution");
			ARBShaderObjects.glUniform2fARB(loc, resWidth, resHeight);
        }
		
		@Override
        public int operationID()
        {
	        return op;
        }
		
		public LiquidVisOperation setColor(int rgb)
		{
			red = ((rgb << 16) & 0xFF) / 255F;
			green = ((rgb << 8) & 0xFF) / 255F;
			blue = ((rgb << 0) & 0xFF) / 255F;
			return this;
		}
		
		public LiquidVisOperation setResolution(int res)
		{
			resHeight = res;
			resWidth = res;
			return this;
		}
	}
	
	public static boolean useShaders()
	{
		return OpenGlHelper.shadersSupported && LTConfigs.client_useShaders;
	}
}