package com.pengu.lostthaumaturgy.items.tools.axe;

import java.util.List;

import net.minecraft.block.BlockLog;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;
import com.pengu.lostthaumaturgy.init.SoundEventsLT;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXBubble;
import com.pengu.lostthaumaturgy.utils.WoodHelper;

public class ItemAxeElemental extends ItemAxe
{
	public ItemAxeElemental()
	{
		super(ItemMaterialsLT.tool_elemental, 10, -2.8F);
		setUnlocalizedName("elemental_axe");
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		WorldLocation loc = new WorldLocation(player.world, pos);
		
		if(loc.getBlock() instanceof BlockLog && !player.isSneaking())
		{
			itemstack.damageItem(1, player);
			
			if(!player.world.isRemote)
			{
				WoodHelper helper = WoodHelper.newHelper(loc, 64);
				BlockPos woodPos = helper.getFarthest(pos);
				
				if(woodPos == null)
					return true;
				
				for(int i = 0; i < 16; ++i)
					HCNetwork.manager.sendToAllAround(new PacketFXBubble(woodPos.getX() + player.getRNG().nextFloat(), woodPos.getY() + player.getRNG().nextFloat(), woodPos.getZ() + player.getRNG().nextFloat()), loc.getPointWithRad(64));
				
				SoundEventsLT.SWING.playAt(new WorldLocation(player.world, woodPos), 1F, 1F, SoundCategory.PLAYERS);
				
				AxisAlignedBB aabb = new AxisAlignedBB(woodPos).grow(1);
				List<EntityItem> before = player.world.getEntitiesWithinAABB(EntityItem.class, aabb);
				player.world.destroyBlock(woodPos, true);
				List<EntityItem> after = player.world.getEntitiesWithinAABB(EntityItem.class, aabb);
				
				after.removeAll(before);
				
				// List<EntityItem> drops = ListDelta.positiveDelta(before,
				// after);
				for(EntityItem drop : after)
				{
					drop.setPositionAndUpdate(player.posX, player.posY, player.posZ);
					drop.setNoPickupDelay();
				}
			}
			
			return true;
		}
		
		return false;
	}
}