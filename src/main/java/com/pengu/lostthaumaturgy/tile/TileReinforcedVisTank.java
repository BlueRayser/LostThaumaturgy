package com.pengu.lostthaumaturgy.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;

import com.google.common.base.Predicate;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.math.MathHelper;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;

public class TileReinforcedVisTank extends TileVisTank
{
	{
		breakchance = 3333;
		canBreak = false;
	}
	
	@Override
	public float getMaxVis()
	{
		return 1000;
	}
}