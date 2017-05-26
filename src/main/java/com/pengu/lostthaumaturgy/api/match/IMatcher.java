package com.pengu.lostthaumaturgy.api.match;

public interface IMatcher<T>
{
	T defaultInstance();
	
	boolean matches(T t);
}