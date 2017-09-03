package com.pengu.lostthaumaturgy.api.research.client;

import com.pengu.lostthaumaturgy.api.research.ResearchPage;

public interface IRenderExtension<T extends ResearchPage>
{
	void render(T recipe, int side, int x, int y, int mx, int my);
}