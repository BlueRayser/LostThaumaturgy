package com.pengu.lostthaumaturgy.api.wand;

public enum EnumCapLocation
{
	UP,
	DOWN;
	
	public EnumCapLocation opposite()
	{
		return this == UP ? DOWN : UP;
	}
	
	public String nbtName()
	{
		String n = name().toLowerCase();
		char c = n.charAt(0);
		n = Character.toUpperCase(c) + n.substring(1);
		return "WandCap" + n;
	}
}