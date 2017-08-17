package com.pengu.lostthaumaturgy.api.tiles;

import java.util.List;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import com.pengu.hammercore.api.handlers.ITileHandler;

public interface IConnection extends ITileHandler
{
	boolean getConnectable(EnumFacing to);
	
	boolean isVisSource();
	
	boolean isVisConduit();
	
	float[] subtractVis(float vis);
	
	float getPureVis();
	
	void setPureVis(float vis);
	
	float getTaintedVis();
	
	void setTaintedVis(float vis);
	
	float getMaxVis();
	
	int getVisSuction(BlockPos to);
	
	void setVisSuction(int amt);
	
	int getTaintSuction(BlockPos to);
	
	void setTaintSuction(int amt);
	
	void setSuction(int amt);
	
	int getSuction(BlockPos to);
	
	default void addTooltipToGoggles(List<String> tooltip)
	{
	};
}