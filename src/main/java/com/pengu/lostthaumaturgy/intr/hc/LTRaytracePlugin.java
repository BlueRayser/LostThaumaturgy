package com.pengu.lostthaumaturgy.intr.hc;

import com.mrdimka.hammercore.api.mhb.IRayCubeRegistry;
import com.mrdimka.hammercore.api.mhb.IRayRegistry;
import com.mrdimka.hammercore.api.mhb.RaytracePlugin;
import com.pengu.lostthaumaturgy.block.BlockAdvancedVisValve;
import com.pengu.lostthaumaturgy.block.BlockConduit;
import com.pengu.lostthaumaturgy.block.BlockPressurizedConduit;
import com.pengu.lostthaumaturgy.block.BlockVisValve;
import com.pengu.lostthaumaturgy.init.BlocksLT;

@RaytracePlugin
public class LTRaytracePlugin implements IRayRegistry
{
	@Override
	public void registerCubes(IRayCubeRegistry cube)
	{
		cube.bindBlockCubeManager((BlockConduit) BlocksLT.CONDUIT, (BlockConduit) BlocksLT.CONDUIT);
		cube.bindBlockCubeManager((BlockVisValve) BlocksLT.VIS_VALVE, (BlockVisValve) BlocksLT.VIS_VALVE);
		cube.bindBlockCubeManager((BlockAdvancedVisValve) BlocksLT.ADVANCED_VIS_VALVE, (BlockAdvancedVisValve) BlocksLT.ADVANCED_VIS_VALVE);
		cube.bindBlockCubeManager((BlockPressurizedConduit) BlocksLT.PRESSURIZED_CONDUIT, (BlockPressurizedConduit) BlocksLT.PRESSURIZED_CONDUIT);
	}
}