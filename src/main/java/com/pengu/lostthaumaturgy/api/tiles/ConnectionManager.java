package com.pengu.lostthaumaturgy.api.tiles;

import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConnectionManager
{
	public static IConnection getConnection(World world, BlockPos pos, EnumFacing facing)
	{
		TileEntity tile = world.getTileEntity(pos.offset(facing));
		if(tile != null)
		{
			if(tile.hasCapability(CapabilityVisConnection.VIS, facing.getOpposite()))
				return tile.getCapability(CapabilityVisConnection.VIS, facing.getOpposite());
			else if(tile instanceof IConnection)
				return (IConnection) tile;
		}
		return null;
	}
	
	public static IConnection getConnection(WorldLocation loc, EnumFacing facing)
	{
		return getConnection(loc.getWorld(), loc.getPos(), facing);
	}
}