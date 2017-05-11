package com.pengu.lostthaumaturgy.api.match;

import net.minecraft.item.ItemStack;

public interface IMatcher<T>
{
	T defaultInstance();
	boolean matches(T t);
}