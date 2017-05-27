package com.pengu.lostthaumaturgy.events;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.annotations.MCFBus;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.gui.GuiManager;
import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;

@MCFBus
public class InteractionEvents
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void interactWithUpgradable(PlayerInteractEvent.RightClickBlock e)
	{
		World world = e.getWorld();
		BlockPos pos = e.getPos();
		ItemStack held = e.getItemStack();
		
		WorldLocation l = new WorldLocation(world, pos);
		
		if(!held.isEmpty())
		{
			ItemUpgrade item = WorldUtil.cast(held.getItem(), ItemUpgrade.class);
			IUpgradable tile = WorldUtil.cast(world.getTileEntity(pos), IUpgradable.class);
			
			if(item != null && tile != null)
			{
				if(!world.isRemote && tile.setUpgrade(ItemUpgrade.idFromItem(item)))
				{
					held.shrink(1);
					HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":upgrade", pos, 1F, .8F + world.rand.nextFloat() * .4F, SoundCategory.PLAYERS);
					TileSyncable t = WorldUtil.cast(tile, TileSyncable.class);
					if(t != null)
						t.sync();
					
					e.setUseBlock(Result.DENY);
					e.setUseItem(Result.DENY);
					e.setCanceled(true);
				}
				e.getEntityPlayer().swingArm(e.getHand());
			} else if(held.getItem() == ItemsLT.WAND_REVERSAL)
			{
				if(!world.isRemote && tile != null && tile.dropUpgrade(e.getEntityPlayer()))
				{
					held.damageItem(1, e.getEntityLiving());
					HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":zap", pos, 1F, .8F + world.rand.nextFloat() * .4F, SoundCategory.PLAYERS);
					
					TileSyncable t = WorldUtil.cast(tile, TileSyncable.class);
					if(t != null)
						t.sync();
					
					e.setUseBlock(Result.DENY);
					e.setUseItem(Result.DENY);
					e.setCanceled(true);
				}
				e.getEntityPlayer().swingArm(e.getHand());
			}
		}
	}
	
	@SubscribeEvent
	public void blockBroken(BlockEvent.BreakEvent e)
	{
		WorldLocation loc = new WorldLocation(e.getWorld(), e.getPos());
		TileEntity tile = loc.getTile();
		
		if(tile instanceof IUpgradable)
		{
			IUpgradable upgradable = (IUpgradable) tile;
			while(upgradable.getInstalledUpgradeCount() > 0)
				upgradable.dropUpgrade(e.getPlayer());
		}
		
		AuraTicker.spillTaint(e.getWorld(), e.getPos());
	}
}