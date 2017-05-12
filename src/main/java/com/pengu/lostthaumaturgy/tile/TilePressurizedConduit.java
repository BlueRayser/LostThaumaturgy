package com.pengu.lostthaumaturgy.tile;

import java.util.List;

import net.minecraft.util.text.translation.I18n;

import org.apache.commons.lang3.ArrayUtils;

import com.mrdimka.hammercore.vec.Cuboid6;

public class TilePressurizedConduit extends TileConduit
{
	@Override
	public void tick()
	{
		int suction = getSuction(null);
		super.tick();
		if(suction != getSuction(null))
		{
			world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), world.getBlockState(pos), world.getBlockState(pos), 3);
			sync();
		}
	}
	
	@Override
	public void rebake()
	{
		super.rebake();
		hitboxes = ArrayUtils.add(hitboxes, new Cuboid6(3.5 / 16, 3.5 / 16, 3.5 / 16, 12.5 / 16, 12.5 / 16, 12.5 / 16));
	}
	
	public void addTooltipToGoggles(List<String> tooltip)
	{
		tooltip.add(I18n.translateToLocal("itemGroup.redstone") + ": " + Math.min(getSuction(null), 15));
	}
}