package com.pengu.lostthaumaturgy.intr.hc;

import com.mrdimka.hammercore.api.mhb.IRayCubeRegistry;
import com.mrdimka.hammercore.api.mhb.IRayRegistry;
import com.mrdimka.hammercore.api.mhb.RaytracePlugin;
import com.pengu.lostthaumaturgy.block.BlockConduit;
import com.pengu.lostthaumaturgy.init.BlocksLT;

@RaytracePlugin
public class LTRaytracePlugin implements IRayRegistry
{
	@Override
    public void registerCubes(IRayCubeRegistry cube)
    {
		cube.bindBlockCubeManager((BlockConduit) BlocksLT.CONDUIT, (BlockConduit) BlocksLT.CONDUIT);
    }
}