package com.pengu.lostthaumaturgy.core.utils;

import java.util.ArrayList;
import java.util.List;

public class ListDelta
{
	public static <T> List<T> positiveDelta(List<T> a, List<T> b)
	{
		List<T> delta = new ArrayList<T>();
		delta.addAll(b);
		delta.removeAll(a);
		return delta;
	}
}