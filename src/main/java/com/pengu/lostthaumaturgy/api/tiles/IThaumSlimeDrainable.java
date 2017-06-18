package com.pengu.lostthaumaturgy.api.tiles;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.utils.ListUtils;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.entity.EntityThaumSlime;

public interface IThaumSlimeDrainable
{
	default void attractSlimeToTile(int rad, WorldLocation location, int maxSlimes)
	{
		AxisAlignedBB aabb = new AxisAlignedBB(location.getPos()).grow(rad);
		List<EntityThaumSlime> slimes = ListUtils.randomizeList(location.getWorld().getEntitiesWithinAABB(EntityThaumSlime.class, aabb), location.getWorld().rand);
		for(EntityThaumSlime s : slimes)
		{
			s.faceTileEntity(WorldUtil.cast(this, TileEntity.class), 20, 20);
			maxSlimes--;
			if(maxSlimes <= 0)
				break;
		}
	}
	
	/** Make your magic happen! */
	void onDrained(EntityThaumSlime slime);
}