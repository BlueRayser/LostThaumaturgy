package com.pengu.lostthaumaturgy.tile;

import net.minecraft.util.math.BlockPos;

public class TileCrucibleVoid extends TileCrucible
{
	{
		setTier(1000, .8F, 1);
		radMod = .5F;
	}
	
	private boolean emited = false;
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(emitsPower() != emited)
		{
			emited = emitsPower();
			for(int x = -1; x < 2; ++x)
				for(int y = -1; y < 2; ++y)
					for(int z = -1; z < 2; ++z)
					{
						BlockPos pos = this.pos.add(x, y, z);
						world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), world.getBlockState(pos), world.getBlockState(pos), 3);
					}
		}
	}
	
	public boolean emitsPower()
	{
		return pureVis + taintedVis >= maxVis * .9F;
	}
}