package com.pengu.lostthaumaturgy.api.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.utils.WorldLocation;

public class ConnectionManager
{
	public static IConnection getConnection(World world, BlockPos pos, EnumFacing facing)
	{
		TileEntity tile = world.getTileEntity(pos.offset(facing));
		if(tile != null)
		{
			IConnection c = null;
			if(tile.hasCapability(CapabilityVisConnection.VIS, facing.getOpposite()))
				c = tile.getCapability(CapabilityVisConnection.VIS, facing.getOpposite());
			else if(tile instanceof IConnection)
				c = (IConnection) tile;
			if(c != null && c.getConnectable(facing.getOpposite()))
				return c;
		}
		return null;
	}
	
	public static IConnection getConnection(WorldLocation loc, EnumFacing facing)
	{
		return getConnection(loc.getWorld(), loc.getPos(), facing);
	}
}