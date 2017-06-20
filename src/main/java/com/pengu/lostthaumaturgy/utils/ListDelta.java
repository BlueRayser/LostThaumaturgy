package com.pengu.lostthaumaturgy.utils;

import java.util.ArrayList;
import java.util.List;

public class ListDelta
{
	public static <T> List<T> positiveDelta(List<T> a, List<T> b)
	{
		List<T> delta = new ArrayList<T>();
		List<T> everything = new ArrayList<T>();
		List<T> same = new ArrayList<T>();
		for(T ta : a)
			if(b.contains(ta))
				same.add(ta);
		everything.addAll(a);
		everything.addAll(b);
		for(T te : everything)
			if(!same.contains(te))
				delta.add(te);
		return delta;
	}
}