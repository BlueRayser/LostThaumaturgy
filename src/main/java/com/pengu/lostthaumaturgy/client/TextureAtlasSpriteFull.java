package com.pengu.lostthaumaturgy.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TextureAtlasSpriteFull extends TextureAtlasSprite
{
	public static final TextureAtlasSpriteFull sprite = new TextureAtlasSpriteFull("full");
	
	protected TextureAtlasSpriteFull(String spriteName)
	{
		super(spriteName);
	}
	
	@Override
	public float getMinU()
	{
		return 0;
	}
	
	@Override
	public float getMinV()
	{
		return 0;
	}
	
	@Override
	public float getMaxU()
	{
		return 1;
	}
	
	@Override
	public float getMaxV()
	{
		return 1;
	}
}