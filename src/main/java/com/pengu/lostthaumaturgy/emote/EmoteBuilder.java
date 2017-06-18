package com.pengu.lostthaumaturgy.emote;

import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.net.PacketEmote;

public class EmoteBuilder
{
	public final EmoteData data;
	
	public EmoteBuilder(int world, double x, double y, double z, String id)
	{
		data = new EmoteData(world, x, y, z, id);
	}
	
	public EmoteBuilder setLifespan(int fadeIn, int stay, int fadeOut)
	{
		data.fadeIn = fadeIn;
		data.stay = stay;
		data.fadeOut = fadeOut;
		return this;
	}
	
	public EmoteBuilder setColorRGB(int rgb)
	{
		data.red = ((rgb >> 16) & 0xFF) / 255F;
		data.green = ((rgb >> 8) & 0xFF) / 255F;
		data.blue = ((rgb >> 0) & 0xFF) / 255F;
		return this;
	}
	
	public EmoteBuilder setColorRGB(float r, float g, float b)
	{
		data.red = r;
		data.green = g;
		data.blue = b;
		return this;
	}
	
	public EmoteBuilder setColorAlpha(float alpha)
	{
		data.alpha = alpha;
		return this;
	}
	
	public EmoteBuilder setColorRGBA(int rgba)
	{
		data.red = ((rgba >> 24) & 0xFF) / 255F;
		data.green = ((rgba >> 16) & 0xFF) / 255F;
		data.blue = ((rgba >> 8) & 0xFF) / 255F;
		data.alpha = ((rgba >> 0) & 0xFF) / 255F;
		return this;
	}
	
	public EmoteBuilder setColorARGB(int argb)
	{
		data.alpha = ((argb >> 24) & 0xFF) / 255F;
		data.red = ((argb >> 16) & 0xFF) / 255F;
		data.green = ((argb >> 8) & 0xFF) / 255F;
		data.blue = ((argb >> 0) & 0xFF) / 255F;
		return this;
	}
	
	public EmoteBuilder setScale(float scale)
	{
		data.scale = scale;
		return this;
	}
	
	public void build()
	{
		HCNetwork.manager.sendToAllAround(new PacketEmote(data), new TargetPoint(data.getWorld(), data.getX(), data.getY(), data.getZ(), 64));
	}
}